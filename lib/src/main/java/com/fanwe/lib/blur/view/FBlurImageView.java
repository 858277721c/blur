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
        mImageViewBlur = new ViewBlur<ImageView>(getBlur())
        {
            @Override
            protected Drawable getDrawable(ImageView view)
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

    private Blur getBlur()
    {
        if (mBlur == null)
        {
            mBlur = new CompatBlur(getContext());
            mBlur.setDestroyAfterBlur(false);
        }
        return mBlur;
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
}
