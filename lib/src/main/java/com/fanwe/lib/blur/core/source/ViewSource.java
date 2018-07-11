package com.fanwe.lib.blur.core.source;

import android.graphics.Canvas;
import android.view.View;

class ViewSource extends BaseSource<View>
{
    public ViewSource(View source)
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
        getSource().draw(canvas);
    }
}
