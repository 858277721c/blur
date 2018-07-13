package com.fanwe.lib.blur.viewblur;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

public class ViewBackgroundBlur extends ViewDrawableBlur<View>
{
    public ViewBackgroundBlur(Context context)
    {
        super(context);
    }

    @Override
    protected Drawable getDrawable(View source)
    {
        return source.getBackground();
    }

    @Override
    protected void onDrawableBlurred(Drawable drawable, View source)
    {
        if (Build.VERSION.SDK_INT >= 16)
            source.setBackground(drawable);
        else
            source.setBackgroundDrawable(drawable);
    }
}
