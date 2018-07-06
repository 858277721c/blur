package com.fanwe.lib.blur.core;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

public abstract class BaseBlur implements Blur
{
    private int mRadius = 10;
    private int mDownSampling = 8;
    private int mColorOverlay = Color.TRANSPARENT;
    private boolean mKeepDownSamplingSize;
    private boolean mDownSamplingChanged;

    private int mWidth;
    private int mHeight;

    private Bitmap mBitmapOutput;
    private Bitmap mBitmapInput;
    private Canvas mCanvasInput;

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
    public void setColorOverlay(int colorOverlay)
    {
        mColorOverlay = colorOverlay;
    }

    @Override
    public void setKeepDownSamplingSize(boolean keepDownSamplingSize)
    {
        mKeepDownSamplingSize = keepDownSamplingSize;
    }

    @Override
    public int getDownSampling()
    {
        return mDownSampling;
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

            if (mBitmapOutput != null)
                mBitmapOutput.recycle();
            mBitmapOutput = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);

            if (mBitmapInput != null)
                mBitmapInput.recycle();
            mBitmapInput = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);

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

    protected final int getRadius()
    {
        return mRadius;
    }

    @Override
    public final Bitmap blur(View view)
    {
        if (view == null)
            return null;

        final boolean isDrawingCacheEnabled = view.isDrawingCacheEnabled();
        final int quality = view.getDrawingCacheQuality();

        if (!isDrawingCacheEnabled)
            view.setDrawingCacheEnabled(true);

        if (quality != View.DRAWING_CACHE_QUALITY_LOW)
            view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);

        final Bitmap drawingCache = view.getDrawingCache();
        final Bitmap bitmapResult = blur(drawingCache);

        if (isDrawingCacheEnabled)
        {
            view.setDrawingCacheQuality(quality);
        } else
        {
            view.destroyDrawingCache();
            view.setDrawingCacheEnabled(false);
        }
        return bitmapResult;
    }

    @Override
    public final Bitmap blur(final Bitmap bitmap)
    {
        if (bitmap == null)
            return null;

        if (!init(bitmap.getWidth(), bitmap.getHeight()))
            return null;

        mCanvasInput.drawBitmap(bitmap, 0, 0, null);
        mCanvasInput.drawColor(mColorOverlay);

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

        if (bitmapResult == mBitmapOutput)
            bitmapResult = Bitmap.createBitmap(mBitmapOutput);

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
        if (mBitmapOutput != null)
        {
            mBitmapOutput.recycle();
            mBitmapOutput = null;
        }
    }
}
