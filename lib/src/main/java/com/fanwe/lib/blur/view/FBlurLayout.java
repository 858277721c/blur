package com.fanwe.lib.blur.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class FBlurLayout extends FrameLayout
{
    public FBlurLayout(Context context)
    {
        super(context);
    }

    public FBlurLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public FBlurLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    private FBlurView mBlurView;

    public FBlurView getBlurView()
    {
        if (mBlurView == null)
            mBlurView = new FBlurView(getContext());
        return mBlurView;
    }

    @Override
    public void onViewAdded(View child)
    {
        super.onViewAdded(child);
        if (getChildCount() > 2)
            throw new RuntimeException("can not add child more");

        final FBlurView blurView = getBlurView();
        if (child != blurView)
        {
            blurView.setTarget(child);
            addView(blurView);
        }
    }

    @Override
    public void onViewRemoved(View child)
    {
        super.onViewRemoved(child);

        final FBlurView blurView = getBlurView();
        if (child != blurView)
        {
            blurView.setTarget(null);
            removeView(blurView);
        }
    }
}
