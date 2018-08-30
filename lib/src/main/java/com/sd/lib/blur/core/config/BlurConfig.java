package com.sd.lib.blur.core.config;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public interface BlurConfig
{
    boolean init(int width, int height, int downSampling);

    int getWidth();

    int getHeight();

    Bitmap newBitmapOutput();

    Bitmap getBitmapInput();

    Canvas getCanvas();

    void recycle();
}
