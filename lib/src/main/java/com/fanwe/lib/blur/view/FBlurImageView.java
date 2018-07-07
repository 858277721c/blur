package com.fanwe.lib.blur.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.fanwe.lib.blur.core.Blur;
import com.fanwe.lib.blur.core.CompatBlur;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FBlurImageView extends ImageView implements BlurView
{
    public FBlurImageView(Context context)
    {
        super(context);
    }

    public FBlurImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public FBlurImageView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    private Blur mBlur;

    private ExecutorService mExecutorService;
    private Future mFuture;
    private Runnable mRunnable;

    private Drawable mDrawable;

    private final Blur getBlur()
    {
        if (mBlur == null)
        {
            mBlur = new CompatBlur(getContext());
            mBlur.setDestroyAfterBlur(false);
        }
        return mBlur;
    }

    @Override
    public final void setBlurRadius(int radius)
    {
        getBlur().setRadius(radius);
    }

    @Override
    public final void setBlurDownSampling(int downSampling)
    {
        getBlur().setDownSampling(downSampling);
    }

    @Override
    public final void setBlurColor(int color)
    {
        getBlur().setColor(color);
    }

    @Override
    public void blur()
    {
        invalidate();
    }

    private void setDrawable(Drawable drawable)
    {
        if (mDrawable != drawable)
        {
            mDrawable = drawable;
            if (!(drawable instanceof InternalBitmapDrawable))
                submit(new BlurDrawableRunnable(drawable));
        }
    }

    private void submit(Runnable runnable)
    {
        if (mExecutorService == null)
            mExecutorService = Executors.newSingleThreadExecutor();

        if (mFuture != null)
            mFuture.cancel(true);

        mRunnable = runnable;
        mFuture = mExecutorService.submit(runnable);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        final Drawable drawable = getDrawable();
        setDrawable(drawable);

        if (drawable instanceof InternalBitmapDrawable)
            super.onDraw(canvas);
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        if (mRunnable != null)
            submit(mRunnable);
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        if (mBlur != null)
        {
            mBlur.destroy();
            mBlur = null;
        }
        if (mFuture != null)
        {
            mFuture.cancel(true);
            mFuture = null;
        }
        if (mExecutorService != null)
        {
            mExecutorService.shutdownNow();
            mExecutorService = null;
        }
    }

    private final class BlurDrawableRunnable implements Runnable
    {
        private final Drawable mDrawable;

        public BlurDrawableRunnable(Drawable drawable)
        {
            if (drawable == null)
                throw new NullPointerException("drawable is null");
            mDrawable = drawable;
        }

        @Override
        public void run()
        {
            final Bitmap bitmap = drawableToBitmap(mDrawable);
            final Bitmap blurBitmap = getBlur().blur(bitmap);
            bitmap.recycle();
            post(new Runnable()
            {
                @Override
                public void run()
                {
                    FBlurImageView.super.setImageDrawable(new InternalBitmapDrawable(getResources(), blurBitmap));
                }
            });
            mRunnable = null;
        }
    }

    private static final class InternalBitmapDrawable extends BitmapDrawable
    {
        public InternalBitmapDrawable(Resources res, Bitmap bitmap)
        {
            super(res, bitmap);
        }
    }

    private static Bitmap drawableToBitmap(Drawable drawable)
    {
        if (drawable == null)
            return null;
        if (drawable instanceof BitmapDrawable)
            return ((BitmapDrawable) drawable).getBitmap();

        final Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        final Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), config);
        final Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
