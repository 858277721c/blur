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
        mBlur.setDestroyAfterBlur(true);
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
    public FBlur radius(int radius)
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
    public FBlur downSampling(int downSampling)
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
    public FBlur color(int color)
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
    public FBlur keepDownSamplingSize(boolean keepDownSamplingSize)
    {
        mBlur.setKeepDownSamplingSize(keepDownSamplingSize);
        return this;
    }

    /**
     * {@link Blur#setDestroyAfterBlur(boolean)}
     *
     * @param destroyAfterBlur
     * @return
     */
    public FBlur destroyAfterBlur(boolean destroyAfterBlur)
    {
        mBlur.setDestroyAfterBlur(destroyAfterBlur);
        return this;
    }

    /**
     * {@link Blur#destroy()}
     */
    public void destroy()
    {
        mBlur.destroy();
    }

    /**
     * 模糊view
     *
     * @param view
     * @return
     */
    public BlurInvoker blur(View view)
    {
        return new ViewInvoker(view, mBlur);
    }

    /**
     * 模糊drawable
     *
     * @param drawable
     * @return
     */
    public BlurInvoker blur(Drawable drawable)
    {
        return new DrawableInvoker(drawable, mBlur);
    }

    /**
     * 模糊bitmap
     *
     * @param bitmap
     * @return
     */
    public BlurInvoker blur(Bitmap bitmap)
    {
        return new BitmapInvoker(bitmap, mBlur);
    }
}
