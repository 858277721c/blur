package com.fanwe.lib.blur.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.fanwe.lib.blur.api.BlurApi;
import com.fanwe.lib.blur.api.BlurApiFactory;
import com.fanwe.lib.blur.api.BlurInvoker;

public class FBlurImageView extends ImageView implements BlurView
{
    private final BlurApi mBlurApi;
    private boolean mAsync;

    private Drawable mDrawable;
    private Drawable mBlurredDrawable;

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
            {
                drawBlurredDrawable(canvas);
                blurDrawable(drawable);
            }
        }
    }

    private void drawBlurredDrawable(Canvas canvas)
    {
        final Drawable drawable = mBlurredDrawable;

        if (drawable == null)
            return;

        final int saveCount = canvas.getSaveCount();
        canvas.save();

        final Matrix matrix = getImageMatrix();
        if (matrix == null && getPaddingTop() == 0 && getPaddingLeft() == 0)
        {
            drawable.draw(canvas);
        } else
        {
            if (Build.VERSION.SDK_INT >= 16)
            {
                if (getCropToPadding())
                {
                    final int scrollX = getScrollX();
                    final int scrollY = getScrollY();
                    canvas.clipRect(scrollX + getPaddingLeft(), scrollY + getPaddingTop(),
                            scrollX + getRight() - getLeft() - getPaddingRight(),
                            scrollY + getBottom() - getTop() - getPaddingBottom());
                }
            }

            canvas.translate(getPaddingLeft(), getPaddingTop());

            if (matrix != null)
            {
                canvas.concat(matrix);
            }
            drawable.draw(canvas);
        }

        canvas.restoreToCount(saveCount);
    }

    private void blurDrawable(Drawable drawable)
    {
        if (mDrawable == drawable)
            return;
        if (drawable instanceof BlurredBitmapDrawable)
            throw new IllegalArgumentException("can not blur BlurredBitmapDrawable");

        mDrawable = drawable;

        mBlurApi.blur(drawable).async(mAsync).into(new BlurInvoker.Target()
        {
            @Override
            public void onBlurred(Bitmap bitmap)
            {
                if (bitmap == null)
                    return;

                mBlurredDrawable = new BlurredBitmapDrawable(getResources(), bitmap);
                setImageDrawable(mBlurredDrawable);
            }
        });
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        mBlurApi.destroy();
    }

    private static final class BlurredBitmapDrawable extends BitmapDrawable
    {
        private BlurredBitmapDrawable(Resources res, Bitmap bitmap)
        {
            super(res, bitmap);
        }
    }
}
