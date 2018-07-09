package com.fanwe.lib.blur.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import com.fanwe.lib.blur.core.Blur;
import com.fanwe.lib.blur.core.BlurFactory;

import java.lang.ref.WeakReference;

public class FBlurView extends View implements BlurView
{
    public FBlurView(Context context)
    {
        this(context, null);
    }

    public FBlurView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        final BlurViewAttrs viewAttrs = new BlurViewAttrs(context, attrs);
        setBlurRadius(viewAttrs.getRadius());
        setBlurDownSampling(viewAttrs.getDownSampling());
        setBlurColor(viewAttrs.getColor());
        setBlurAsync(viewAttrs.isAsync());
    }

    private Blur mBlur;
    private WeakReference<View> mBlurTarget;

    /**
     * 返回设置的要模糊的view
     *
     * @return
     */
    public final View getBlurTarget()
    {
        return mBlurTarget == null ? null : mBlurTarget.get();
    }

    /**
     * 设置要模糊的view
     *
     * @param blurTarget
     */
    public final void setBlurTarget(View blurTarget)
    {
        final View old = getBlurTarget();
        if (old != blurTarget)
        {
            if (old != null)
            {
                final ViewTreeObserver observer = old.getViewTreeObserver();
                if (observer.isAlive())
                    observer.removeOnPreDrawListener(mOnPreDrawListener);
            }

            mBlurTarget = blurTarget == null ? null : new WeakReference<>(blurTarget);

            if (blurTarget != null)
            {
                final ViewTreeObserver observer = blurTarget.getViewTreeObserver();
                if (observer.isAlive())
                    observer.addOnPreDrawListener(mOnPreDrawListener);
            }
        }
    }

    private final ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener()
    {
        @Override
        public boolean onPreDraw()
        {
            blur();
            return true;
        }
    };

    private Blur getBlur()
    {
        if (mBlur == null)
        {
            mBlur = BlurFactory.create(getContext());
            mBlur.setKeepDownSamplingSize(true);
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
    public void setBlurAsync(boolean async)
    {

    }

    @Override
    public void blur()
    {
        if (mBitmapBlurred == null)
        {
            mBitmapBlurred = getBlur().blur(getBlurTarget());
            if (mBitmapBlurred != null)
                invalidate();
        }
    }

    private Bitmap mBitmapBlurred;

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        final View target = getBlurTarget();
        if (target == null)
            return;

        if (mBitmapBlurred != null)
        {
            final int scale = getBlur().getDownSampling();

            canvas.save();
            canvas.translate(target.getX() - getX(), target.getY() - getY());
            canvas.scale(scale, scale);
            canvas.drawBitmap(mBitmapBlurred, 0, 0, null);
            canvas.restore();

            post(new Runnable()
            {
                @Override
                public void run()
                {
                    mBitmapBlurred = null;
                }
            });
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
}
