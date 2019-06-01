package com.sd.lib.blur.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class FBlurImageView extends ImageView implements BlurView
{
    private final FBlurImageViewProxy mBlurImageViewProxy;

    public FBlurImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mBlurImageViewProxy = new FBlurImageViewProxy(this, attrs)
        {
            @Override
            protected void setImageDrawableSuper(Drawable drawable)
            {
                FBlurImageView.super.setImageDrawable(drawable);
            }
        };
    }

    @Override
    public final void setBlurRadius(int radius)
    {
        mBlurImageViewProxy.setBlurRadius(radius);
    }

    @Override
    public final void setBlurDownSampling(int downSampling)
    {
        mBlurImageViewProxy.setBlurDownSampling(downSampling);
    }

    @Override
    public final void setBlurColor(int color)
    {
        mBlurImageViewProxy.setBlurColor(color);
    }

    @Override
    public final void setBlurAsync(boolean async)
    {
        mBlurImageViewProxy.setBlurAsync(async);
    }

    @Override
    public final void blur()
    {
        mBlurImageViewProxy.blur();
    }

    @Override
    public void setImageResource(int resId)
    {
        mBlurImageViewProxy.setImageResourceOverride(resId);
    }

    @Override
    public void setImageDrawable(Drawable drawable)
    {
        mBlurImageViewProxy.setImageDrawableOverride(drawable);
    }
}
