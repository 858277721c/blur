package com.fanwe.lib.blur.api;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.fanwe.lib.blur.api.target.BackgroundTarget;
import com.fanwe.lib.blur.api.target.BlurTarget;
import com.fanwe.lib.blur.api.target.ImageViewTarget;
import com.fanwe.lib.blur.api.target.MainThreadTargetWrapper;
import com.fanwe.lib.blur.core.Blur;

abstract class BlurApi<S, R>
{
    private final Blur mBlur;

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
    public R radius(int radius)
    {
        mBlur.setRadius(radius);
        return (R) this;
    }

    /**
     * {@link Blur#setDownSampling(int)}
     *
     * @param sampling
     * @return
     */
    public R sampling(int sampling)
    {
        mBlur.setDownSampling(sampling);
        return (R) this;
    }

    /**
     * {@link Blur#setColorOverlay(int)}
     *
     * @param color
     * @return
     */
    public R color(int color)
    {
        mBlur.setColorOverlay(color);
        return (R) this;
    }

    /**
     * {@link Blur#setKeepDownSamplingSize(boolean)}
     *
     * @param keepSamplingSize
     * @return
     */
    public R keepSamplingSize(boolean keepSamplingSize)
    {
        mBlur.setKeepDownSamplingSize(keepSamplingSize);
        return (R) this;
    }

    /**
     * 返回模糊的Bitmap
     *
     * @return
     */
    protected abstract Bitmap blurImplemention();

    /**
     * 模糊后设置给ImageView
     *
     * @param imageView
     * @return
     */
    public R into(ImageView imageView)
    {
        if (imageView != null)
            into(new ImageViewTarget(imageView));
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
            into(new BackgroundTarget(view));
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
            final MainThreadTargetWrapper wrapper = new MainThreadTargetWrapper(target)
            {
                @Override
                protected void onBlurMainThread(Bitmap bitmap)
                {
                    super.onBlurMainThread(bitmap);
                    mBlur.destroy();
                }
            };
            wrapper.onBlur(blurImplemention());
        }
        return (R) this;
    }
}
