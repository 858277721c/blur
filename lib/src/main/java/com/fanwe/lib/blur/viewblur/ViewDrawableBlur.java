package com.fanwe.lib.blur.viewblur;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

abstract class ViewDrawableBlur<V extends View> extends BaseViewBlur<V>
{
    private Drawable mDrawable;

    public ViewDrawableBlur(Context context)
    {
        super(context);
    }

    @Override
    protected final void onUpdate(V source)
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
    protected void onTargetChanged(V oldTarget, V newTarget)
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
    protected abstract Drawable getDrawable(V source);

    /**
     * 把模糊后的Drawable设置给要模糊的目标
     *
     * @param drawable
     * @param target
     */
    protected abstract void onDrawableBlurred(Drawable drawable, V target);

    private static final class BlurredBitmapDrawable extends BitmapDrawable
    {
        public BlurredBitmapDrawable(Resources res, Bitmap bitmap)
        {
            super(res, bitmap);
        }
    }
}
