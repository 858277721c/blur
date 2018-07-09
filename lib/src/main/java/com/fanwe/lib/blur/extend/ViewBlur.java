package com.fanwe.lib.blur.extend;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewTreeObserver;

import com.fanwe.lib.blur.core.Blur;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class ViewBlur<T extends View>
{
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

    private final Blur mBlur;
    private WeakReference<T> mView;

    private Future mFuture;
    private Drawable mViewDrawable;
    private Handler mHandler;

    public ViewBlur(Blur blur)
    {
        mBlur = blur;
    }

    private Handler getHandler()
    {
        if (mHandler == null)
            mHandler = new Handler(Looper.getMainLooper());
        return mHandler;
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

    protected abstract void onBlur(BlurredBitmapDrawable drawable, T view);

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
        if (isAttachedToWindow(getView()))
        {
            if (mFuture != null)
                mFuture.cancel(true);

            mFuture = EXECUTOR_SERVICE.submit(new Runnable()
            {
                @Override
                public void run()
                {
                    if (getView() == null)
                        return;

                    final Bitmap bitmapBlurred = mBlur.blur(drawable);
                    getHandler().post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            final T view = getView();
                            if (view != null)
                                onBlur(new BlurredBitmapDrawable(view.getResources(), bitmapBlurred), view);
                        }
                    });
                }
            });
        }
    }

    private void destroy()
    {
        mViewDrawable = null;

        if (mBlur != null)
            mBlur.destroy();

        if (mFuture != null)
        {
            mFuture.cancel(true);
            mFuture = null;
        }
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
