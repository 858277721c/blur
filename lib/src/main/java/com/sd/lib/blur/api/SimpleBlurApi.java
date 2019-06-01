package com.sd.lib.blur.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.sd.lib.blur.api.target.BackgroundTarget;
import com.sd.lib.blur.api.target.ImageViewTarget;
import com.sd.lib.blur.api.target.MainThreadTargetWrapper;
import com.sd.lib.blur.core.Blur;
import com.sd.lib.blur.core.BlurFactory;
import com.sd.lib.blur.core.source.BlurSource;
import com.sd.lib.blur.core.source.BlurSourceFactory;

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
    public Invoker blur(Bitmap source)
    {
        if (source == null)
            return null;
        return blur(BlurSourceFactory.create(source));
    }

    @Override
    public Invoker blur(View source)
    {
        if (source == null)
            return null;
        return blur(BlurSourceFactory.create(source));
    }

    @Override
    public Invoker blur(Drawable source)
    {
        if (source == null)
            return null;
        return blur(BlurSourceFactory.create(source));
    }

    @Override
    public Invoker blur(BlurSource source)
    {
        if (source == null)
            return null;
        return new SourceInvoker(source);
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

    private final class SourceInvoker implements Invoker
    {
        private final BlurSource mSource;
        private boolean mAsync;

        public SourceInvoker(BlurSource source)
        {
            if (source == null)
                throw new IllegalArgumentException("source is null");
            mSource = source;
        }

        @Override
        public final Invoker async(boolean async)
        {
            mAsync = async;
            return this;
        }

        @Override
        public final Bitmap bitmap()
        {
            return getBlur().blur(mSource);
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
        public final Cancelable into(Target target)
        {
            if (target != null)
            {
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

        private void notifyTargetInternal(Target target)
        {
            cancel();
            if (mAsync)
            {
                final ExecutorService service = EXECUTOR_SERVICE;
                final Future future = service.submit(new BlurTask(new Callable<Bitmap>()
                {
                    @Override
                    public Bitmap call() throws Exception
                    {
                        return bitmap();
                    }
                }, this, target));

                if (mMapInvoker == null)
                    mMapInvoker = new ConcurrentHashMap<>();
                mMapInvoker.put(this, future);
            } else
            {
                target.onBlurred(bitmap());
            }
        }
    }

    private final class BlurTask extends FutureTask<Bitmap>
    {
        private final Invoker mInvoker;
        private final Target mTarget;

        public BlurTask(Callable<Bitmap> callable, Invoker invoker, Target target)
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
}
