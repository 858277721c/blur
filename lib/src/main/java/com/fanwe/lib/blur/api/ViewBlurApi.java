package com.fanwe.lib.blur.api;

import android.graphics.Bitmap;
import android.view.View;

import com.fanwe.lib.blur.core.Blur;

import java.lang.ref.WeakReference;

class ViewBlurApi extends BlurApi<View>
{
    private final WeakReference<View> mView;

    ViewBlurApi(View source, boolean async, Blur blur)
    {
        super(source, async, blur);
        mView = new WeakReference<>(source);
    }

    private View getView()
    {
        return mView == null ? null : mView.get();
    }

    @Override
    public Bitmap blurImplemention()
    {
        return getBlur().blur(getView());
    }
}
