package com.fanwe.lib.blur.api.target;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.View;

public class BackgroundTarget extends ViewTarget<View>
{
    public BackgroundTarget(View view)
    {
        super(view);
    }

    @Override
    public void onBlur(Bitmap bitmap)
    {
        final View view = getView();
        if (view == null)
            return;

        if (bitmap != null)
        {
            final BitmapDrawable drawable = new BitmapDrawable(null, bitmap);
            if (Build.VERSION.SDK_INT >= 16)
                view.setBackground(drawable);
            else
                view.setBackgroundDrawable(drawable);
        }
    }
}
