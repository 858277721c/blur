package com.sd.lib.blur.api.target;

import android.graphics.Bitmap;
import android.view.View;

import com.sd.lib.blur.api.BlurApi;

import java.lang.ref.WeakReference;

public abstract class ViewTarget<T extends View> implements BlurApi.Target
{
    private final WeakReference<T> mView;

    public ViewTarget(T view)
    {
        if (view == null)
            throw new IllegalArgumentException("view is null when create " + getClass().getSimpleName());
        mView = new WeakReference<>(view);
    }

    protected final T getView()
    {
        return mView == null ? null : mView.get();
    }

    @Override
    public final void onBlurred(Bitmap bitmap)
    {
        final T view = getView();
        if (view != null)
            onBlurred(bitmap, view);
    }

    public abstract void onBlurred(Bitmap bitmap, T view);
}
