package com.fanwe.lib.blur.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class FBlurLayout extends FrameLayout implements BlurView
{
    public FBlurLayout(Context context)
    {
        this(context, null);
    }

    public FBlurLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mBlurView = new FBlurView(context, attrs);
    }

    private final FBlurView mBlurView;

    @Override
    public void setBlurRadius(int radius)
    {
        mBlurView.setBlurRadius(radius);
    }

    @Override
    public void setBlurDownSampling(int downSampling)
    {
        mBlurView.setBlurDownSampling(downSampling);
    }

    @Override
    public void setBlurColor(int color)
    {
        mBlurView.setBlurColor(color);
    }

    @Override
    public void setBlurAsync(boolean async)
    {
        mBlurView.setBlurAsync(async);
    }

    @Override
    public void blur()
    {
        mBlurView.blur();
    }

    @Override
    public void onViewAdded(View child)
    {
        super.onViewAdded(child);
        if (getChildCount() > 2)
            throw new RuntimeException("can not add child more");

        if (child != mBlurView)
        {
            mBlurView.setBlurTarget(child);
            addView(mBlurView);
        }
    }

    @Override
    public void onViewRemoved(View child)
    {
        super.onViewRemoved(child);

        if (child != mBlurView)
        {
            mBlurView.setBlurTarget(null);
            removeView(mBlurView);
        }
    }
}
