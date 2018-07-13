package com.fanwe.lib.blur.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

import com.fanwe.lib.blur.DefaultBlurSettings;
import com.fanwe.lib.blur.core.config.BlurConfig;
import com.fanwe.lib.blur.core.config.SimpleConfig;
import com.fanwe.lib.blur.core.source.BlurSource;
import com.fanwe.lib.blur.core.strategy.BlurStrategy;
import com.fanwe.lib.blur.core.strategy.BlurStrategyFactory;

class SimpleBlur implements Blur
{
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    private int mRadius = 15;
    private int mDownSampling = 8;
    private int mColor = Color.TRANSPARENT;
    private boolean mKeepDownSamplingSize = false;
    private boolean mDestroyAfterBlur = true;

    private final BlurStrategy mStrategy;
    private final BlurConfig mConfig;

    public SimpleBlur(Context context)
    {
        mStrategy = BlurStrategyFactory.create(context);
        mConfig = new SimpleConfig();

        final DefaultBlurSettings settings = DefaultBlurSettings.get(context);
        setRadius(settings.getRadius());
        setDownSampling(settings.getDownSampling());
        setColor(settings.getColor());
    }

    @Override
    public void setRadius(int radius)
    {
        if (radius > 0 && radius <= 25)
            mRadius = radius;
        else
            throw new IllegalArgumentException("radius out of range (0 < radius <= 25)");

    }

    @Override
    public void setDownSampling(int downSampling)
    {
        if (downSampling > 0)
            mDownSampling = downSampling;
        else
            throw new IllegalArgumentException("downSampling out of range (downSampling > 0)");

    }

    @Override
    public void setColor(int color)
    {
        mColor = color;
    }

    @Override
    public void setKeepDownSamplingSize(boolean keepDownSamplingSize)
    {
        mKeepDownSamplingSize = keepDownSamplingSize;
    }

    @Override
    public void setDestroyAfterBlur(boolean destroyAfterBlur)
    {
        mDestroyAfterBlur = destroyAfterBlur;
    }

    @Override
    public int getRadius()
    {
        return mRadius;
    }

    @Override
    public int getDownSampling()
    {
        return mDownSampling;
    }

    @Override
    public int getColor()
    {
        return mColor;
    }

    @Override
    public boolean isKeepDownSamplingSize()
    {
        return mKeepDownSamplingSize;
    }

    @Override
    public boolean isDestroyAfterBlur()
    {
        return mDestroyAfterBlur;
    }

    @Override
    public Bitmap blur(BlurSource source)
    {
        if (source == null)
            return null;

        try
        {
            if (!mConfig.init(source.getWidth(), source.getHeight(), mDownSampling))
                return null;

            if (mConfig.getBitmapInput().isRecycled())
                throw new RuntimeException("bitmap for canvas is recycled");

            source.draw(mConfig.getCanvas(), MAIN_HANDLER);
            return blurInternal(mConfig);
        } finally
        {
            if (mDestroyAfterBlur)
                destroy();
        }
    }

    private Bitmap blurInternal(BlurConfig config)
    {
        final Bitmap bitmapOutput = config.newBitmapOutput();
        final Bitmap bitmapInput = config.getBitmapInput();
        final Canvas canvas = config.getCanvas();

        canvas.drawColor(mColor);
        mStrategy.blur(mRadius, bitmapInput, bitmapOutput);

        if (bitmapInput.isRecycled() || bitmapOutput.isRecycled())
            throw new RuntimeException("you can not recycle bitmapInput or bitmapOutput");

        Bitmap bitmapResult = null;
        if (mDownSampling == 1 || mKeepDownSamplingSize)
        {
            bitmapResult = bitmapOutput;
        } else
        {
            bitmapResult = Bitmap.createScaledBitmap(bitmapOutput, config.getWidth(), config.getHeight(), true);
        }

        if (bitmapOutput != bitmapResult)
            bitmapOutput.recycle();

        return bitmapResult;
    }

    @Override
    public void destroy()
    {
        mStrategy.destroy();
        mConfig.recycle();
    }
}
