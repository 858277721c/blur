package com.fanwe.lib.blur.api.target;

import android.view.View;

import java.lang.ref.WeakReference;

public abstract class ViewTarget<T extends View> implements BlurryTarget
{
    private final WeakReference<T> mView;

    public ViewTarget(T view)
    {
        mView = new WeakReference<>(view);
    }

    protected final T getView()
    {
        return mView == null ? null : mView.get();
    }
}
