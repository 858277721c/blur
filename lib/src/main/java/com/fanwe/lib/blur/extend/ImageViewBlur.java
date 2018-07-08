package com.fanwe.lib.blur.extend;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.fanwe.lib.blur.core.Blur;

public class ImageViewBlur extends ViewBlur<ImageView>
{
    public ImageViewBlur(Blur blur)
    {
        super(blur);
    }

    @Override
    protected Drawable getViewDrawable(ImageView view)
    {
        return view.getDrawable();
    }

    @Override
    protected void onBlur(BlurredBitmapDrawable drawable, ImageView view)
    {
        view.setImageDrawable(drawable);
    }
}
