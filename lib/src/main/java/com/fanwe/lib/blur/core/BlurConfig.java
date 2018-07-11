package com.fanwe.lib.blur.core;

import android.graphics.Bitmap;
import android.graphics.Canvas;

interface BlurConfig
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
