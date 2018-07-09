package com.fanwe.lib.blur.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.fanwe.lib.blur.DefaultBlurSettings;
import com.fanwe.lib.blur.R;

class BlurViewAttrs
{
    private int mRadius;
    private int mDownSampling;
    private int mColor;
    private boolean mAsync;

    private BlurViewAttrs(Context context, AttributeSet attrs)
    {
        final DefaultBlurSettings settings = DefaultBlurSettings.get(context);
        mRadius = settings.getRadius();
        mDownSampling = settings.getDownSampling();
        mColor = settings.getColor();

        if (attrs != null)
        {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.lib_blur_blur_view);

            mRadius = a.getInt(R.styleable.lib_blur_blur_view_blurRadius, mRadius);
            mDownSampling = a.getInt(R.styleable.lib_blur_blur_view_blurDownSampling, mDownSampling);
            mColor = a.getInt(R.styleable.lib_blur_blur_view_blurColor, mColor);
            mAsync = a.getBoolean(R.styleable.lib_blur_blur_view_blurAsync, mAsync);

            a.recycle();
        }
    }

    public static BlurViewAttrs parse(Context context, AttributeSet attrs)
    {
        return new BlurViewAttrs(context, attrs);
    }

    public int getRadius()
    {
        return mRadius;
    }

    public int getDownSampling()
    {
        return mDownSampling;
    }

    public int getColor()
    {
        return mColor;
    }

    public boolean isAsync()
    {
        return mAsync;
    }
}
