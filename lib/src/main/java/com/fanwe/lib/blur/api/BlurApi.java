package com.fanwe.lib.blur.api;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.fanwe.lib.blur.Blur;
import com.fanwe.lib.blur.api.target.BackgroundTarget;
import com.fanwe.lib.blur.api.target.BlurTarget;
import com.fanwe.lib.blur.api.target.ImageViewTarget;
import com.fanwe.lib.blur.api.target.MainThreadTargetWrapper;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

abstract class BlurApi<S, R>
{
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    private final Blur mBlur;
    private boolean mAsync;
    private Map<BlurTask, Future> mMapTask;

    public BlurApi(S source, Blur blur)
    {
        if (blur == null)
            throw new NullPointerException("blur must not be null");

        mBlur = blur;
    }

    protected final Blur getBlur()
    {
        return mBlur;
    }

    /**
     * {@link Blur#setRadius(int)}
     *
     * @param radius
     * @return
     */
    public R setRadius(int radius)
    {
        mBlur.setRadius(radius);
        return (R) this;
    }

    /**
     * {@link Blur#setDownSampling(int)}
     *
     * @param downSampling
     * @return
     */
    public R setDownSampling(int downSampling)
    {
        mBlur.setDownSampling(downSampling);
        return (R) this;
    }

    /**
     * {@link Blur#setColorOverlay(int)}
     *
     * @param colorOverlay
     * @return
     */
    public R setColorOverlay(int colorOverlay)
    {
        mBlur.setColorOverlay(colorOverlay);
        return (R) this;
    }

    /**
     * {@link Blur#setKeepDownSamplingSize(boolean)}
     *
     * @param keepDownSamplingSize
     * @return
     */
    public R setKeepDownSamplingSize(boolean keepDownSamplingSize)
    {
        mBlur.setKeepDownSamplingSize(keepDownSamplingSize);
        return (R) this;
    }

    /**
     * 设置是否在子线程执行
     *
     * @param async
     * @return
     */
    public R setAsync(boolean async)
    {
        mAsync = async;
        return (R) this;
    }

    /**
     * 返回模糊的Bitmap
     *
     * @return
     */
    protected abstract Bitmap blur();

    /**
     * 模糊后设置给ImageView
     *
     * @param imageView
     * @return
     */
    public R into(ImageView imageView)
    {
        if (imageView != null)
            into(new MainThreadTargetWrapper(new ImageViewTarget(imageView)));
        return (R) this;
    }

    /**
     * 模糊后设置给view的背景
     *
     * @param view
     * @return
     */
    public R intoBackground(View view)
    {
        if (view != null)
            into(new MainThreadTargetWrapper(new BackgroundTarget(view)));
        return (R) this;
    }

    /**
     * 模糊后设置给某个目标
     *
     * @param target
     * @return
     */
    public R into(BlurTarget target)
    {
        if (target != null)
        {
            if (mAsync)
            {
                synchronized (BlurApi.this)
                {
                    final BlurTask task = new BlurTask(target);
                    final Future future = EXECUTOR_SERVICE.submit(task);

                    if (mMapTask == null)
                        mMapTask = new WeakHashMap<>();
                    mMapTask.put(task, future);
                }
            } else
            {
                target.onBlur(blur());
            }
        }
        return (R) this;
    }

    /**
     * 取消异步请求
     *
     * @return
     */
    public R cancelAsync()
    {
        synchronized (BlurApi.this)
        {
            if (mMapTask != null)
            {
                for (Map.Entry<BlurTask, Future> item : mMapTask.entrySet())
                {
                    item.getValue().cancel(true);
                }
                mMapTask.clear();
            }
        }
        return (R) this;
    }

    /**
     * 释放资源，调用此方法后依旧可以使用此对象
     */
    public void destroy()
    {
        cancelAsync();
        mBlur.destroy();
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
            mTarget.onBlur(blur());

            synchronized (BlurApi.this)
            {
                if (mMapTask != null)
                    mMapTask.remove(this);
            }
        }
    }
}
