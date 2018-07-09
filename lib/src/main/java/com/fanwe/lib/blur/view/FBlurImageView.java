package com.fanwe.lib.blur.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.fanwe.lib.blur.api.BlurApi;
import com.fanwe.lib.blur.api.BlurApiFactory;
import com.fanwe.lib.blur.api.BlurInvoker;
import com.fanwe.lib.blur.api.target.BlurTarget;

public class FBlurImageView extends ImageView implements BlurView
{
    private final BlurApi mBlurApi;
    private BlurInvoker mBlurInvoker;
    private boolean mAsync;
    private Drawable mDrawable;

    public FBlurImageView(Context context)
    {
        this(context, null);
    }

    public FBlurImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mBlurApi = BlurApiFactory.create(context);
        mBlurApi.destroyAfterBlur(false);

        final BlurViewAttrs viewAttrs = BlurViewAttrs.parse(context, attrs);
        setBlurRadius(viewAttrs.getRadius());
        setBlurDownSampling(viewAttrs.getDownSampling());
        setBlurColor(viewAttrs.getColor());
        setBlurAsync(viewAttrs.isAsync());
    }

    @Override
    public final void setBlurRadius(int radius)
    {
        mBlurApi.radius(radius);
    }

    @Override
    public final void setBlurDownSampling(int downSampling)
    {
        mBlurApi.downSampling(downSampling);
    }

    @Override
    public final void setBlurColor(int color)
    {
        mBlurApi.color(color);
    }

    @Override
    public void setBlurAsync(boolean async)
    {
        mAsync = async;
    }

    @Override
    public void blur()
    {

    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        final Drawable drawable = getDrawable();
        if (drawable == null)
        {
            super.onDraw(canvas);
        } else
        {
            if (drawable instanceof BlurredBitmapDrawable)
                super.onDraw(canvas);
            else
                blurDrawable(drawable);
        }
    }

    private void blurDrawable(Drawable drawable)
    {
        if (mDrawable == drawable)
            return;
        if (drawable instanceof BlurredBitmapDrawable)
            return;

        mDrawable = drawable;

        if (mBlurInvoker != null)
            mBlurInvoker.cancelAsync();

        mBlurInvoker = mBlurApi.blur(drawable).async(mAsync).into(new BlurTarget()
        {
            @Override
            public void onBlur(Bitmap bitmap)
            {
                if (bitmap != null)
                    setImageDrawable(new BlurredBitmapDrawable(getResources(), bitmap));
            }
        });
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        mDrawable = null;
        mBlurApi.destroy();
        if (mBlurInvoker != null)
            mBlurInvoker.cancelAsync();
    }

    private static final class BlurredBitmapDrawable extends BitmapDrawable
    {
        private BlurredBitmapDrawable(Resources res, Bitmap bitmap)
        {
            super(res, bitmap);
        }
    }
}
