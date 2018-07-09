package com.fanwe.lib.blur.api;

import android.content.Context;

public final class BlurApiFactory
{
    private BlurApiFactory()
    {
    }

    public static BlurApi create(Context context)
    {
        return new SimpleBlurApi(context);
    }
}
