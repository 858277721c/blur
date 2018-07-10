package com.fanwe.lib.blur.api;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.fanwe.lib.blur.api.target.BackgroundTarget;
import com.fanwe.lib.blur.api.target.ImageViewTarget;
import com.fanwe.lib.blur.api.target.MainThreadTargetWrapper;
import com.fanwe.lib.blur.core.Blur;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

abstract class BaseBlurInvoker<S> implements BlurInvoker
{
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    private final Blur mBlur;
    private final BlurApi.Config mConfig;

    private boolean mAsync;
    private Future mFuture;

    BaseBlurInvoker(S source, Blur blur, BlurApi.Config config)
    {
        if (blur == null || config == null)
            throw new NullPointerException("params must not be null");
        mBlur = blur;
        mConfig = config;
    }

    protected final Blur getBlur()
    {
        return mBlur;
    }

    /**
     * 返回模糊的Bitmap
     *
     * @return
     */
    protected abstract Bitmap blurSource();

    @Override
    public BlurInvoker async(boolean async)
    {
        mAsync = async;
        return this;
    }

    @Override
    public BlurInvoker into(ImageView imageView)
    {
        if (imageView != null)
            into(new ImageViewTarget(imageView));
        return this;
    }

    @Override
    public BlurInvoker intoBackground(View view)
    {
        if (view != null)
            into(new BackgroundTarget(view));
        return this;
    }

    @Override
    public BlurInvoker into(Target target)
    {
        if (target != null)
        {
            target = new MainThreadTargetWrapper(target);

            cancelAsync();
            if (mAsync)
            {
                mFuture = EXECUTOR_SERVICE.submit(new BlurTask(target));
                Log.i(getClass().getName(), this + " submit");
            } else
            {
                target.onBlurred(blurSource());
            }
        }
        return this;
    }

    @Override
    public BlurInvoker cancelAsync()
    {
        if (mFuture != null)
        {
            Log.i(getClass().getName(), this + " try cancelled");
            if (mFuture.cancel(true))
            {
                Log.e(getClass().getName(), this + " cancelled");
            }
        }
        return this;
    }

    private final class BlurTask implements Runnable
    {
        private final Target mTarget;

        public BlurTask(Target target)
        {
            if (target == null)
                throw new NullPointerException("target is null");
            mTarget = target;
        }

        @Override
        public void run()
        {
            synchronized (mBlur)
            {
                try
                {
                    mTarget.onBlurred(blurSource());
                } finally
                {
                    if (mConfig.isDestroyAfterBlur())
                        mBlur.destroy();
                }
            }
        }
    }
}
