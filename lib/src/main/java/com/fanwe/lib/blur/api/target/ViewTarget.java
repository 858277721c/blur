package com.fanwe.lib.blur.api.target;

import android.graphics.Bitmap;
import android.view.View;

import java.lang.ref.WeakReference;

public abstract class ViewTarget<T extends View> implements BlurTarget
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

    @Override
    public final void onBlur(Bitmap bitmap)
    {
        onBlur(bitmap, getView());
    }

    public abstract void onBlur(Bitmap bitmap, T view);
}