package com.fanwe.lib.blur.api;

import android.content.Context;
import android.graphics.Bitmap;

public final class BitmapBlurApi extends BlurApi<Bitmap, BitmapBlurApi>
{
    private final Bitmap mBitmap;

    BitmapBlurApi(Bitmap source, Context context)
    {
        super(source, context);
        mBitmap = source;
    }

    @Override
    public Bitmap blurImplemention()
    {
        return getBlur().blur(mBitmap);
    }
}
