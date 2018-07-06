package com.fanwe.lib.blur.api;

import android.graphics.Bitmap;
import android.view.View;

import com.fanwe.lib.blur.Blur;

import java.lang.ref.WeakReference;

public final class ViewBlurry extends BaseBlurry<View, ViewBlurry>
{
    private WeakReference<View> mView;

    public ViewBlurry(View source, Blur blur)
    {
        super(source, blur);
        mView = new WeakReference<>(source);
    }

    private View getView()
    {
        return mView == null ? null : mView.get();
    }

    @Override
    public Bitmap blur()
    {
        return getBlur().blur(getView());
    }
}
