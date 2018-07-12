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
    protected Drawable getDrawable(View target)
    {
        return target.getBackground();
    }

    @Override
    protected void onDrawableBlurred(Drawable drawable, View target)
    {
        if (Build.VERSION.SDK_INT >= 16)
            target.setBackground(drawable);
        else
            target.setBackgroundDrawable(drawable);
    }
}
