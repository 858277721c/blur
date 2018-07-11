package com.fanwe.lib.blur.api;

import android.view.View;
import android.widget.ImageView;

import com.fanwe.lib.blur.api.target.BackgroundTarget;
import com.fanwe.lib.blur.api.target.BlurTarget;
import com.fanwe.lib.blur.api.target.ImageViewTarget;
import com.fanwe.lib.blur.api.target.MainThreadTargetWrapper;

abstract class BaseInvoker implements BlurApi.Invoker
{
    private final boolean mAsync;

    public BaseInvoker(boolean async)
    {
        mAsync = async;
    }

    @Override
    public final BlurApi.Invoker into(ImageView imageView)
    {
        if (imageView != null)
            into(new ImageViewTarget(imageView));
        return this;
    }

    @Override
    public final BlurApi.Invoker intoBackground(View view)
    {
        if (view != null)
            into(new BackgroundTarget(view));
        return this;
    }

    @Override
    public final BlurApi.Invoker into(BlurTarget target)
    {
        if (target != null)
        {
            target = new MainThreadTargetWrapper(target);
            notifyTarget(target, mAsync);
        }
        return this;
    }

    protected abstract void notifyTarget(BlurTarget target, boolean async);
}
