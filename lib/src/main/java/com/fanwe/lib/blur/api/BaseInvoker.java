package com.fanwe.lib.blur.api;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.fanwe.lib.blur.api.target.BackgroundTarget;
import com.fanwe.lib.blur.api.target.ImageViewTarget;
import com.fanwe.lib.blur.api.target.MainThreadTargetWrapper;

abstract class BaseInvoker<S> implements BlurInvoker
{
    private final boolean mAsync;

    public BaseInvoker(S source, boolean async)
    {
        mAsync = async;
    }

    @Override
    public final BlurInvoker into(ImageView imageView)
    {
        if (imageView != null)
            into(new ImageViewTarget(imageView));
        return this;
    }

    @Override
    public final BlurInvoker intoBackground(View view)
    {
        if (view != null)
            into(new BackgroundTarget(view));
        return this;
    }

    @Override
    public final BlurInvoker into(Target target)
    {
        if (target != null)
        {
            target = new MainThreadTargetWrapper(target);
            notifyTarget(target);
        }
        return this;
    }

    protected final boolean isAsync()
    {
        return mAsync;
    }

    protected abstract void notifyTarget(Target target);

    protected abstract Bitmap blurSource();
}
