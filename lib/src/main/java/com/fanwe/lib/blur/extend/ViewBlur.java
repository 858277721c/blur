package com.fanwe.lib.blur.extend;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import com.fanwe.lib.blur.core.Blur;
import com.fanwe.lib.task.FTask;

import java.lang.ref.WeakReference;

public abstract class ViewBlur<T extends View>
{
    private final Blur mBlur;
    private WeakReference<T> mView;

    private FTask mBlurTask;
    private Drawable mViewDrawable;

    public ViewBlur(Blur blur)
    {
        mBlur = blur;
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
            {
                final Drawable viewDrawable = getViewDrawable(view);
                setViewDrawable(viewDrawable);
            }
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
                submit(drawable);
        }
    }

    private void submit(final Drawable drawable)
    {
        if (isAttachedToWindow(getView()))
        {
            if (mBlurTask != null)
                mBlurTask.cancel(true);

            mBlurTask = new FTask()
            {
                @Override
                protected void onRun() throws Exception
                {
                    if (getView() == null)
                        return;

                    final Bitmap bitmap = drawableToBitmap(drawable);
                    final Bitmap bitmapBlurred = mBlur.blur(bitmap);

                    runOnUiThread(new Runnable()
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
            };
            mBlurTask.submit();
        }
    }

    private void destroy()
    {
        mViewDrawable = null;

        if (mBlur != null)
            mBlur.destroy();

        if (mBlurTask != null)
        {
            mBlurTask.cancel(true);
            mBlurTask = null;
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
