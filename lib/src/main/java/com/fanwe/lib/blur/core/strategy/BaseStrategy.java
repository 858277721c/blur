package com.fanwe.lib.blur.core.strategy;

import android.graphics.Bitmap;

abstract class BaseStrategy implements BlurStrategy
{
    @Override
    public boolean test()
    {
        try
        {
            final Bitmap bitmapInput = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            final Bitmap bitmapOutput = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            blur(1, bitmapInput, bitmapOutput);
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }
}
