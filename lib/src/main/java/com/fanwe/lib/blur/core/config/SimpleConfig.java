package com.fanwe.lib.blur.core.config;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class SimpleConfig implements BlurConfig
{
    private int mWidth;
    private int mHeight;
    private int mDownSampling;

    private Bitmap mBitmapOutput;
    private Bitmap mBitmapInput;
    private Canvas mCanvasInput;

    @Override
    public boolean init(int width, int height, int downSampling)
    {
        mWidth = width;
        mHeight = height;
        mDownSampling = downSampling;

        final float scale = 1.0f / mDownSampling;
        final int scaledWidth = (int) (width * scale);
        final int scaledHeight = (int) (height * scale);
        if (scaledWidth <= 0 || scaledHeight <= 0)
            return false;

        final Bitmap.Config config = Bitmap.Config.ARGB_8888;

        mBitmapOutput = Bitmap.createBitmap(scaledWidth, scaledHeight, config);
        mBitmapInput = Bitmap.createBitmap(scaledWidth, scaledHeight, config);

        mCanvasInput = new Canvas(mBitmapInput);
        mCanvasInput.scale(scale, scale);
        return true;
    }

    @Override
    public final int getWidth()
    {
        return mWidth;
    }

    @Override
    public final int getHeight()
    {
        return mHeight;
    }

    @Override
    public final int getDownSampling()
    {
        return mDownSampling;
    }

    @Override
    public final Bitmap getBitmapOutput()
    {
        return mBitmapOutput;
    }

    @Override
    public final Bitmap getBitmapInput()
    {
        return mBitmapInput;
    }

    @Override
    public final Canvas getCanvasInput()
    {
        return mCanvasInput;
    }

    @Override
    public void recycle()
    {
        if (mBitmapInput != null)
        {
            mBitmapInput.recycle();
            mBitmapInput = null;
        }
    }
}
