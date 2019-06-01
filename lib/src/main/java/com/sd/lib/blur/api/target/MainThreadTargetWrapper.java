package com.sd.lib.blur.api.target;

import android.graphics.Bitmap;

import com.sd.lib.blur.api.BlurApi;

public final class MainThreadTargetWrapper extends MainThreadTarget
{
    private final BlurApi.Target mTarget;

    public MainThreadTargetWrapper(BlurApi.Target target)
    {
        if (target == null)
            throw new IllegalArgumentException("target is null when create MainThreadTargetWrapper");
        mTarget = target;
    }

    @Override
    public void onBlurredMainThread(Bitmap bitmap)
    {
        mTarget.onBlurred(bitmap);
    }
}
