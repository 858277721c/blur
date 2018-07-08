package com.fanwe.lib.blur.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.fanwe.lib.blur.core.Blur;
import com.fanwe.lib.blur.core.CompatBlur;
import com.fanwe.lib.blur.extend.ViewBlur;

public class FBlurImageView extends ImageView implements BlurView
{
    public FBlurImageView(Context context)
    {
        super(context);
        init();
    }

    public FBlurImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public FBlurImageView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Blur mBlur;
    private ViewBlur<ImageView> mImageViewBlur;

    private void init()
    {
        mBlur = new CompatBlur(getContext());
        mImageViewBlur = new ViewBlur<ImageView>(mBlur)
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
        mImageViewBlur.setView(this);
    }

    @Override
    public final void setBlurRadius(int radius)
    {
        mBlur.setRadius(radius);
    }

    @Override
    public final void setBlurDownSampling(int downSampling)
    {
        mBlur.setDownSampling(downSampling);
    }

    @Override
    public final void setBlurColor(int color)
    {
        mBlur.setColor(color);
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
}
