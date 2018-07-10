package com.fanwe.lib.blur.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.fanwe.lib.blur.core.Blur;
import com.fanwe.lib.blur.core.BlurFactory;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

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
        if (mMapInvoker != null)
        {
            for (Map.Entry<BlurInvoker, Future> item : mMapInvoker.entrySet())
            {
                item.getValue().cancel(true);
            }
            mMapInvoker.clear();
        }

        if (needSynchronized())
        {
            synchronized (mBlur)
            {
                mBlur.destroy();
            }
        } else
        {
            mBlur.destroy();
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

    private boolean needSynchronized()
    {
        if (mMapInvoker == null || mMapInvoker.isEmpty())
            return false;
        return true;
    }

    private abstract class SourceInvoker<S> extends BaseInvoker
    {
        public SourceInvoker(S source, boolean async)
        {
            super(async);
        }

        @Override
        public final BlurInvoker cancelAsync()
        {
            if (mMapInvoker != null)
            {
                final Future future = mMapInvoker.remove(this);
                if (future != null)
                    future.cancel(true);
            }
            return this;
        }

        @Override
        protected final void notifyTarget(BlurTarget target, boolean async)
        {
            cancelAsync();
            if (async)
            {
                final Future future = EXECUTOR_SERVICE.submit(new BlurTask(new Callable<Bitmap>()
                {
                    @Override
                    public Bitmap call() throws Exception
                    {
                        return blurSource();
                    }
                }, this, target));

                if (mMapInvoker == null)
                    mMapInvoker = new ConcurrentHashMap<>();
                mMapInvoker.put(this, future);
            } else
            {
                target.onBlurred(blurSource());
            }
        }

        private Bitmap blurSource()
        {
            if (needSynchronized())
            {
                synchronized (mBlur)
                {
                    return blurSourceImplemention();
                }
            } else
            {
                return blurSourceImplemention();
            }
        }

        protected abstract Bitmap blurSourceImplemention();
    }

    private final class BlurTask extends FutureTask<Bitmap>
    {
        private final BlurInvoker mInvoker;
        private final BlurTarget mTarget;

        public BlurTask(Callable<Bitmap> callable, BlurInvoker invoker, BlurTarget target)
        {
            super(callable);
            mInvoker = invoker;
            mTarget = target;
        }

        @Override
        protected void done()
        {
            synchronized (mBlur)
            {
                try
                {
                    mTarget.onBlurred(get());
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                } catch (ExecutionException e)
                {
                    e.printStackTrace();
                } finally
                {
                    mMapInvoker.remove(mInvoker);
                    if (mBlur.isDestroyAfterBlur())
                        mBlur.destroy();
                }
            }
        }
    }

    private final class ViewInvoker extends SourceInvoker<View>
    {
        private final WeakReference<View> mView;

        public ViewInvoker(View source, boolean async)
        {
            super(source, async);
            mView = new WeakReference<>(source);
        }

        @Override
        protected Bitmap blurSourceImplemention()
        {
            final View view = mView == null ? null : mView.get();
            return mBlur.blur(view);
        }
    }

    private final class DrawableInvoker extends SourceInvoker<Drawable>
    {
        private final Drawable mDrawable;

        public DrawableInvoker(Drawable source, boolean async)
        {
            super(source, async);
            mDrawable = source;
        }

        @Override
        protected Bitmap blurSourceImplemention()
        {
            return mBlur.blur(mDrawable);
        }
    }

    private final class BitmapInvoker extends SourceInvoker<Bitmap>
    {
        private final Bitmap mBitmap;

        public BitmapInvoker(Bitmap source, boolean async)
        {
            super(source, async);
            mBitmap = source;
        }

        @Override
        protected Bitmap blurSourceImplemention()
        {
            return mBlur.blur(mBitmap);
        }
    }
}
