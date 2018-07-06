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
    public void onBlurry(Bitmap bitmap)
    {
        final ImageView view = getView();
        if (view == null)
            return;

        if (bitmap != null)
            view.setImageBitmap(bitmap);
    }
}
