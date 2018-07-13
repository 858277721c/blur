package com.fanwe.lib.blur.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.fanwe.lib.blur.api.target.BackgroundTarget;
import com.fanwe.lib.blur.api.target.BlurTarget;
import com.fanwe.lib.blur.api.target.ImageViewTarget;
import com.fanwe.lib.blur.api.target.MainThreadTargetWrapper;
import com.fanwe.lib.blur.core.Blur;
import com.fanwe.lib.blur.core.BlurFactory;
import com.fanwe.lib.blur.core.source.BlurSource;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

class SimpleBlurApi implements BlurApi, BlurApi.Settings
{
    private final Blur mBlur;
    private Blur mSynchronizedBlur;

    public SimpleBlurApi(Context context)
    {
        mBlur = BlurFactory.create(context);
        mBlur.setDestroyAfterBlur(true);
    }

    private Blur getBlur()
    {
        if (mMapInvoker == null || mMapInvoker.isEmpty())
            return mBlur;
        else
        {
            if (mSynchronizedBlur == null)
                mSynchronizedBlur = BlurFactory.synchronizedBlur(mBlur);
            return mSynchronizedBlur;
        }
    }

    @Override
    public BlurApi setRadius(int radius)
    {
        getBlur().setRadius(radius);
        return this;
    }

    @Override
    public BlurApi setDownSampling(int downSampling)
    {
        getBlur().setDownSampling(downSampling);
        return this;
    }

    @Override
    public BlurApi setColor(int color)
    {
        getBlur().setColor(color);
        return this;
    }

    @Override
    public BlurApi setKeepDownSamplingSize(boolean keepDownSamplingSize)
    {
        getBlur().setKeepDownSamplingSize(keepDownSamplingSize);
        return this;
    }

    @Override
    public BlurApi setDestroyAfterBlur(boolean destroyAfterBlur)
    {
        getBlur().setDestroyAfterBlur(destroyAfterBlur);
        return this;
    }

    @Override
    public Settings settings()
    {
        return this;
    }

    @Override
    public int getRadius()
    {
        return getBlur().getRadius();
    }

    @Override
    public int getDownSampling()
    {
        return getBlur().getDownSampling();
    }

    @Override
    public int getColor()
    {
        return getBlur().getColor();
    }

    @Override
    public boolean isKeepDownSamplingSize()
    {
        return getBlur().isKeepDownSamplingSize();
    }

    @Override
    public boolean isDestroyAfterBlur()
    {
        return getBlur().isDestroyAfterBlur();
    }

    @Override
    public Bitmap bitmap(Bitmap source)
    {
        return getBlur().blur(source);
    }

    @Override
    public Bitmap bitmap(View source)
    {
        return getBlur().blur(source);
    }

    @Override
    public Bitmap bitmap(Drawable source)
    {
        return getBlur().blur(source);
    }

    @Override
    public Bitmap bitmap(BlurSource source)
    {
        return getBlur().blur(source);
    }

    @Override
    public Invoker blur(Bitmap bitmap)
    {
        return new BitmapInvoker(bitmap);
    }

    @Override
    public Invoker blur(View view)
    {
        return new ViewInvoker(view);
    }

    @Override
    public Invoker blur(Drawable drawable)
    {
        return new DrawableInvoker(drawable);
    }

    @Override
    public BlurApi destroy()
    {
        if (mMapInvoker != null)
        {
            for (Map.Entry<Invoker, Future> item : mMapInvoker.entrySet())
            {
                item.getValue().cancel(true);
            }
            mMapInvoker.clear();
        }

        getBlur().destroy();
        return this;
    }

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    private Map<Invoker, Future> mMapInvoker;

    private abstract class SourceInvoker<S> implements Invoker
    {
        private boolean mAsync;
        private boolean mHasInvoke;

        public SourceInvoker(S source)
        {
        }

        @Override
        public final Invoker async(boolean async)
        {
            mAsync = async;
            return this;
        }

        @Override
        public final Cancelable into(ImageView imageView)
        {
            if (imageView != null)
                into(new ImageViewTarget(imageView));
            return this;
        }

        @Override
        public final Cancelable intoBackground(View view)
        {
            if (view != null)
                into(new BackgroundTarget(view));
            return this;
        }

        @Override
        public final synchronized Cancelable into(BlurTarget target)
        {
            if (target != null)
            {
                if (mHasInvoke)
                    throw new RuntimeException("current instance's (into(BlurTarget)) method can only be called once");

                mHasInvoke = true;
                target = new MainThreadTargetWrapper(target);
                notifyTargetInternal(target);
            }
            return this;
        }

        @Override
        public final void cancel()
        {
            if (mMapInvoker != null)
            {
                final Future future = mMapInvoker.remove(this);
                if (future != null)
                    future.cancel(true);
            }
        }

        private void notifyTargetInternal(BlurTarget target)
        {
            S source = getSource();
            if (source == null)
                return;

            cancel();
            if (mAsync)
            {
                final ExecutorService service = EXECUTOR_SERVICE;
                final Future future = service.submit(new BlurTask(new Callable<Bitmap>()
                {
                    @Override
                    public Bitmap call() throws Exception
                    {
                        return blurSource(getSource());
                    }
                }, this, target));

                if (mMapInvoker == null)
                    mMapInvoker = new ConcurrentHashMap<>();
                mMapInvoker.put(this, future);
            } else
            {
                target.onBlurred(blurSource(source));
            }
        }

        protected abstract S getSource();

        protected abstract Bitmap blurSource(S source);
    }

    private final class BlurTask extends FutureTask<Bitmap>
    {
        private final Invoker mInvoker;
        private final BlurTarget mTarget;

        public BlurTask(Callable<Bitmap> callable, Invoker invoker, BlurTarget target)
        {
            super(callable);
            mInvoker = invoker;
            mTarget = target;
        }

        @Override
        protected void done()
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
            }
        }
    }

    private final class BitmapInvoker extends SourceInvoker<Bitmap>
    {
        private final Bitmap mBitmap;

        public BitmapInvoker(Bitmap source)
        {
            super(source);
            mBitmap = source;
        }

        @Override
        protected Bitmap getSource()
        {
            return mBitmap;
        }

        @Override
        protected Bitmap blurSource(Bitmap source)
        {
            return getBlur().blur(source);
        }
    }

    private final class ViewInvoker extends SourceInvoker<View>
    {
        private final WeakReference<View> mView;

        public ViewInvoker(View source)
        {
            super(source);
            mView = new WeakReference<>(source);
        }

        @Override
        protected View getSource()
        {
            return mView == null ? null : mView.get();
        }

        @Override
        protected Bitmap blurSource(View source)
        {
            return getBlur().blur(source);
        }
    }

    private final class DrawableInvoker extends SourceInvoker<Drawable>
    {
        private final Drawable mDrawable;

        public DrawableInvoker(Drawable source)
        {
            super(source);
            mDrawable = source;
        }

        @Override
        protected Drawable getSource()
        {
            return mDrawable;
        }

        @Override
        protected Bitmap blurSource(Drawable source)
        {
            return getBlur().blur(source);
        }
    }
}
