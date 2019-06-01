package com.sd.lib.blur.viewblur;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.sd.lib.blur.api.BlurApi;

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
    protected final void onUpdate(BlurApi blurApi, V source, V target)
    {
        final Drawable drawable = getSourceDrawable(source);
        if (mDrawable == drawable)
            return;

        mDrawable = drawable;

        if (drawable != null && !(drawable instanceof BlurredBitmapDrawable))
        {
            if (mBlurAsync)
            {
                blurApi.blur(drawable).async().into(mBlurTarget);
            } else
            {
                applyBlur(blurApi.blur(drawable).bitmap());
            }
        }
    }

    private final BlurApi.Target mBlurTarget = new BlurApi.Target()
    {
        @Override
        public void onBlurred(Bitmap bitmap)
        {
            applyBlur(bitmap);
        }
    };

    private void applyBlur(Bitmap bitmap)
    {
        if (bitmap == null)
            return;

        final V target = getTarget();
        if (target == null)
            return;

        mDrawable = new BlurredBitmapDrawable(target.getContext().getResources(), bitmap);
        onDrawableBlurred(mDrawable, target);
    }

    /**
     * 从数据源拿到Drawable
     *
     * @param source
     * @return
     */
    protected abstract Drawable getSourceDrawable(V source);

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
