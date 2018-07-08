package com.fanwe.lib.blur.core;

import android.content.Context;

public final class BlurFactory
{
    private BlurFactory()
    {
    }

    public static Blur create(Context context)
    {
        return new CompatBlur(context);
    }
}
