package com.fanwe.lib.blur.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.fanwe.lib.blur.DefaultBlurSettings;

abstract class BaseBlur implements Blur
{
    private int mRadius = 15;
    private int mDownSampling = 8;
    private int mColor = Color.TRANSPARENT;
    private boolean mKeepDownSamplingSize = false;
    private boolean mDestroyAfterBlur = true;
    private boolean mDownSamplingChanged;

    private int mWidth;
    private int mHeight;

    private Bitmap mBitmapOutput;
    private Bitmap mBitmapInput;
    private Canvas mCanvasInput;

    public BaseBlur(Context context)
    {
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

        if (mDownSampling != downSampling)
        {
            mDownSampling = downSampling;
            mDownSamplingChanged = true;
        }
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

    private boolean init(int width, int height)
    {
        if (isConfigurationChanged(width, height))
        {
            mDownSamplingChanged = false;
            mWidth = width;
            mHeight = height;

            final float scale = 1.0f / mDownSampling;
            final int scaledWidth = (int) (width * scale);
            final int scaledHeight = (int) (height * scale);
            if (scaledWidth <= 0 || scaledHeight <= 0)
                return false;

            final Bitmap.Config config = Bitmap.Config.ARGB_8888;
            mBitmapOutput = Bitmap.createBitmap(scaledWidth, scaledHeight, config);

            if (mBitmapInput != null)
                mBitmapInput.recycle();
            mBitmapInput = Bitmap.createBitmap(scaledWidth, scaledHeight, config);

            mCanvasInput = new Canvas(mBitmapInput);
            mCanvasInput.scale(scale, scale);

            onConfigurationChanged(scaledWidth, scaledHeight, scale, mBitmapInput, mBitmapOutput);
        }
        return true;
    }

    protected boolean isConfigurationChanged(int width, int height)
    {
        return mDownSamplingChanged
                || width != mWidth || height != mHeight
                || mBitmapInput == null || mBitmapInput.isRecycled()
                || mBitmapOutput == null || mBitmapOutput.isRecycled();
    }

    protected void onConfigurationChanged(int scaledWidth, int scaledHeight, float scale, Bitmap bitmapInput, Bitmap bitmapOutput)
    {

    }

    @Override
    public final Bitmap blur(View view)
    {
        if (view == null)
            return null;

        if (!init(view.getWidth(), view.getHeight()))
            return null;

        view.draw(mCanvasInput);
        return blurInternal();
    }

    @Override
    public Bitmap blur(Drawable drawable)
    {
        if (drawable == null)
            return null;

        if (drawable instanceof BitmapDrawable)
            return blur(((BitmapDrawable) drawable).getBitmap());

        if (!init(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()))
            return null;

        drawable.draw(mCanvasInput);
        return blurInternal();
    }

    @Override
    public final Bitmap blur(final Bitmap bitmap)
    {
        if (bitmap == null)
            return null;

        if (!init(bitmap.getWidth(), bitmap.getHeight()))
            return null;

        mCanvasInput.drawBitmap(bitmap, 0, 0, null);
        return blurInternal();
    }

    private Bitmap blurInternal()
    {
        mCanvasInput.drawColor(mColor);
        onBlurImplemention(mBitmapInput, mBitmapOutput);

        if (mBitmapInput.isRecycled() || mBitmapOutput.isRecycled())
            throw new RuntimeException("you can not recycle bitmapInput or bitmapOutput");

        Bitmap bitmapResult = null;
        if (mDownSampling == 1 || mKeepDownSamplingSize)
        {
            bitmapResult = mBitmapOutput;
        } else
        {
            bitmapResult = Bitmap.createScaledBitmap(mBitmapOutput, mWidth, mHeight, true);
        }

        if (mDestroyAfterBlur)
            destroy();

        return bitmapResult;
    }

    /**
     * 模糊实现
     *
     * @param bitmapInput  要模糊的对象
     * @param bitmapOutput 模糊后输出的对象
     */
    protected abstract void onBlurImplemention(Bitmap bitmapInput, Bitmap bitmapOutput);

    @Override
    public void destroy()
    {
        if (mBitmapInput != null)
        {
            mBitmapInput.recycle();
            mBitmapInput = null;
        }
    }
}
