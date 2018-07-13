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

    @Override
    protected void onTargetChanged(S oldTarget, S newTarget)
    {
        if (newTarget != null && mDrawable instanceof BlurredBitmapDrawable)
            onDrawableBlurred(mDrawable, newTarget);
    }

    /**
     * 从数据源拿到Drawable
     *
     * @param source
     * @return
     */
    protected abstract Drawable getDrawable(S source);

    /**
     * 把模糊后的Drawable设置给要模糊的目标
     *
     * @param drawable
     * @param target
     */
    protected abstract void onDrawableBlurred(Drawable drawable, S target);

    private static final class BlurredBitmapDrawable extends BitmapDrawable
    {
        public BlurredBitmapDrawable(Resources res, Bitmap bitmap)
        {
            super(res, bitmap);
        }
    }
}
