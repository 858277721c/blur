package com.fanwe.lib.blur.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

class CompatBlur implements Blur
{
    private final Blur mBlur;

    public CompatBlur(Context context)
    {
        context = context.getApplicationContext();

        if (Build.VERSION.SDK_INT >= 17)
        {
            Blur blur = new RenderScriptBlur(context);
            try
            {
                blur.blur(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888));
            } catch (Exception e)
            {
                blur.destroy();
                blur = new FastBlur(context);
            } finally
            {
                mBlur = blur;
            }
        } else
        {
            mBlur = new FastBlur(context);
        }
    }

    @Override
    public void setRadius(int radius)
    {
        mBlur.setRadius(radius);
    }

    @Override
    public void setDownSampling(int downSampling)
    {
        mBlur.setDownSampling(downSampling);
    }

    @Override
    public void setColor(int color)
    {
        mBlur.setColor(color);
    }

    @Override
    public void setKeepDownSamplingSize(boolean keepDownSamplingSize)
    {
        mBlur.setKeepDownSamplingSize(keepDownSamplingSize);
    }

    @Override
    public void setDestroyAfterBlur(boolean destroyAfterBlur)
    {
        mBlur.setDestroyAfterBlur(destroyAfterBlur);
    }

    @Override
    public int getRadius()
    {
        return mBlur.getRadius();
    }

    @Override
    public int getColor()
    {
        return mBlur.getColor();
    }

    @Override
    public int getDownSampling()
    {
        return mBlur.getDownSampling();
    }

    @Override
    public Bitmap blur(View view)
    {
        return mBlur.blur(view);
    }

    @Override
    public Bitmap blur(Drawable drawable)
    {
        return mBlur.blur(drawable);
    }

    @Override
    public Bitmap blur(Bitmap bitmap)
    {
        return mBlur.blur(bitmap);
    }

    @Override
    public void destroy()
    {
        mBlur.destroy();
    }
}
