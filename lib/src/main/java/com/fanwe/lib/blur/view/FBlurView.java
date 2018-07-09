package com.fanwe.lib.blur.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import com.fanwe.lib.blur.api.BlurApi;
import com.fanwe.lib.blur.api.FBlur;
import com.fanwe.lib.blur.api.target.BlurTarget;

import java.lang.ref.WeakReference;

public class FBlurView extends View implements BlurView
{
    public FBlurView(Context context)
    {
        super(context);
    }

    public FBlurView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public FBlurView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    private BlurApi mBlurApi;
    private WeakReference<View> mTarget;

    /**
     * 返回设置的要模糊的view
     *
     * @return
     */
    public final View getTarget()
    {
        return mTarget == null ? null : mTarget.get();
    }

    /**
     * 设置要模糊的view
     *
     * @param target
     */
    public final void setTarget(View target)
    {
        final View old = getTarget();
        if (old != target)
        {
            if (old != null)
            {
                final ViewTreeObserver observer = old.getViewTreeObserver();
                if (observer.isAlive())
                    observer.removeOnPreDrawListener(mOnPreDrawListener);
            }

            mTarget = target == null ? null : new WeakReference<>(target);

            if (target != null)
            {
                final ViewTreeObserver observer = target.getViewTreeObserver();
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

    private BlurApi getBlurApi()
    {
        if (mBlurApi == null)
        {
            mBlurApi = FBlur.with(getContext())
                    .keepDownSamplingSize(true)
                    .destroyAfterBlur(false);
        }
        return mBlurApi;
    }

    @Override
    public final void setBlurRadius(int radius)
    {
        getBlurApi().radius(radius);
    }

    @Override
    public final void setBlurDownSampling(int downSampling)
    {
        getBlurApi().downSampling(downSampling);
    }

    @Override
    public final void setBlurColor(int color)
    {
        getBlurApi().color(color);
    }

    @Override
    public void blur()
    {
        if (mBitmapBlurred == null)
            getBlurApi().blur(getTarget()).into(mBlurTarget);
    }

    private final BlurTarget mBlurTarget = new BlurTarget()
    {
        @Override
        public void onBlur(Bitmap bitmap)
        {
            if (bitmap == null)
                return;
            if (getTarget() == null)
                return;

            mBitmapBlurred = bitmap;
            invalidate();
        }
    };

    private Bitmap mBitmapBlurred;

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        final View target = getTarget();
        if (target == null)
            return;

        if (mBitmapBlurred != null)
        {
            final int scale = getBlurApi().config().getDownSampling();

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
        if (mBlurApi != null)
        {
            mBlurApi.destroy();
            mBlurApi = null;
        }
    }
}
