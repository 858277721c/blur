package com.fanwe.lib.blur.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.fanwe.lib.blur.Blur;
import com.fanwe.lib.blur.CompatBlur;

public class FBlur
{
    private final Blur mBlur;

    private FBlur(Context context)
    {
        mBlur = new CompatBlur(context);
    }

    /**
     * 返回一个新对象
     *
     * @param context
     * @return
     */
    public static FBlur with(Context context)
    {
        return new FBlur(context);
    }

    public BitmapBlurApi from(Bitmap bitmap)
    {
        return new BitmapBlurApi(bitmap, mBlur);
    }

    public ViewBlurApi from(View view)
    {
        return new ViewBlurApi(view, mBlur);
    }
}
