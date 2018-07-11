package com.fanwe.lib.blur.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.fanwe.lib.blur.DefaultBlurSettings;
import com.fanwe.lib.blur.core.config.BlurConfig;
import com.fanwe.lib.blur.core.config.SimpleConfig;
import com.fanwe.lib.blur.core.source.BlurSource;
import com.fanwe.lib.blur.core.source.BlurSourceFactory;
import com.fanwe.lib.blur.core.strategy.BlurStrategy;
import com.fanwe.lib.blur.core.strategy.BlurStrategyFactory;

class SimpleBlur implements Blur
{
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
        if (radius <= 0 || radius > 25)
            throw new IllegalArgumentException("radius out of range (0 < radius <= 25)");
        mRadius = radius;
    }

    @Override
    public void setDownSampling(int downSampling)
    {
        if (downSampling <= 0)
            throw new IllegalArgumentException("downSampling out of range (downSampling > 0)");
        mDownSampling = downSampling;
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
    public Bitmap blur(Bitmap source)
    {
        if (source == null)
            return null;
        return blur(BlurSourceFactory.create(source));
    }

    @Override
    public Bitmap blur(View source)
    {
        if (source == null)
            return null;
        return blur(BlurSourceFactory.create(source));
    }

    @Override
    public Bitmap blur(Drawable source)
    {
        if (source == null)
            return null;
        return blur(BlurSourceFactory.create(source));
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

            source.draw(mConfig.getCanvasInput());
            return blurInternal();
        } finally
        {
            if (mDestroyAfterBlur)
                destroy();
        }
    }

    private Bitmap blurInternal()
    {
        final BlurConfig config = mConfig;

        final Bitmap bitmapOutput = config.getBitmapOutput();
        final Bitmap bitmapInput = config.getBitmapInput();
        final Canvas canvas = config.getCanvasInput();

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

        return bitmapResult;
    }

    @Override
    public void destroy()
    {
        mStrategy.destroy();
    }
}
