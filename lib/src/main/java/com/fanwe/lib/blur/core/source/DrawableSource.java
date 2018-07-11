package com.fanwe.lib.blur.core.source;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

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
    public void draw(Canvas canvas)
    {
        getSource().draw(canvas);
    }
}
