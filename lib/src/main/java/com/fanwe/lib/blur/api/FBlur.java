package com.fanwe.lib.blur.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

public class FBlur
{
    private final Context mContext;

    private FBlur(Context context)
    {
        if (context == null)
            throw new NullPointerException("context must not be null");
        mContext = context.getApplicationContext();
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
        return new BitmapBlurApi(bitmap, mContext);
    }

    public ViewBlurApi from(View view)
    {
        return new ViewBlurApi(view, mContext);
    }
}
