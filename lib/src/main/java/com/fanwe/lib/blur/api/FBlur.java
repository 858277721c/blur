package com.fanwe.lib.blur.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.fanwe.lib.blur.core.Blur;
import com.fanwe.lib.blur.core.BlurFactory;

public final class FBlur
{
    private final Blur mBlur;

    private FBlur(Context context)
    {
        mBlur = BlurFactory.create(context);
    }

    public static FBlur with(Context context)
    {
        return new FBlur(context);
    }

    /**
     * {@link Blur#setRadius(int)}
     *
     * @param radius
     * @return
     */
    public FBlur setRadius(int radius)
    {
        mBlur.setRadius(radius);
        return this;
    }

    /**
     * {@link Blur#setDownSampling(int)}
     *
     * @param downSampling
     * @return
     */
    public FBlur setDownSampling(int downSampling)
    {
        mBlur.setDownSampling(downSampling);
        return this;
    }

    /**
     * {@link Blur#setColor(int)}
     *
     * @param color
     * @return
     */
    public FBlur setColor(int color)
    {
        mBlur.setColor(color);
        return this;
    }

    /**
     * {@link Blur#setKeepDownSamplingSize(boolean)}
     *
     * @param keepDownSamplingSize
     * @return
     */
    public FBlur setKeepDownSamplingSize(boolean keepDownSamplingSize)
    {
        mBlur.setKeepDownSamplingSize(keepDownSamplingSize);
        return this;
    }

    /**
     * 模糊view
     *
     * @param view
     * @return
     */
    public BlurApi blur(View view)
    {
        return new ViewBlurApi(view, mBlur);
    }

    /**
     * 模糊drawable
     *
     * @param drawable
     * @return
     */
    public BlurApi blur(Drawable drawable)
    {
        return new DrawableBlurApi(drawable, mBlur);
    }

    /**
     * 模糊bitmap
     *
     * @param bitmap
     * @return
     */
    public BlurApi blur(Bitmap bitmap)
    {
        return new BitmapBlurApi(bitmap, mBlur);
    }
}
