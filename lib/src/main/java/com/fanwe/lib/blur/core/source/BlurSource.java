package com.fanwe.lib.blur.core.source;

import android.graphics.Canvas;

public interface BlurSource
{
    int getWidth();

    int getHeight();

    void draw(Canvas canvas);
}
