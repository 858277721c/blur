package com.fanwe.lib.blur.core.strategy;

import android.content.Context;

public final class BlurStrategyFactory
{
    private BlurStrategyFactory()
    {
    }

    public static BlurStrategy create(Context context)
    {
        return new CompatStrategy(context);
    }
}
