package com.fanwe.lib.blur.viewblur;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

abstract class ViewDrawableBlur<S extends View> extends BaseViewBlur<S>
{
    private Drawable mDrawable;

    public ViewDrawableBlur(Context context)
    {
        super(context);
    }

    @Override
    protected final void onUpdate(S source)
    {
        final Drawable drawable = getDrawable(source);
        if (mDrawable != drawable)
        {
            mDrawable = drawable;

            if (drawable != null && !(drawable instanceof BlurredBitmapDrawable))
            {
                final Bitmap bitmapBlurred = getBlurApi().bitmap(drawable);
                if (bitmapBlurred == null)
                    return;

                mDrawable = new BlurredBitmapDrawable(source.getContext().getResources(), bitmapBlurred);
                onDrawableBlurred(mDrawable, source);
            }
        }
    }

    protected abstract Drawable getDrawable(S source);

    protected abstract void onDrawableBlurred(Drawable drawable, S source);

    private static final class BlurredBitmapDrawable extends BitmapDrawable
    {
        public BlurredBitmapDrawable(Resources res, Bitmap bitmap)
        {
            super(res, bitmap);
        }
    }
}
