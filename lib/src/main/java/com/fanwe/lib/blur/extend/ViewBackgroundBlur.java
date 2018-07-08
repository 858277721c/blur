package com.fanwe.lib.blur.extend;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.fanwe.lib.blur.core.Blur;

public class ViewBackgroundBlur extends ViewBlur<View>
{
    public ViewBackgroundBlur(Blur blur)
    {
        super(blur);
    }

    @Override
    protected Drawable getViewDrawable(View view)
    {
        return view.getBackground();
    }

    @Override
    protected void onBlur(BlurredBitmapDrawable drawable, View view)
    {
        if (Build.VERSION.SDK_INT >= 16)
            view.setBackground(drawable);
        else
            view.setBackgroundDrawable(drawable);
    }
}
