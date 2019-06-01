package com.sd.lib.blur.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class FBlurImageView extends ImageView implements BlurView
{
    private FBlurImageViewProxy mProxy;

    public FBlurImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        getProxy().init(this, attrs);
    }

    private FBlurImageViewProxy getProxy()
    {
        if (mProxy == null)
        {
            mProxy = new FBlurImageViewProxy(getContext())
            {
                @Override
                protected void setImageDrawableSuper(Drawable drawable)
                {
                    FBlurImageView.super.setImageDrawable(drawable);
                }
            };
        }
        return mProxy;
    }

    @Override
    public final void setBlurRadius(int radius)
    {
        getProxy().setBlurRadius(radius);
    }

    @Override
    public final void setBlurDownSampling(int downSampling)
    {
        getProxy().setBlurDownSampling(downSampling);
    }

    @Override
    public final void setBlurColor(int color)
    {
        getProxy().setBlurColor(color);
    }

    @Override
    public final void setBlurAsync(boolean async)
    {
        getProxy().setBlurAsync(async);
    }

    @Override
    public final void blur()
    {
        getProxy().blur();
    }

    @Override
    public void setImageResource(int resId)
    {
        getProxy().setImageResourceOverride(resId);
    }

    @Override
    public void setImageDrawable(Drawable drawable)
    {
        getProxy().setImageDrawableOverride(drawable);
    }
}
