package com.sd.lib.blur.core.strategy;

import android.graphics.Bitmap;

public interface BlurStrategy
{
    void blur(int radius, Bitmap bitmapInput, Bitmap bitmapOutput);

    boolean test();

    void destroy();
}
