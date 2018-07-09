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

        mBlur = BlurFactory.create(context);
        mBlur.setKeepDownSamplingSize(true);
        mBlur.setDestroyAfterBlur(false);

        final BlurViewAttrs viewAttrs = BlurViewAttrs.parse(context, attrs);
        setBlurRadius(viewAttrs.getRadius());
        setBlurDownSampling(viewAttrs.getDownSampling());
        setBlurColor(viewAttrs.getColor());
        setBlurAsync(viewAttrs.isAsync());
    }

    private final Blur mBlur;
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

    @Override
    public final void setBlurRadius(int radius)
    {
        mBlur.setRadius(radius);
    }

    @Override
    public final void setBlurDownSampling(int downSampling)
    {
        mBlur.setDownSampling(downSampling);
    }

    @Override
    public final void setBlurColor(int color)
    {
        mBlur.setColor(color);
    }

    @Override
    public void setBlurAsync(boolean async)
    {

    }

    @Override
    public void blur()
    {
        if (mIsDrawingBlur)
            return;

        mBitmapBlurred = mBlur.blur(getBlurTarget());
        if (mBitmapBlurred != null)
        {
            mIsDrawingBlur = true;
            invalidate();
        }
    }

    private Bitmap mBitmapBlurred;
    private boolean mIsDrawingBlur;

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        final View target = getBlurTarget();
        if (target == null)
            return;

        if (mBitmapBlurred != null)
        {
            final int scale = mBlur.getDownSampling();

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
                    mIsDrawingBlur = false;
                }
            });
        }
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        mBlur.destroy();
    }
}
