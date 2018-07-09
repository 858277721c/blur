package com.fanwe.lib.blur.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.fanwe.lib.blur.core.Blur;
import com.fanwe.lib.blur.core.BlurFactory;

class SimpleBlurApi implements BlurApi, BlurApiConfig
{
    private final Blur mBlur;

    SimpleBlurApi(Context context)
    {
        mBlur = BlurFactory.create(context);
        mBlur.setDestroyAfterBlur(true);
    }

    @Override
    public BlurApi radius(int radius)
    {
        mBlur.setRadius(radius);
        return this;
    }

    @Override
    public BlurApi downSampling(int downSampling)
    {
        mBlur.setDownSampling(downSampling);
        return this;
    }

    @Override
    public BlurApi color(int color)
    {
        mBlur.setColor(color);
        return this;
    }

    @Override
    public BlurApi keepDownSamplingSize(boolean keepDownSamplingSize)
    {
        mBlur.setKeepDownSamplingSize(keepDownSamplingSize);
        return this;
    }

    @Override
    public BlurApi destroyAfterBlur(boolean destroyAfterBlur)
    {
        mBlur.setDestroyAfterBlur(destroyAfterBlur);
        return this;
    }

    @Override
    public BlurApiConfig config()
    {
        return this;
    }

    @Override
    public BlurApi destroy()
    {
        mBlur.destroy();
        return this;
    }

    @Override
    public BlurInvoker blur(View view)
    {
        return new ViewInvoker(view, mBlur);
    }

    @Override
    public BlurInvoker blur(Drawable drawable)
    {
        return new DrawableInvoker(drawable, mBlur);
    }

    @Override
    public BlurInvoker blur(Bitmap bitmap)
    {
        return new BitmapInvoker(bitmap, mBlur);
    }

    @Override
    public int getRadius()
    {
        return mBlur.getRadius();
    }

    @Override
    public int getDownSampling()
    {
        return mBlur.getDownSampling();
    }

    @Override
    public int getColor()
    {
        return mBlur.getColor();
    }
}
