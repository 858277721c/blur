package com.fanwe.lib.blur.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import com.fanwe.lib.blur.api.BlurApi;
import com.fanwe.lib.blur.api.BlurApiFactory;
import com.fanwe.lib.blur.api.target.BlurTarget;

import java.lang.ref.WeakReference;

public class FBlurView extends View implements BlurView
{
    private BlurApi mBlurApi;
    private WeakReference<View> mBlurTarget;

    private Bitmap mBitmapBlurred;
    private boolean mIsDrawingBlur;
    private boolean mIsAttachedToWindow;

    public FBlurView(Context context)
    {
        this(context, null);
    }

    public FBlurView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        final BlurViewAttrs viewAttrs = BlurViewAttrs.parse(context, attrs);
        setBlurRadius(viewAttrs.getBlurRadius());
        setBlurDownSampling(viewAttrs.getBlurDownSampling());
        setBlurColor(viewAttrs.getBlurColor());
        setBlurAsync(viewAttrs.isBlurAsync());
    }

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
     * @param target
     */
    public final void setBlurTarget(View target)
    {
        final View old = getBlurTarget();
        if (old != target)
        {
            if (old != null)
            {
                final ViewTreeObserver observer = old.getViewTreeObserver();
                if (observer.isAlive())
                    observer.removeOnPreDrawListener(mOnPreDrawListener);
            }

            mBlurTarget = target == null ? null : new WeakReference<>(target);

            if (target != null)
            {
                final ViewTreeObserver observer = target.getViewTreeObserver();
                if (observer.isAlive())
                    observer.addOnPreDrawListener(mOnPreDrawListener);

                blur();
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
            mBlurApi = BlurApiFactory.create(getContext());
            mBlurApi.destroyAfterBlur(false);
            mBlurApi.keepDownSamplingSize(true);
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
    public void setBlurAsync(boolean async)
    {
        getBlurApi().async(async);
    }

    @Override
    public void blur()
    {
        if (mIsDrawingBlur)
            return;

        if (mIsAttachedToWindow)
            getBlurApi().blur(getBlurTarget()).into(mInvokeTarget);
    }

    private final BlurTarget mInvokeTarget = new BlurTarget()
    {
        @Override
        public void onBlurred(Bitmap bitmap)
        {
            if (bitmap == null)
                return;

            mBitmapBlurred = bitmap;
            mIsDrawingBlur = true;
            invalidate();
        }
    };

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        final View target = getBlurTarget();
        if (target == null)
            return;

        if (mBitmapBlurred == null || mBitmapBlurred.isRecycled())
            return;

        final int scale = getBlurApi().settings().getDownSampling();

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

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        mIsAttachedToWindow = true;
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        mIsAttachedToWindow = false;
        if (mBlurApi != null)
            mBlurApi.destroy();
    }
}
