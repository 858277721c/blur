package com.fanwe.lib.blur.api.target;

import android.graphics.Bitmap;
import android.view.View;

import com.fanwe.lib.blur.api.BlurInvoker;

import java.lang.ref.WeakReference;

public abstract class ViewTarget<T extends View> implements BlurInvoker.Target
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
    public final void onBlurred(Bitmap bitmap)
    {
        onBlurred(bitmap, getView());
    }

    public abstract void onBlurred(Bitmap bitmap, T view);
}
