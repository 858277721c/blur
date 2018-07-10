package com.fanwe.lib.blur.api.target;

import android.graphics.Bitmap;

import com.fanwe.lib.blur.api.BlurInvoker;

public class MainThreadTargetWrapper extends MainThreadTarget
{
    private final BlurInvoker.Target mTarget;

    public MainThreadTargetWrapper(BlurInvoker.Target target)
    {
        if (target == null)
            throw new NullPointerException("target is null");
        mTarget = target;
    }

    @Override
    public void onBlurredMainThread(Bitmap bitmap)
    {
        mTarget.onBlurred(bitmap);
    }
}
