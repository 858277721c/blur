package com.fanwe.lib.blur.extend;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
    private final Blur mBlur;
    private WeakReference<T> mView;

    private ExecutorService mExecutorService;
    private Future mFuture;
    private Runnable mRunnable;

    private Drawable mDrawable;

    public ViewBlur(Blur blur)
    {
        mBlur = blur;
    }

    private T getView()
    {
        final T view = mView == null ? null : mView.get();
        if (view == null)
        {
            destroy();
            mRunnable = null;
        }
        return view;
    }

    public final void setView(T view)
    {
        final T old = getView();
        if (old != view)
        {
            mRunnable = null;

            if (old != null)
            {
                final ViewTreeObserver observer = old.getViewTreeObserver();
                if (observer.isAlive())
                    observer.removeOnPreDrawListener(mOnPreDrawListener);
                old.removeOnAttachStateChangeListener(mOnAttachStateChangeListener);
            }

            mView = view == null ? null : new WeakReference<>(view);

            if (view != null)
            {
                final ViewTreeObserver observer = view.getViewTreeObserver();
                if (observer.isAlive())
                    observer.addOnPreDrawListener(mOnPreDrawListener);
                view.addOnAttachStateChangeListener(mOnAttachStateChangeListener);
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
            {
                final Drawable drawable = getDrawable(view);
                setDrawable(drawable);
            }
            return true;
        }
    };

    protected abstract Drawable getDrawable(T view);

    protected abstract void onBlur(BlurredBitmapDrawable drawable, T view);

    private void setDrawable(Drawable drawable)
    {
        if (mDrawable != drawable)
        {
            mDrawable = drawable;
            if (!(drawable instanceof BlurredBitmapDrawable))
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

    private final View.OnAttachStateChangeListener mOnAttachStateChangeListener = new View.OnAttachStateChangeListener()
    {
        @Override
        public void onViewAttachedToWindow(View v)
        {
            if (mRunnable != null)
                submit(mRunnable);
        }

        @Override
        public void onViewDetachedFromWindow(View v)
        {
            destroy();
        }
    };

    private void destroy()
    {
        if (mBlur != null)
            mBlur.destroy();

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

    private Handler mHandler;

    private Handler getHandler()
    {
        if (mHandler == null)
            mHandler = new Handler(Looper.getMainLooper());
        return mHandler;
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
            if (getView() == null)
                return;

            final Bitmap bitmap = drawableToBitmap(mDrawable);
            final Bitmap bitmapBlurred = mBlur.blur(bitmap);
            bitmap.recycle();

            getHandler().post(new Runnable()
            {
                @Override
                public void run()
                {
                    final T view = getView();
                    if (view != null)
                        onBlur(new BlurredBitmapDrawable(view.getResources(), bitmapBlurred), view);
                    mRunnable = null;
                }
            });
        }
    }

    public static final class BlurredBitmapDrawable extends BitmapDrawable
    {
        private BlurredBitmapDrawable(Resources res, Bitmap bitmap)
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
