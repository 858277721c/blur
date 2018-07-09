package com.fanwe.lib.blur.api;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.fanwe.lib.blur.core.Blur;

class DrawableInvoker extends BlurInvoker<Drawable>
{
    private final Drawable mDrawable;

    DrawableInvoker(Drawable source, Blur blur)
    {
        super(source, blur);
        mDrawable = source;
    }

    @Override
    protected Bitmap blurSource()
    {
        return getBlur().blur(mDrawable);
    }
}
