package com.fanwe.lib.blur.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    private ExecutorService mExecutorService;
    private Future mFuture;
    private Blur mBlur;

    private final Blur getBlur()
    {
        if (mBlur == null)
        {
            mBlur = new CompatBlur(getContext());
            mBlur.setDestroyAfterBlur(false);
        }
        return mBlur;
    }

    private ExecutorService getExecutorService()
    {
        if (mExecutorService == null)
            mExecutorService = Executors.newSingleThreadExecutor();
        return mExecutorService;
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

    @Override
    public void setImageResource(int resId)
    {
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
        setImageBitmap(bitmap);
    }

    @Override
    public void setImageDrawable(Drawable drawable)
    {
        if (drawable == null)
        {
            super.setImageDrawable(null);
        } else
        {
            if (mFuture != null)
                mFuture.cancel(true);
            mFuture = getExecutorService().submit(new BlurDrawableRunnable(drawable));
        }
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
    }

    private final class BlurDrawableRunnable implements Runnable
    {
        private Drawable mDrawable;

        public BlurDrawableRunnable(Drawable drawable)
        {
            mDrawable = drawable;
        }

        @Override
        public void run()
        {
            final Bitmap bitmap = drawableToBitmap(mDrawable);
            if (bitmap != null)
            {
                final Bitmap blurBitmap = getBlur().blur(bitmap);
                bitmap.recycle();
                post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        FBlurImageView.super.setImageDrawable(new BitmapDrawable(getResources(), blurBitmap));
                    }
                });
            }
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
