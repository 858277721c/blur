package com.fanwe.lib.blur.core.source;

import android.graphics.Bitmap;
import android.graphics.Canvas;

class BitmapSource extends BaseSource<Bitmap>
{
    public BitmapSource(Bitmap source)
    {
        super(source);
    }

    @Override
    public int getWidth()
    {
        return getSource().getWidth();
    }

    @Override
    public int getHeight()
    {
        return getSource().getHeight();
    }

    @Override
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(getSource(), 0, 0, null);
    }
}
