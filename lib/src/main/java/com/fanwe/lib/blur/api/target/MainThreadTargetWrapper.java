package com.fanwe.lib.blur.api.target;

import android.graphics.Bitmap;

public class MainThreadTargetWrapper extends MainThreadTarget
{
    private final BlurTarget mTarget;

    public MainThreadTargetWrapper(BlurTarget target)
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
