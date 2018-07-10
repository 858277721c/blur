package com.fanwe.lib.blur.api;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.fanwe.lib.blur.core.Blur;

class DrawableInvoker extends BaseBlurInvoker<Drawable>
{
    private final Drawable mDrawable;

    DrawableInvoker(Drawable source, Blur blur, BlurApi.Config config)
    {
        super(source, blur, config);
        mDrawable = source;
    }

    @Override
    protected Bitmap blurSource()
    {
        return getBlur().blur(mDrawable);
    }
}
