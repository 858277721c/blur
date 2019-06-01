package com.sd.lib.blur.viewblur;

import android.content.Context;
import android.os.Build;
import android.view.View;

import com.sd.lib.blur.api.BlurApi;
import com.sd.lib.blur.api.BlurApiFactory;

import java.lang.ref.WeakReference;

abstract class BaseViewBlur<V extends View> implements ViewBlur<V>
{
    private final Context mContext;
    private BlurApi mBlurApi;

    private WeakReference<V> mTarget;

    public BaseViewBlur(Context context)
    {
        mContext = context.getApplicationContext();
    }

    private BlurApi getBlurApi()
    {
        if (mBlurApi == null)
        {
            mBlurApi = BlurApiFactory.create(mContext);
            mBlurApi.setDestroyAfterBlur(false);
        }
        return mBlurApi;
    }

    private void destroyBlurApi()
    {
        if (mBlurApi != null)
            mBlurApi.destroy();
    }

    @Override
    public final ViewBlur<V> setBlurRadius(int radius)
    {
        getBlurApi().setRadius(radius);
        return this;
    }

    @Override
    public final ViewBlur<V> setBlurDownSampling(int downSampling)
    {
        getBlurApi().setDownSampling(downSampling);
        return this;
    }

    @Override
    public final ViewBlur<V> setBlurColor(int color)
    {
        getBlurApi().setColor(color);
        return this;
    }

    @Override
    public final V getSource()
    {
        return mSourceListener.getView();
    }

    @Override
    public final V getTarget()
    {
        return mTarget == null ? null : mTarget.get();
    }

    @Override
    public final ViewBlur<V> setSource(V source)
    {
        mSourceListener.setView(source);
        return this;
    }

    @Override
    public final ViewBlur<V> setTarget(V target)
    {
        final V old = getTarget();
        if (old != target)
        {
            if (old != null)
                old.removeOnAttachStateChangeListener(mTargetAttachStateChangeListener);

            mTarget = target == null ? null : new WeakReference<>(target);

            if (target != null)
            {
                target.addOnAttachStateChangeListener(mTargetAttachStateChangeListener);
                notifyUpdate();
            } else
            {
                destroyBlurApi();
            }
        }
        return this;
    }

    @Override
    public void release()
    {
        setSource(null);
        setTarget(null);
        destroyBlurApi();
    }

    private final FViewListener<V> mSourceListener = new FViewListener<V>()
    {
        @Override
        protected void onViewChanged(V oldView, V newView)
        {
            super.onViewChanged(oldView, newView);
            if (newView != null)
                notifyUpdate();
            else
                destroyBlurApi();
        }

        @Override
        protected void onUpdate(V view)
        {
            notifyUpdate();
        }
    };

    private final View.OnAttachStateChangeListener mTargetAttachStateChangeListener = new View.OnAttachStateChangeListener()
    {
        @Override
        public void onViewAttachedToWindow(View v)
        {
            notifyUpdate();
        }

        @Override
        public void onViewDetachedFromWindow(View v)
        {
            destroyBlurApi();
        }
    };

    private void notifyUpdate()
    {
        final V source = getSource();
        if (!isAttachedToWindow(source))
            return;

        final V target = getTarget();
        if (!isAttachedToWindow(target))
            return;

        onUpdate(getBlurApi(), source, target);
    }

    protected abstract void onUpdate(BlurApi blurApi, V source, V target);

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
