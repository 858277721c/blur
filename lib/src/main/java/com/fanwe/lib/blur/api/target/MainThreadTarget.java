package com.fanwe.lib.blur.api.target;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

public abstract class MainThreadTarget implements BlurTarget
{
    private Handler mHandler;

    private Handler getHandler()
    {
        if (mHandler == null)
            mHandler = new Handler(Looper.getMainLooper());
        return mHandler;
    }

    @Override
    public final void onBlur(final Bitmap bitmap)
    {
        if (Looper.myLooper() == Looper.getMainLooper())
        {
            onBlurMainThread(bitmap);
        } else
        {
            getHandler().post(new Runnable()
            {
                @Override
                public void run()
                {
                    onBlurMainThread(bitmap);
                }
            });
        }
    }

    public abstract void onBlurMainThread(Bitmap bitmap);
}
