package com.fanwe.lib.blur.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.fanwe.lib.blur.core.Blur;
import com.fanwe.lib.blur.core.BlurFactory;
import com.fanwe.lib.blur.extend.ViewBlur;

public class FBlurImageView extends ImageView implements BlurView
{
    public FBlurImageView(Context context)
    {
        super(context);
    }

    public FBlurImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public FBlurImageView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    private Blur mBlur;
    private ViewBlur<ImageView> mImageViewBlur;

    private Blur getBlur()
    {
        if (mBlur == null)
            mBlur = BlurFactory.create(getContext());
        return mBlur;
    }

    private ViewBlur<ImageView> getImageViewBlur()
    {
        if (mImageViewBlur == null)
        {
            mImageViewBlur = new ViewBlur<ImageView>(getBlur())
            {
                @Override
                protected Drawable getViewDrawable(ImageView view)
                {
                    return view.getDrawable();
                }

                @Override
                protected void onBlur(BlurredBitmapDrawable drawable, ImageView view)
                {
                    view.setImageDrawable(drawable);
                }
            };
        }
        return mImageViewBlur;
    }

    @Override
    public final void setBlurRadius(int radius)
    {
        getBlur().setRadius(radius);
    }

    @Override
    public final void setBlurDownSampling(int downSampling)
    {
        getBlur().setDownSampling(downSampling);
    }

    @Override
    public final void setBlurColor(int color)
    {
        getBlur().setColor(color);
    }

    @Override
    public void blur()
    {
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (getDrawable() instanceof ViewBlur.BlurredBitmapDrawable)
            super.onDraw(canvas);
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        getImageViewBlur().setView(this);
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        getImageViewBlur().setView(null);
    }
}
