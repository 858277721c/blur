package com.fanwe.lib.blur.api;

import android.graphics.Bitmap;

import com.fanwe.lib.blur.core.Blur;

class BitmapInvoker extends BaseBlurInvoker<Bitmap>
{
    private final Bitmap mBitmap;

    BitmapInvoker(Bitmap source, Blur blur, BlurApi.Config config)
    {
        super(source, blur, config);
        mBitmap = source;
    }

    @Override
    public Bitmap blurSource()
    {
        return getBlur().blur(mBitmap);
    }
}
