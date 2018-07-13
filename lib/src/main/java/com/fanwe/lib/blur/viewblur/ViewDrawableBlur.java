package com.fanwe.lib.blur.viewblur;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

abstract class ViewDrawableBlur<T extends View> extends ViewBlur<T>
{
    private Drawable mDrawable;

    public ViewDrawableBlur(Context context)
    {
        super(context);
    }

    @Override
    protected final void onUpdate(T target)
    {
        final Drawable drawable = getDrawable(target);
        if (mDrawable != drawable)
        {
            mDrawable = drawable;

            if (drawable != null && !(drawable instanceof BlurredBitmapDrawable))
            {
                final Bitmap bitmapBlurred = getBlurApi().bitmap(drawable);
                if (bitmapBlurred == null)
                    return;

                mDrawable = new BlurredBitmapDrawable(target.getContext().getResources(), bitmapBlurred);
                onDrawableBlurred(mDrawable, target);
            }
        }
    }

    protected abstract Drawable getDrawable(T target);

    protected abstract void onDrawableBlurred(Drawable drawable, T target);

    private static final class BlurredBitmapDrawable extends BitmapDrawable
    {
        public BlurredBitmapDrawable(Resources res, Bitmap bitmap)
        {
            super(res, bitmap);
        }
    }
}
