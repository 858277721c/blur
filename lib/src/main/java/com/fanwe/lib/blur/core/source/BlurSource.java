package com.fanwe.lib.blur.core.source;

import android.graphics.Canvas;
import android.os.Handler;

public interface BlurSource
{
    int getWidth();

    int getHeight();

    void draw(Canvas canvas, Handler handler);
}
