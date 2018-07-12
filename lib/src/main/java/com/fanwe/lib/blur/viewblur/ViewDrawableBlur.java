package com.fanwe.lib.blur.viewblur;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.fanwe.lib.blur.api.BlurApi;
import com.fanwe.lib.blur.api.target.BlurTarget;

abstract class ViewDrawableBlur<T extends View> extends ViewBlur<T>
{
    private Drawable mDrawable;

    public ViewDrawableBlur(Context context)
    {
        super(context);
    }

    @Override
    protected void onUpdate(T target, BlurApi blurApi)
    {
        final Drawable drawable = getDrawable(target);

        if (mDrawable != drawable)
        {
            mDrawable = drawable;
            if (drawable != null)
            {
                if (!(drawable instanceof BlurredBitmapDrawable))
                    blurApi.blur(drawable).into(mBlurTarget);
            }
        }
    }

    private final BlurTarget mBlurTarget = new BlurTarget()
    {
        @Override
        public void onBlurred(Bitmap bitmap)
        {
            if (bitmap == null)
                return;
            final T target = getTarget();
            if (target == null)
                return;

            mDrawable = new BlurredBitmapDrawable(target.getContext().getResources(), bitmap);
            onDrawableBlurred(mDrawable, target);
        }
    };

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
