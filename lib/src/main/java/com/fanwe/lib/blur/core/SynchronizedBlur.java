package com.fanwe.lib.blur.core;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.fanwe.lib.blur.utils.LockHelper;

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
    public synchronized void setRadius(final int radius)
    {
        LockHelper.lock(this);
        mBlur.setRadius(radius);
        LockHelper.unLock(this);
    }

    @Override
    public synchronized void setDownSampling(final int downSampling)
    {
        LockHelper.lock(this);
        mBlur.setDownSampling(downSampling);
        LockHelper.unLock(this);
    }

    @Override
    public synchronized void setColor(final int color)
    {
        LockHelper.lock(this);
        mBlur.setColor(color);
        LockHelper.unLock(this);
    }

    @Override
    public synchronized void setKeepDownSamplingSize(final boolean keepDownSamplingSize)
    {
        LockHelper.lock(this);
        mBlur.setKeepDownSamplingSize(keepDownSamplingSize);
        LockHelper.unLock(this);
    }

    @Override
    public synchronized void setDestroyAfterBlur(final boolean destroyAfterBlur)
    {
        LockHelper.lock(this);
        mBlur.setDestroyAfterBlur(destroyAfterBlur);
        LockHelper.unLock(this);
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
    public synchronized Bitmap blur(final View view)
    {
        LockHelper.lock(this);
        final Bitmap bitmapBlurred = mBlur.blur(view);
        LockHelper.unLock(this);
        return bitmapBlurred;
    }

    @Override
    public synchronized Bitmap blur(Drawable drawable)
    {
        LockHelper.lock(this);
        final Bitmap bitmapBlurred = mBlur.blur(drawable);
        LockHelper.unLock(this);
        return bitmapBlurred;
    }

    @Override
    public synchronized Bitmap blur(Bitmap bitmap)
    {
        LockHelper.lock(this);
        final Bitmap bitmapBlurred = mBlur.blur(bitmap);
        LockHelper.unLock(this);
        return bitmapBlurred;
    }

    @Override
    public synchronized void destroy()
    {
        LockHelper.lock(this);
        mBlur.destroy();
        LockHelper.unLock(this);
    }
}
