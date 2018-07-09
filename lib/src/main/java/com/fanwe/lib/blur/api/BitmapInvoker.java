package com.fanwe.lib.blur.api;

import android.graphics.Bitmap;

import com.fanwe.lib.blur.core.Blur;

class BitmapInvoker extends BlurInvoker<Bitmap>
{
    private final Bitmap mBitmap;

    BitmapInvoker(Bitmap source, Blur blur)
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
