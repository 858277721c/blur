package com.fanwe.lib.blur.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.fanwe.lib.blur.core.Blur;
import com.fanwe.lib.blur.core.BlurFactory;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class SimpleBlurApi implements BlurApi, BlurApi.Config
{
    private final Blur mBlur;
    private boolean mAsync;

    SimpleBlurApi(Context context)
    {
        mBlur = BlurFactory.create(context);
        mBlur.setDestroyAfterBlur(true);
    }

    @Override
    public BlurApi radius(int radius)
    {
        mBlur.setRadius(radius);
        return this;
    }

    @Override
    public BlurApi downSampling(int downSampling)
    {
        mBlur.setDownSampling(downSampling);
        return this;
    }

    @Override
    public BlurApi color(int color)
    {
        mBlur.setColor(color);
        return this;
    }

    @Override
    public BlurApi keepDownSamplingSize(boolean keepDownSamplingSize)
    {
        mBlur.setKeepDownSamplingSize(keepDownSamplingSize);
        return this;
    }

    @Override
    public BlurApi destroyAfterBlur(boolean destroyAfterBlur)
    {
        mBlur.setDestroyAfterBlur(destroyAfterBlur);
        return this;
    }

    @Override
    public BlurApi async(boolean async)
    {
        mAsync = async;
        return this;
    }

    @Override
    public Config config()
    {
        return this;
    }

    @Override
    public BlurApi destroy()
    {
        mBlur.destroy();
        if (mMapInvoker != null)
        {
            for (Map.Entry<BlurInvoker, Future> item : mMapInvoker.entrySet())
            {
                item.getValue().cancel(true);
            }
            mMapInvoker.clear();
        }
        return this;
    }

    @Override
    public BlurInvoker blur(View view)
    {
        return new ViewInvoker(view, mAsync);
    }

    @Override
    public BlurInvoker blur(Drawable drawable)
    {
        return new DrawableInvoker(drawable, mAsync);
    }

    @Override
    public BlurInvoker blur(Bitmap bitmap)
    {
        return new BitmapInvoker(bitmap, mAsync);
    }

    @Override
    public int getRadius()
    {
        return mBlur.getRadius();
    }

    @Override
    public int getDownSampling()
    {
        return mBlur.getDownSampling();
    }

    @Override
    public int getColor()
    {
        return mBlur.getColor();
    }

    @Override
    public boolean isKeepDownSamplingSize()
    {
        return mBlur.isKeepDownSamplingSize();
    }

    @Override
    public boolean isDestroyAfterBlur()
    {
        return mBlur.isDestroyAfterBlur();
    }

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    private Map<BlurInvoker, Future> mMapInvoker;

    private abstract class InternalInvoker<S> extends BaseInvoker<S>
    {
        public InternalInvoker(S source, boolean async)
        {
            super(source, async);
        }

        @Override
        public final BlurInvoker cancelAsync()
        {
            if (mMapInvoker != null)
            {
                final Future future = mMapInvoker.get(this);
                if (future != null)
                    future.cancel(true);
            }
            return this;
        }

        @Override
        protected final void notifyTarget(final Target target)
        {
            cancelAsync();
            if (isAsync())
            {
                final Future future = EXECUTOR_SERVICE.submit(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        synchronized (mBlur)
                        {
                            target.onBlurred(blurSource());
                        }
                    }
                });

                if (mMapInvoker == null)
                    mMapInvoker = new WeakHashMap<>();
                mMapInvoker.put(this, future);
            } else
            {
                target.onBlurred(blurSource());
            }
        }
    }

    private final class ViewInvoker extends InternalInvoker<View>
    {
        private final WeakReference<View> mView;

        public ViewInvoker(View source, boolean async)
        {
            super(source, async);
            mView = new WeakReference<>(source);
        }

        @Override
        protected Bitmap blurSource()
        {
            final View view = mView == null ? null : mView.get();
            return mBlur.blur(view);
        }
    }

    private final class DrawableInvoker extends InternalInvoker<Drawable>
    {
        private final Drawable mDrawable;

        public DrawableInvoker(Drawable source, boolean async)
        {
            super(source, async);
            mDrawable = source;
        }

        @Override
        protected Bitmap blurSource()
        {
            return mBlur.blur(mDrawable);
        }
    }

    private final class BitmapInvoker extends InternalInvoker<Bitmap>
    {
        private final Bitmap mBitmap;

        public BitmapInvoker(Bitmap source, boolean async)
        {
            super(source, async);
            mBitmap = source;
        }

        @Override
        protected Bitmap blurSource()
        {
            return mBlur.blur(mBitmap);
        }
    }
}
