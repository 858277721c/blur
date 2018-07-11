package com.fanwe.lib.blur.core;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.fanwe.lib.blur.core.source.BlurSource;

class SynchronizedBlur implements Blur
{
    private final Blur mBlur;

    public SynchronizedBlur(Blur blur)
    {
        if (blur == null)
            throw new NullPointerException("blur is null");
        mBlur = blur;
    }

    @Override
    public synchronized void setRadius(int radius)
    {
        mBlur.setRadius(radius);
    }

    @Override
    public synchronized void setDownSampling(int downSampling)
    {
        mBlur.setDownSampling(downSampling);
    }

    @Override
    public synchronized void setColor(int color)
    {
        mBlur.setColor(color);
    }

    @Override
    public synchronized void setKeepDownSamplingSize(boolean keepDownSamplingSize)
    {
        mBlur.setKeepDownSamplingSize(keepDownSamplingSize);
    }

    @Override
    public synchronized void setDestroyAfterBlur(boolean destroyAfterBlur)
    {
        mBlur.setDestroyAfterBlur(destroyAfterBlur);
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

    @Override
    public boolean isKeepDownSamplingSize()
    {
        return mBlur.isKeepDownSamplingSize();
    }

    @Override
    public boolean isDestroyAfterBlur()
    {
        return mBlur.isDestroyAfterBlur();
    }

    @Override
    public synchronized Bitmap blur(Bitmap bitmap)
    {
        return mBlur.blur(bitmap);
    }

    @Override
    public synchronized Bitmap blur(View view)
    {
        return mBlur.blur(view);
    }

    @Override
    public synchronized Bitmap blur(Drawable drawable)
    {
        return mBlur.blur(drawable);
    }

    @Override
    public synchronized Bitmap blur(BlurSource source)
    {
        return mBlur.blur(source);
    }

    @Override
    public synchronized void destroy()
    {
        mBlur.destroy();
    }
}
