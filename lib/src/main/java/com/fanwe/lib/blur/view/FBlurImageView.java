package com.fanwe.lib.blur.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class FBlurImageView extends ImageView implements BlurView
{
    public FBlurImageView(Context context)
    {
        this(context, null);
    }

    public FBlurImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        final BlurViewAttrs viewAttrs = new BlurViewAttrs(context, attrs);
        setBlurRadius(viewAttrs.getBlurRadius());
        setBlurDownSampling(viewAttrs.getBlurDownSampling());
        setBlurColor(viewAttrs.getBlurColor());
        setBlurAsync(viewAttrs.isBlurAsync());
    }

    private BlurViewWrapper<ImageView> mBlurViewWrapper;

    private BlurViewWrapper<ImageView> getBlurViewWrapper()
    {
        if (mBlurViewWrapper == null)
        {
            mBlurViewWrapper = new BlurViewWrapper<ImageView>(getContext())
            {
                @Override
                protected Drawable getViewDrawable(ImageView view)
                {
                    return view.getDrawable();
                }

                @Override
                protected void onDrawableBlurred(BlurredBitmapDrawable drawable, ImageView view)
                {
                    view.setImageDrawable(drawable);
                }
            };
        }
        return mBlurViewWrapper;
    }

    @Override
    public final void setBlurRadius(int radius)
    {
        getBlurViewWrapper().setBlurRadius(radius);
    }

    @Override
    public final void setBlurDownSampling(int downSampling)
    {
        getBlurViewWrapper().setBlurDownSampling(downSampling);
    }

    @Override
    public final void setBlurColor(int color)
    {
        getBlurViewWrapper().setBlurColor(color);
    }

    @Override
    public void setBlurAsync(boolean async)
    {
        getBlurViewWrapper().setBlurAsync(async);
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
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        getBlurViewWrapper().setView(this);
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        getBlurViewWrapper().setView(null);
    }
}
