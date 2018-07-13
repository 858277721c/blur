package com.fanwe.lib.blur.viewblur;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import com.fanwe.lib.blur.api.BlurApi;
import com.fanwe.lib.blur.api.BlurApiFactory;

import java.lang.ref.WeakReference;

abstract class BaseViewBlur<S extends View> implements ViewBlur<S>
{
    private final Context mContext;
    private BlurApi mBlurApi;
    private WeakReference<S> mSource;

    public BaseViewBlur(Context context)
    {
        mContext = context.getApplicationContext();
    }

    @Override
    public final void setRadius(int radius)
    {
        getBlurApi().setRadius(radius);
    }

    @Override
    public final void setDownSampling(int downSampling)
    {
        getBlurApi().setDownSampling(downSampling);
    }

    @Override
    public final void setColor(int color)
    {
        getBlurApi().setColor(color);
    }

    protected final BlurApi getBlurApi()
    {
        if (mBlurApi == null)
        {
            mBlurApi = BlurApiFactory.create(mContext);
            mBlurApi.setDestroyAfterBlur(false);
        }
        return mBlurApi;
    }

    @Override
    public final S getSource()
    {
        return mSource == null ? null : mSource.get();
    }

    @Override
    public final void setSource(S source)
    {
        final S old = getSource();
        if (old != source)
        {
            if (old != null)
            {
                final ViewTreeObserver observer = old.getViewTreeObserver();
                if (observer.isAlive())
                    observer.removeOnPreDrawListener(mOnPreDrawListener);

                old.removeOnAttachStateChangeListener(mOnAttachStateChangeListener);
            }

            mSource = source == null ? null : new WeakReference<>(source);

            if (source != null)
            {
                final ViewTreeObserver observer = source.getViewTreeObserver();
                if (observer.isAlive())
                    observer.addOnPreDrawListener(mOnPreDrawListener);

                source.addOnAttachStateChangeListener(mOnAttachStateChangeListener);
                onUpdate(source);
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
            final S source = getSource();
            if (source != null)
            {
                if (isAttachedToWindow(source))
                    onUpdate(source);
            }
            return true;
        }
    };

    private final View.OnAttachStateChangeListener mOnAttachStateChangeListener = new View.OnAttachStateChangeListener()
    {
        @Override
        public void onViewAttachedToWindow(View v)
        {
            onUpdate(getSource());
        }

        @Override
        public void onViewDetachedFromWindow(View v)
        {
            if (mBlurApi != null)
                mBlurApi.destroy();
        }
    };

    protected abstract void onUpdate(S source);

    private static boolean isAttachedToWindow(View view)
    {
        if (Build.VERSION.SDK_INT >= 19)
            return view.isAttachedToWindow();
        else
            return view.getWindowToken() != null;
    }
}
