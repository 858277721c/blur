package com.fanwe.lib.blur.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import com.fanwe.lib.blur.api.BlurApi;
import com.fanwe.lib.blur.api.BlurApiFactory;
import com.fanwe.lib.blur.api.BlurInvoker;
import com.fanwe.lib.blur.api.target.BlurTarget;

import java.lang.ref.WeakReference;

public abstract class BlurViewWrapper<T extends View> implements BlurView
{
    private final BlurApi mBlurApi;
    private BlurInvoker mBlurInvoker;
    private boolean mBlurAsync;

    private WeakReference<T> mView;
    private Drawable mViewDrawable;

    public BlurViewWrapper(Context context)
    {
        mBlurApi = BlurApiFactory.create(context);
        mBlurApi.destroyAfterBlur(false);
    }

    @Override
    public void setBlurRadius(int radius)
    {
        mBlurApi.radius(radius);
    }

    @Override
    public void setBlurDownSampling(int downSampling)
    {
        mBlurApi.downSampling(downSampling);
    }

    @Override
    public void setBlurColor(int color)
    {
        mBlurApi.color(color);
    }

    @Override
    public void setBlurAsync(boolean async)
    {
        mBlurAsync = async;
    }

    @Override
    public void blur()
    {
    }

    private T getView()
    {
        final T view = mView == null ? null : mView.get();
        if (view == null)
            destroy();
        return view;
    }

    /**
     * 设置view后对象会被{@link ViewTreeObserver}持有，如果需要释放，可以调用{@link #setView(View)}设置为null来释放当前对象
     *
     * @param view
     */
    public final void setView(T view)
    {
        final T old = getView();
        if (old != view)
        {
            if (old != null)
            {
                final ViewTreeObserver observer = old.getViewTreeObserver();
                if (observer.isAlive())
                    observer.removeOnPreDrawListener(mOnPreDrawListener);
            }

            mView = view == null ? null : new WeakReference<>(view);

            if (view != null)
            {
                final ViewTreeObserver observer = view.getViewTreeObserver();
                if (observer.isAlive())
                    observer.addOnPreDrawListener(mOnPreDrawListener);
            } else
            {
                destroy();
            }
        }
    }

    private final ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener()
    {
        @Override
        public boolean onPreDraw()
        {
            final T view = getView();
            if (view != null)
                setViewDrawable(getViewDrawable(view));
            return true;
        }
    };

    protected abstract Drawable getViewDrawable(T view);

    protected abstract void onDrawableBlurred(BlurredBitmapDrawable drawable, T view);

    private void setViewDrawable(Drawable drawable)
    {
        if (mViewDrawable != drawable)
        {
            mViewDrawable = drawable;
            if (drawable != null && !(drawable instanceof BlurredBitmapDrawable))
                blurDrawable(drawable);
        }
    }

    private void blurDrawable(final Drawable drawable)
    {
        if (drawable == null)
            return;

        if (isAttachedToWindow(getView()))
        {
            if (mBlurInvoker != null)
                mBlurInvoker.cancelAsync();

            mBlurInvoker = mBlurApi.blur(drawable).async(mBlurAsync).into(new BlurTarget()
            {
                @Override
                public void onBlur(Bitmap bitmap)
                {
                    if (bitmap == null)
                        return;

                    final T view = getView();
                    if (view == null)
                        return;

                    onDrawableBlurred(new BlurredBitmapDrawable(view.getResources(), bitmap), view);
                }
            });
        }
    }

    private void destroy()
    {
        mViewDrawable = null;

        mBlurApi.destroy();
        if (mBlurInvoker != null)
            mBlurInvoker.cancelAsync();
    }

    public static final class BlurredBitmapDrawable extends BitmapDrawable
    {
        private BlurredBitmapDrawable(Resources res, Bitmap bitmap)
        {
            super(res, bitmap);
        }
    }

    private static boolean isAttachedToWindow(View view)
    {
        if (view == null)
            return false;

        if (Build.VERSION.SDK_INT >= 19)
            return view.isAttachedToWindow();
        else
            return view.getWindowToken() != null;
    }
}
