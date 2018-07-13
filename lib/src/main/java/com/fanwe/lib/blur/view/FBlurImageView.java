package com.fanwe.lib.blur.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.fanwe.lib.blur.api.BlurApi;
import com.fanwe.lib.blur.api.BlurApiFactory;
import com.fanwe.lib.blur.api.target.BlurTarget;

public class FBlurImageView extends ImageView implements BlurView
{
    private BlurApi mBlurApi;
    private boolean mBlurAsync;

    private Drawable mDrawable;
    private boolean mIsAttachedToWindow;
    private long mBlurTime;

    public FBlurImageView(Context context)
    {
        this(context, null);
    }

    public FBlurImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        final BlurViewAttrs viewAttrs = BlurViewAttrs.parse(context, attrs);
        setBlurRadius(viewAttrs.getBlurRadius());
        setBlurDownSampling(viewAttrs.getBlurDownSampling());
        setBlurColor(viewAttrs.getBlurColor());
        setBlurAsync(viewAttrs.isBlurAsync());

        if (getBackground() == null)
            setBackgroundColor(viewAttrs.getBlurColor());
    }

    private BlurApi getBlurApi()
    {
        if (mBlurApi == null)
        {
            mBlurApi = BlurApiFactory.create(getContext());
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
        setImageDrawable(mDrawable);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        final Drawable drawable = getDrawable();
        if (drawable == null)
        {
            super.onDraw(canvas);
            return;
        }

        if (drawable instanceof BlurredBitmapDrawable)
        {
            super.onDraw(canvas);
        } else
        {
            mDrawable = drawable;
            blurDrawable(drawable);
        }
    }

    private void blurDrawable(Drawable drawable)
    {
        if (drawable instanceof BlurredBitmapDrawable)
            throw new IllegalArgumentException("can not blur BlurredBitmapDrawable");
        if (!mIsAttachedToWindow)
            throw new RuntimeException("can not blur when view is detached");

        final long blurTime = System.currentTimeMillis();
        getBlurApi().blur(drawable).async(mBlurAsync).into(new BlurTarget()
        {
            @Override
            public void onBlurred(Bitmap bitmap)
            {
                if (bitmap == null)
                    return;

                if (blurTime >= mBlurTime)
                {
                    mBlurTime = blurTime;
                    setImageDrawable(new BlurredBitmapDrawable(getResources(), bitmap));
                }
            }
        });
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        mIsAttachedToWindow = true;
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        mIsAttachedToWindow = false;
        if (mBlurApi != null)
            mBlurApi.destroy();
    }

    private static final class BlurredBitmapDrawable extends BitmapDrawable
    {
        private BlurredBitmapDrawable(Resources res, Bitmap bitmap)
        {
            super(res, bitmap);
        }
    }
}
