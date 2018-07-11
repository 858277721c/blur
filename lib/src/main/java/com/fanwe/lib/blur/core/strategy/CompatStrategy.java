package com.fanwe.lib.blur.core.strategy;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

class CompatStrategy extends BaseStrategy
{
    private final BlurStrategy mStrategy;

    public CompatStrategy(Context context)
    {
        if (Build.VERSION.SDK_INT >= 17)
        {
            final BlurStrategy strategy = new RenderScriptStrategy(context.getApplicationContext());
            if (strategy.test())
            {
                strategy.destroy();
                mStrategy = strategy;
            } else
            {
                mStrategy = new JavaStrategy();
            }
        } else
        {
            mStrategy = new JavaStrategy();
        }
    }

    @Override
    public void blur(int radius, Bitmap bitmapInput, Bitmap bitmapOutput)
    {
        mStrategy.blur(radius, bitmapInput, bitmapOutput);
    }

    @Override
    public void destroy()
    {
        mStrategy.destroy();
    }
}
