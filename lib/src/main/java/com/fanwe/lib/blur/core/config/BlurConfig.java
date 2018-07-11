package com.fanwe.lib.blur.core.config;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public interface BlurConfig
{
    boolean init(int width, int height, int downSampling);

    int getWidth();

    int getHeight();

    int getDownSampling();

    Bitmap getBitmapOutput();

    Bitmap getBitmapInput();

    Canvas getCanvasInput();

    void recycle();
}
