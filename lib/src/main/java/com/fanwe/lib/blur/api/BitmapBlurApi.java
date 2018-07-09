package com.fanwe.lib.blur.api;

import android.graphics.Bitmap;

import com.fanwe.lib.blur.core.Blur;

class BitmapBlurApi extends BlurApi<Bitmap>
{
    private final Bitmap mBitmap;

    BitmapBlurApi(Bitmap source, Blur blur)
    {
        super(source, blur);
        mBitmap = source;
    }

    @Override
    public Bitmap blurSource()
    {
        return getBlur().blur(mBitmap);
    }
}
