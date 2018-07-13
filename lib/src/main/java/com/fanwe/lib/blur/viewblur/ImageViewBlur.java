package com.fanwe.lib.blur.viewblur;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ImageViewBlur extends ViewDrawableBlur<ImageView>
{
    public ImageViewBlur(Context context)
    {
        super(context);
    }

    @Override
    protected Drawable getDrawable(ImageView source)
    {
        return source.getDrawable();
    }

    @Override
    protected void onDrawableBlurred(Drawable drawable, ImageView target)
    {
        target.setImageDrawable(drawable);
    }
}
