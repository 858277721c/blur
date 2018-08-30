package com.sd.lib.blur.core.source;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

public final class BlurSourceFactory
{
    private BlurSourceFactory()
    {
    }

    public static BlurSource create(Bitmap bitmap)
    {
        return new BitmapSource(bitmap);
    }

    public static BlurSource create(View view)
    {
        return new ViewSource(view);
    }

    public static BlurSource create(Drawable drawable)
    {
        if (drawable instanceof BitmapDrawable)
            return create(((BitmapDrawable) drawable).getBitmap());
        else
            return new DrawableSource(drawable);
    }
}
