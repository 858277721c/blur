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
import com.fanwe.lib.blur.api.BlurTarget;

public class FBlurImageView extends ImageView implements BlurView
{
    private BlurApi mBlurApi;
    private Drawable mDrawable;

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
            mBlurApi.destroyAfterBlur(false);
        }
        return mBlurApi;
    }

    @Override
    public final void setBlurRadius(int radius)
    {
        getBlurApi().radius(radius);
    }

    @Override
    public final void setBlurDownSampling(int downSampling)
    {
        getBlurApi().downSampling(downSampling);
    }

    @Override
    public final void setBlurColor(int color)
    {
        getBlurApi().color(color);
    }

    @Override
    public void setBlurAsync(boolean async)
    {
        getBlurApi().async(async);
    }

    @Override
    public void blur()
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

        getBlurApi().blur(drawable).into(new BlurTarget()
        {
            @Override
            public void onBlurred(Bitmap bitmap)
            {
                if (bitmap == null)
                    return;

                setImageDrawable(new BlurredBitmapDrawable(getResources(), bitmap));
            }
        });
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
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
