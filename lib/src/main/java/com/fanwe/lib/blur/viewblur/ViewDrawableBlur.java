package com.fanwe.lib.blur.viewblur;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.fanwe.lib.blur.api.target.BlurTarget;

abstract class ViewDrawableBlur<V extends View> extends BaseViewBlur<V>
{
    private boolean mBlurAsync;
    private Drawable mDrawable;

    public ViewDrawableBlur(Context context)
    {
        super(context);
    }

    @Override
    public final ViewBlur<V> setBlurAsync(boolean async)
    {
        mBlurAsync = async;
        return this;
    }

    @Override
    protected final void onUpdate(V source, V target)
    {
        final Drawable drawable = getDrawable(source);
        if (mDrawable != drawable)
        {
            mDrawable = drawable;

            if (drawable != null && !(drawable instanceof BlurredBitmapDrawable))
                getBlurApi().blur(drawable).async(mBlurAsync).into(mBlurTarget);
        }
    }

    private final BlurTarget mBlurTarget = new BlurTarget()
    {
        @Override
        public void onBlurred(Bitmap bitmap)
        {
            if (bitmap == null)
                return;
            final V target = getTarget();
            if (target == null)
                return;

            mDrawable = new BlurredBitmapDrawable(target.getContext().getResources(), bitmap);
            onDrawableBlurred(mDrawable, target);
        }
    };

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
