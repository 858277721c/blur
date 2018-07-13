package com.fanwe.lib.blur.viewblur;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import com.fanwe.lib.blur.api.BlurApi;
import com.fanwe.lib.blur.api.BlurApiFactory;

import java.lang.ref.WeakReference;

abstract class ViewBlur<T extends View>
{
    private final Context mContext;
    private BlurApi mBlurApi;
    private WeakReference<T> mTarget;

    public ViewBlur(Context context)
    {
        mContext = context.getApplicationContext();
    }

    public final BlurApi getBlurApi()
    {
        if (mBlurApi == null)
        {
            mBlurApi = BlurApiFactory.create(mContext);
            mBlurApi.setDestroyAfterBlur(false);
        }
        return mBlurApi;
    }

    public final T getTarget()
    {
        return mTarget == null ? null : mTarget.get();
    }

    public final void setTarget(T target)
    {
        final T old = getTarget();
        if (old != target)
        {
            if (old != null)
            {
                final ViewTreeObserver observer = old.getViewTreeObserver();
                if (observer.isAlive())
                    observer.removeOnPreDrawListener(mOnPreDrawListener);

                old.removeOnAttachStateChangeListener(mOnAttachStateChangeListener);
            }

            mTarget = target == null ? null : new WeakReference<>(target);

            if (target != null)
            {
                final ViewTreeObserver observer = target.getViewTreeObserver();
                if (observer.isAlive())
                    observer.addOnPreDrawListener(mOnPreDrawListener);

                target.addOnAttachStateChangeListener(mOnAttachStateChangeListener);
                onUpdate(target);
            } else
            {
                if (mBlurApi != null)
                    mBlurApi.destroy();
            }
        }
    }

    private final ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener()
    {
        @Override
        public boolean onPreDraw()
        {
            final T target = getTarget();
            if (target != null)
            {
                if (isAttachedToWindow(target))
                    onUpdate(target);
            }
            return true;
        }
    };

    private final View.OnAttachStateChangeListener mOnAttachStateChangeListener = new View.OnAttachStateChangeListener()
    {
        @Override
        public void onViewAttachedToWindow(View v)
        {
            onUpdate(getTarget());
        }

        @Override
        public void onViewDetachedFromWindow(View v)
        {
            if (mBlurApi != null)
                mBlurApi.destroy();
        }
    };

    protected abstract void onUpdate(T target);

    private static boolean isAttachedToWindow(View view)
    {
        if (Build.VERSION.SDK_INT >= 19)
            return view.isAttachedToWindow();
        else
            return view.getWindowToken() != null;
    }
}
