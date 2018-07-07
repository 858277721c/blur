package com.fanwe.lib.blur.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;

public class CompatBlur implements Blur
{
    private final Context mContext;
    private Blur mBlur;

    public CompatBlur(Context context)
    {
        mContext = context.getApplicationContext();
    }

    private Blur getBlur()
    {
        if (mBlur == null)
        {
            if (Build.VERSION.SDK_INT >= 17)
            {
                Blur blur = new RenderScriptBlur(mContext);
                try
                {
                    blur.blur(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888));
                } catch (Exception e)
                {
                    blur.destroy();
                    blur = new FastBlur();
                } finally
                {
                    mBlur = blur;
                }
            } else
            {
                mBlur = new FastBlur();
            }
        }
        return mBlur;
    }

    @Override
    public void setRadius(int radius)
    {
        getBlur().setRadius(radius);
    }

    @Override
    public void setDownSampling(int downSampling)
    {
        getBlur().setDownSampling(downSampling);
    }

    @Override
    public void setColor(int color)
    {
        getBlur().setColor(color);
    }

    @Override
    public void setKeepDownSamplingSize(boolean keepDownSamplingSize)
    {
        getBlur().setKeepDownSamplingSize(keepDownSamplingSize);
    }

    @Override
    public void setDestroyAfterBlur(boolean destroyAfterBlur)
    {
        getBlur().setDestroyAfterBlur(destroyAfterBlur);
    }

    @Override
    public int getDownSampling()
    {
        return getBlur().getDownSampling();
    }

    @Override
    public Bitmap blur(View view)
    {
        return getBlur().blur(view);
    }

    @Override
    public Bitmap blur(Bitmap bitmap)
    {
        return getBlur().blur(bitmap);
    }

    @Override
    public void destroy()
    {
        if (mBlur != null)
        {
            mBlur.destroy();
            mBlur = null;
        }
    }
}
