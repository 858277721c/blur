package com.fanwe.lib.blur.api.target;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class ImageViewTarget extends ViewTarget<ImageView>
{
    public ImageViewTarget(ImageView view)
    {
        super(view);
    }

    @Override
    protected void onBlur(Bitmap bitmap, ImageView view)
    {
        if (bitmap == null || view == null)
            return;

        view.setImageBitmap(bitmap);
    }
}
