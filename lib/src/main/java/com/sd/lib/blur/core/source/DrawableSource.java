package com.sd.lib.blur.core.source;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;

class DrawableSource extends BaseSource<Drawable>
{
    public DrawableSource(Drawable source)
    {
        super(source);
    }

    @Override
    public int getWidth()
    {
        return getSource().getIntrinsicWidth();
    }

    @Override
    public int getHeight()
    {
        return getSource().getIntrinsicHeight();
    }

    @Override
    public void draw(Canvas canvas, Handler handler)
    {
        getSource().draw(canvas);
    }
}
