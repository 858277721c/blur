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

    public SimpleBlurApi(Context context)
    {
        final Blur blur = BlurFactory.create(context);
        blur.setDestroyAfterBlur(true);

        mBlur = BlurFactory.synchronizedBlur(blur);
    }

    private Blur getBlur()
    {
        return mBlur;
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
        final BlurSource blurSource = BlurSourceFactory.create(source);
        return blur(blurSource);
    }

    @Override
    public Invoker blur(View source)
    {
        final BlurSource blurSource = BlurSourceFactory.create(source);
        return blur(blurSource);
    }

    @Override
    public Invoker blur(Drawable source)
    {
        final BlurSource blurSource = BlurSourceFactory.create(source);
        return blur(blurSource);
    }

    @Override
    public Invoker blur(BlurSource source)
    {
        return new InternalInvoker(source);
    }

    @Override
    public BlurApi destroy()
    {
        if (mMapInvoker != null)
        {
            for (Map.Entry<AsyncInvoker, Future> item : mMapInvoker.entrySet())
            {
                item.getValue().cancel(true);
            }
            mMapInvoker.clear();
        }

        getBlur().destroy();
        return this;
    }

    private abstract class SourceHolder
    {
        protected final BlurSource mSource;

        public SourceHolder(BlurSource source)
        {
            if (source == null)
                throw new IllegalArgumentException("source is null");
            mSource = source;
        }
    }

    private final class InternalInvoker extends SourceHolder implements Invoker
    {
        public InternalInvoker(BlurSource source)
        {
            super(source);
        }

        @Override
        public Bitmap bitmap()
        {
            return getBlur().blur(mSource);
        }

        @Override
        public AsyncInvoker async()
        {
            if (mMapInvoker == null)
                mMapInvoker = new ConcurrentHashMap<>();

            return new InternalAsyncInvoker(mSource);
        }
    }

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    private Map<AsyncInvoker, Future> mMapInvoker;

    private final class InternalAsyncInvoker extends SourceHolder implements AsyncInvoker
    {
        public InternalAsyncInvoker(BlurSource source)
        {
            super(source);
        }

        @Override
        public final Cancelable into(ImageView imageView)
        {
            into(new ImageViewTarget(imageView));
            return this;
        }

        @Override
        public final Cancelable intoBackground(View view)
        {
            into(new BackgroundTarget(view));
            return this;
        }

        @Override
        public final Cancelable into(Target target)
        {
            notifyTargetInternal(new MainThreadTargetWrapper(target));
            return this;
        }

        @Override
        public final void cancel()
        {
            final Future future = mMapInvoker.remove(this);
            if (future != null)
                future.cancel(true);
        }

        private void notifyTargetInternal(Target target)
        {
            cancel();

            final Future future = EXECUTOR_SERVICE.submit(new BlurTask(new Callable<Bitmap>()
            {
                @Override
                public Bitmap call() throws Exception
                {
                    return getBlur().blur(mSource);
                }
            }, this, target));

            mMapInvoker.put(this, future);
        }
    }

    private final class BlurTask extends FutureTask<Bitmap>
    {
        private final AsyncInvoker mInvoker;
        private final Target mTarget;

        public BlurTask(Callable<Bitmap> callable, AsyncInvoker invoker, Target target)
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
