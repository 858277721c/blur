package com.fanwe.lib.blur.core.config;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class SimpleConfig implements BlurConfig
{
    private int mWidth;
    private int mHeight;

    private float mScale;
    private int mScaledWidth;
    private int mScaledHeight;

    private Bitmap mBitmapInput;
    private Canvas mCanvas;

    private boolean mHasInit;

    private boolean mIsDebug = true;

    @Override
    public boolean init(int width, int height, int downSampling)
    {
        mHasInit = false;

        if (width <= 0 || height <= 0)
            return false;

        final float scale = 1.0f / downSampling;
        final int scaledWidth = (int) (width * scale);
        final int scaledHeight = (int) (height * scale);
        if (scaledWidth <= 0 || scaledHeight <= 0)
            return false;

        mWidth = width;
        mHeight = height;

        mScale = scale;
        mScaledWidth = scaledWidth;
        mScaledHeight = scaledHeight;

        mHasInit = true;
        return true;
    }

    private void checkInit()
    {
        if (!mHasInit)
            throw new RuntimeException("config has not init");
    }

    @Override
    public int getWidth()
    {
        return mWidth;
    }

    @Override
    public int getHeight()
    {
        return mHeight;
    }

    @Override
    public Bitmap newBitmapOutput()
    {
        checkInit();
        return Bitmap.createBitmap(mScaledWidth, mScaledHeight, Bitmap.Config.ARGB_8888);
    }

    @Override
    public Bitmap getBitmapInput()
    {
        checkInit();
        if (mBitmapInput == null
                || mBitmapInput.isRecycled()
                || mBitmapInput.getWidth() != mScaledWidth
                || mBitmapInput.getHeight() != mScaledHeight)
        {

            if (mBitmapInput != null)
            {
                mBitmapInput.recycle();
                if (mIsDebug)
                    Log.e(getClass().getSimpleName(), mBitmapInput + " recycled");
            }

            mBitmapInput = Bitmap.createBitmap(mScaledWidth, mScaledHeight, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmapInput);
            mCanvas.scale(mScale, mScale);

            if (mIsDebug)
                Log.i(getClass().getSimpleName(), mBitmapInput + " created:" + mScaledWidth + "," + mScaledHeight);
        }
        return mBitmapInput;
    }

    @Override
    public Canvas getCanvas()
    {
        getBitmapInput();
        return mCanvas;
    }

    @Override
    public void recycle()
    {
        mHasInit = false;
        if (mBitmapInput != null)
        {
            mBitmapInput.recycle();
            if (mIsDebug)
                Log.e(getClass().getSimpleName(), mBitmapInput + " recycled in recycle method");
            mBitmapInput = null;
        }
    }
}
