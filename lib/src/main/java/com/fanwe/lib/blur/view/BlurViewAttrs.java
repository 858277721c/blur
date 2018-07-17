package com.fanwe.lib.blur.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.fanwe.lib.blur.DefaultBlurSettings;
import com.fanwe.lib.blur.R;

class BlurViewAttrs
{
    private int mBlurRadius;
    private int mBlurDownSampling;
    private int mBlurColor;
    private boolean mBlurAsync;

    private BlurViewAttrs(Context context, AttributeSet attrs)
    {
        final DefaultBlurSettings settings = DefaultBlurSettings.get(context);
        mBlurRadius = settings.getRadius();
        mBlurDownSampling = settings.getDownSampling();
        mBlurColor = settings.getColor();

        if (attrs != null)
        {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.lib_blur_blur_view);

            mBlurRadius = a.getInt(R.styleable.lib_blur_blur_view_blurRadius, mBlurRadius);
            mBlurDownSampling = a.getInt(R.styleable.lib_blur_blur_view_blurDownSampling, mBlurDownSampling);
            mBlurColor = a.getInt(R.styleable.lib_blur_blur_view_blurColor, mBlurColor);
            mBlurAsync = a.getBoolean(R.styleable.lib_blur_blur_view_blurAsync, mBlurAsync);

            a.recycle();
        }
    }

    public static BlurViewAttrs parse(Context context, AttributeSet attrs)
    {
        return new BlurViewAttrs(context, attrs);
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

    public boolean isBlurAsync()
    {
        return mBlurAsync;
    }
}
