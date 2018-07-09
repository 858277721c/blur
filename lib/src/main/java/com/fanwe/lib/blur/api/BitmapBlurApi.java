package com.fanwe.lib.blur.api;

import android.graphics.Bitmap;

import com.fanwe.lib.blur.core.Blur;

class BitmapBlurApi extends BlurApi<Bitmap>
{
    private final Bitmap mBitmap;

    BitmapBlurApi(Bitmap source, boolean async, Blur blur)
    {
        super(source, async, blur);
        mBitmap = source;
    }

    @Override
    public Bitmap blurImplemention()
    {
        return getBlur().blur(mBitmap);
    }
}
