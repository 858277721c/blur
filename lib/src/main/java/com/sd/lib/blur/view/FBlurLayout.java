package com.sd.lib.blur.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class FBlurLayout extends FrameLayout implements BlurView
{
    private final FBlurView mBlurView;

    public FBlurLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mBlurView = new FBlurView(context, attrs);
    }

    @Override
    public final void setBlurRadius(int radius)
    {
        mBlurView.setBlurRadius(radius);
    }

    @Override
    public final void setBlurDownSampling(int downSampling)
    {
        mBlurView.setBlurDownSampling(downSampling);
    }

    @Override
    public final void setBlurColor(int color)
    {
        mBlurView.setBlurColor(color);
    }

    @Override
    public final void setBlurAsync(boolean async)
    {
        mBlurView.setBlurAsync(async);
    }

    @Override
    public final void blur()
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
            mBlurView.setBlurSource(child);
            addView(mBlurView);
        }
    }

    @Override
    public void onViewRemoved(View child)
    {
        super.onViewRemoved(child);

        if (child != mBlurView)
        {
            mBlurView.setBlurSource(null);
            removeView(mBlurView);
        }
    }
}
