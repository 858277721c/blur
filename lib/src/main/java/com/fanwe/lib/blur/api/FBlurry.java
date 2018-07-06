package com.fanwe.lib.blur.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.fanwe.lib.blur.Blur;
import com.fanwe.lib.blur.CompatBlur;

public class FBlurry
{
    private static FBlurry sInstance;
    private final Blur mBlur;

    private FBlurry(Context context)
    {
        mBlur = new CompatBlur(context);
    }

    /**
     * 返回单例对象
     *
     * @param context
     * @return
     */
    public static FBlurry getInstance(Context context)
    {
        if (sInstance == null)
        {
            synchronized (FBlurry.class)
            {
                if (sInstance == null)
                    sInstance = new FBlurry(context);
            }
        }
        return sInstance;
    }

    /**
     * 返回一个新对象
     *
     * @param context
     * @return
     */
    public static FBlurry newInstance(Context context)
    {
        return new FBlurry(context);
    }

    public BitmapBlurry from(Bitmap bitmap)
    {
        return new BitmapBlurry(bitmap, mBlur);
    }

    public ViewBlurry from(View view)
    {
        return new ViewBlurry(view, mBlur);
    }
}
