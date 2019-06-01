package com.sd.lib.blur.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.sd.lib.blur.api.BlurApi;
import com.sd.lib.blur.api.BlurApiFactory;

public abstract class FBlurImageViewProxy implements BlurView
{
    private final Context mContext;

    private BlurApi mBlurApi;
    private boolean mBlurAsync;

    private Drawable mOriginalDrawable;
    private boolean mIsAttachedToWindow;

    public FBlurImageViewProxy(ImageView imageView, AttributeSet attrs)
    {
        mContext = imageView.getContext();

        final BlurViewAttrs viewAttrs = BlurViewAttrs.parse(mContext, attrs);
        setBlurRadius(viewAttrs.getBlurRadius());
        setBlurDownSampling(viewAttrs.getBlurDownSampling());
        setBlurColor(viewAttrs.getBlurColor());
        setBlurAsync(viewAttrs.isBlurAsync());

        if (imageView.getBackground() == null)
            imageView.setBackgroundColor(viewAttrs.getBlurColor());

        mOriginalDrawable = imageView.getDrawable();
        imageView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener()
        {
            @Override
            public void onViewAttachedToWindow(View v)
            {
                mIsAttachedToWindow = true;
                blur();
            }

            @Override
            public void onViewDetachedFromWindow(View v)
            {
                mIsAttachedToWindow = false;
                if (mBlurApi != null)
                    mBlurApi.destroy();
            }
        });
    }

    private BlurApi getBlurApi()
    {
        if (mBlurApi == null)
        {
            mBlurApi = BlurApiFactory.create(mContext);
            mBlurApi.setDestroyAfterBlur(false);
        }
        return mBlurApi;
    }

    @Override
    public final void setBlurRadius(int radius)
    {
        getBlurApi().setRadius(radius);
    }

    @Override
    public final void setBlurDownSampling(int downSampling)
    {
        getBlurApi().setDownSampling(downSampling);
    }

    @Override
    public final void setBlurColor(int color)
    {
        getBlurApi().setColor(color);
    }

    @Override
    public final void setBlurAsync(boolean async)
    {
        mBlurAsync = async;
    }

    @Override
    public final void blur()
    {
        setImageDrawableOverride(mOriginalDrawable);
    }

    /**
     * 覆盖ImageView的此方法
     *
     * @param resId
     */
    public void setImageResourceOverride(int resId)
    {
        final Drawable drawable = mContext.getResources().getDrawable(resId);
        setImageDrawableOverride(drawable);
    }

    /**
     * 覆盖ImageView的此方法
     *
     * @param drawable
     */
    public void setImageDrawableOverride(Drawable drawable)
    {
        if (!(drawable instanceof BlurredBitmapDrawable))
            mOriginalDrawable = drawable;

        if (drawable == null || drawable instanceof BlurredBitmapDrawable)
            setImageDrawableSuper(drawable);
        else
            blurDrawable(drawable);
    }

    private void blurDrawable(Drawable drawable)
    {
        if (drawable == null)
            throw new IllegalArgumentException("drawable is null when blurDrawable()");

        if (!mIsAttachedToWindow)
            return;

        if (drawable instanceof BlurredBitmapDrawable)
            throw new IllegalArgumentException("can not blur BlurredBitmapDrawable");

        if (mBlurAsync)
        {
            getBlurApi().blur(drawable).async().into(new BlurApi.Target()
            {
                @Override
                public void onBlurred(Bitmap bitmap)
                {
                    applyBlur(bitmap);
                }
            });
        } else
        {
            applyBlur(getBlurApi().blur(drawable).bitmap());
        }
    }

    private void applyBlur(Bitmap bitmap)
    {
        if (bitmap != null)
            setImageDrawableSuper(new BlurredBitmapDrawable(mContext.getResources(), bitmap));
    }

    protected abstract void setImageDrawableSuper(Drawable drawable);

    private static final class BlurredBitmapDrawable extends BitmapDrawable
    {
        private BlurredBitmapDrawable(Resources res, Bitmap bitmap)
        {
            super(res, bitmap);
        }
    }
}
