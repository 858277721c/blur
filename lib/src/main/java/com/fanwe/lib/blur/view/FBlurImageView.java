package com.fanwe.lib.blur.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class FBlurImageView extends ImageView implements BlurView
{
    private final BlurViewWrapper<ImageView> mBlurViewWrapper;

    public FBlurImageView(Context context)
    {
        this(context, null);
    }

    public FBlurImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mBlurViewWrapper = new BlurViewWrapper<ImageView>(getContext())
        {
            @Override
            protected void onDrawableBlurred(BlurredBitmapDrawable drawable, ImageView view)
            {
                view.setImageDrawable(drawable);
            }
        };

        final BlurViewAttrs viewAttrs = BlurViewAttrs.parse(context, attrs);
        setBlurRadius(viewAttrs.getRadius());
        setBlurDownSampling(viewAttrs.getDownSampling());
        setBlurColor(viewAttrs.getColor());
        setBlurAsync(viewAttrs.isAsync());
    }

    @Override
    public final void setBlurRadius(int radius)
    {
        mBlurViewWrapper.setBlurRadius(radius);
    }

    @Override
    public final void setBlurDownSampling(int downSampling)
    {
        mBlurViewWrapper.setBlurDownSampling(downSampling);
    }

    @Override
    public final void setBlurColor(int color)
    {
        mBlurViewWrapper.setBlurColor(color);
    }

    @Override
    public void setBlurAsync(boolean async)
    {
        mBlurViewWrapper.setBlurAsync(async);
    }

    @Override
    public void blur()
    {
        invalidate();
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

        if (drawable instanceof BlurViewWrapper.BlurredBitmapDrawable)
            super.onDraw(canvas);
        else
            mBlurViewWrapper.blurDrawable(drawable);
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        mBlurViewWrapper.setView(this);
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        mBlurViewWrapper.setView(null);
    }
}
