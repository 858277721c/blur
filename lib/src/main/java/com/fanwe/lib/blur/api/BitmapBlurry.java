package com.fanwe.lib.blur.api;

import android.graphics.Bitmap;

import com.fanwe.lib.blur.Blur;

public final class BitmapBlurry extends BaseBlurry<Bitmap, BitmapBlurry>
{
    private final Bitmap mBitmap;

    public BitmapBlurry(Bitmap source, Blur blur)
    {
        super(source, blur);
        mBitmap = source;
    }

    @Override
    public Bitmap blur()
    {
        return getBlur().blur(mBitmap);
    }
}
