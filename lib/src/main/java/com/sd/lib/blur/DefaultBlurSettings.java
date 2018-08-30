package com.sd.lib.blur;

import android.content.Context;
import android.content.res.Resources;

public class DefaultBlurSettings
{
    private final int mRadius;
    private final int mDownSampling;
    private final int mColor;

    private DefaultBlurSettings(Context context)
    {
        final Resources resources = context.getResources();
        mRadius = resources.getInteger(R.integer.lib_blur_radius);
        mDownSampling = resources.getInteger(R.integer.lib_blur_down_sampling);
        mColor = resources.getColor(R.color.lib_blur_color);
    }

    public static DefaultBlurSettings get(Context context)
    {
        return new DefaultBlurSettings(context);
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
}
