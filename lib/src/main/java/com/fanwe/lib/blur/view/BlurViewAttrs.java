package com.fanwe.lib.blur.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

import com.fanwe.lib.blur.R;

class BlurViewAttrs
{
    private int mBlurRadius = 10;
    private int mBlurDownSampling = 8;
    private int mBlurColor = Color.TRANSPARENT;

    public BlurViewAttrs(Context context, AttributeSet attrs)
    {
        if (attrs == null)
            return;

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.lib_blur_blur_view);

        mBlurRadius = a.getInt(R.styleable.lib_blur_blur_view_blurRadius, mBlurRadius);
        mBlurDownSampling = a.getInt(R.styleable.lib_blur_blur_view_blurDownSampling, mBlurDownSampling);
        mBlurColor = a.getInt(R.styleable.lib_blur_blur_view_blurColor, mBlurColor);

        a.recycle();
    }

    public int getBlurRadius()
    {
        return mBlurRadius;
    }

    public int getBlurDownSampling()
    {
        return mBlurDownSampling;
    }

    public int getBlurColor()
    {
        return mBlurColor;
    }
}
