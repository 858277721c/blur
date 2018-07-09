package com.fanwe.lib.blur.api;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.fanwe.lib.blur.api.target.BackgroundTarget;
import com.fanwe.lib.blur.api.target.BlurTarget;
import com.fanwe.lib.blur.api.target.ImageViewTarget;
import com.fanwe.lib.blur.api.target.MainThreadTargetWrapper;
import com.fanwe.lib.blur.core.Blur;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class BlurApi<S>
{
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    private final Blur mBlur;
    private boolean mAsync;
    private Future mFuture;

    BlurApi(S source, Blur blur)
    {
        if (blur == null)
            throw new NullPointerException("blur must not be null");

        mBlur = blur;
        blur.setDestroyAfterBlur(true);
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
    protected abstract Bitmap blurImplemention();

    /**
     * 设置是否在子线程执行
     * <br>
     * 如果在子线程执行的话，每次发起新的任务都会先取消旧的任务
     *
     * @param async
     * @return
     */
    public BlurApi async(boolean async)
    {
        mAsync = async;
        return this;
    }

    /**
     * 模糊后设置给ImageView
     *
     * @param imageView
     * @return
     */
    public BlurApi into(ImageView imageView)
    {
        if (imageView != null)
            into(new MainThreadTargetWrapper(new ImageViewTarget(imageView)));
        return this;
    }

    /**
     * 模糊后设置给view的背景
     *
     * @param view
     * @return
     */
    public BlurApi intoBackground(View view)
    {
        if (view != null)
            into(new MainThreadTargetWrapper(new BackgroundTarget(view)));
        return this;
    }

    /**
     * 模糊后设置给某个目标
     *
     * @param target
     * @return
     */
    public BlurApi into(BlurTarget target)
    {
        if (target != null)
        {
            if (mAsync)
            {
                cancelAsync();
                mFuture = EXECUTOR_SERVICE.submit(new BlurTask(target));
            } else
            {
                target.onBlur(blurImplemention());
            }
        }
        return this;
    }

    /**
     * 取消异步请求
     *
     * @return
     */
    public BlurApi cancelAsync()
    {
        if (mFuture != null)
        {
            mFuture.cancel(true);
            mFuture = null;
        }
        return this;
    }

    private final class BlurTask implements Runnable
    {
        private final BlurTarget mTarget;

        public BlurTask(BlurTarget target)
        {
            if (target == null)
                throw new NullPointerException("target is null");
            mTarget = target;
        }

        @Override
        public void run()
        {
            mTarget.onBlur(blurImplemention());
        }
    }
}
