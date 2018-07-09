package com.fanwe.lib.blur.api;

import android.graphics.Bitmap;
import android.view.View;

import com.fanwe.lib.blur.core.Blur;

import java.lang.ref.WeakReference;

class ViewInvoker extends BlurInvoker<View>
{
    private final WeakReference<View> mView;

    ViewInvoker(View source, Blur blur)
    {
        super(source, blur);
        mView = new WeakReference<>(source);
    }

    private View getView()
    {
        return mView == null ? null : mView.get();
    }

    @Override
    public Bitmap blurSource()
    {
        return getBlur().blur(getView());
    }
}
