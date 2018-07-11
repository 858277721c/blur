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
        LockHelper.synchronizedSession(this, new Runnable()
        {
            @Override
            public void run()
            {
                mBlur.setRadius(radius);
            }
        });
    }

    @Override
    public synchronized void setDownSampling(final int downSampling)
    {
        LockHelper.synchronizedSession(this, new Runnable()
        {
            @Override
            public void run()
            {
                mBlur.setDownSampling(downSampling);
            }
        });
    }

    @Override
    public synchronized void setColor(final int color)
    {
        LockHelper.synchronizedSession(this, new Runnable()
        {
            @Override
            public void run()
            {
                mBlur.setColor(color);
            }
        });
    }

    @Override
    public synchronized void setKeepDownSamplingSize(final boolean keepDownSamplingSize)
    {
        LockHelper.synchronizedSession(this, new Runnable()
        {
            @Override
            public void run()
            {
                mBlur.setKeepDownSamplingSize(keepDownSamplingSize);
            }
        });
    }

    @Override
    public synchronized void setDestroyAfterBlur(final boolean destroyAfterBlur)
    {
        LockHelper.synchronizedSession(this, new Runnable()
        {
            @Override
            public void run()
            {
                mBlur.setDestroyAfterBlur(destroyAfterBlur);
            }
        });
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
        LockHelper.synchronizedSession(this, new Runnable()
        {
            @Override
            public void run()
            {
                mBlur.destroy();
            }
        });
    }
}
