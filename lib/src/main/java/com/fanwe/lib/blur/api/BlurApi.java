package com.fanwe.lib.blur.api;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.fanwe.lib.blur.Blur;
import com.fanwe.lib.blur.api.target.BackgroundTarget;
import com.fanwe.lib.blur.api.target.BlurryTarget;
import com.fanwe.lib.blur.api.target.ImageViewTarget;

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
     * 返回模糊的Bitmap
     *
     * @return
     */
    protected abstract Bitmap blur();

    /**
     * 模糊后设置给某个目标
     *
     * @param target
     * @return
     */
    public R into(BlurryTarget target)
    {
        if (target != null)
            target.onBlurry(blur());
        return (R) this;
    }

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
     * 释放资源，调用此方法后依旧可以使用此对象
     */
    public void destroy()
    {
        getBlur().destroy();
    }
}
