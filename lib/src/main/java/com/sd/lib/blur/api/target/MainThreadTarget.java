package com.sd.lib.blur.api.target;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import com.sd.lib.blur.api.BlurApi;

public abstract class MainThreadTarget implements BlurApi.Target
{
    private Handler mHandler;

    private Handler getHandler()
    {
        if (mHandler == null)
            mHandler = new Handler(Looper.getMainLooper());
        return mHandler;
    }

    @Override
    public final void onBlurred(final Bitmap bitmap)
    {
        if (Looper.myLooper() == Looper.getMainLooper())
        {
            onBlurredMainThread(bitmap);
        } else
        {
            getHandler().post(new Runnable()
            {
                @Override
                public void run()
                {
                    onBlurredMainThread(bitmap);
                }
            });
        }
    }

    public abstract void onBlurredMainThread(Bitmap bitmap);
}
