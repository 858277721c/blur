package com.fanwe.lib.blur.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

class RenderScriptBlur extends BaseBlur
{
    private final Context mContext;

    private RenderScript mRenderScript;
    private ScriptIntrinsicBlur mBlurScript;

    private Allocation mAllocationInput;
    private Allocation mAllocationOutput;

    public RenderScriptBlur(Context context)
    {
        mContext = context.getApplicationContext();
    }

    @Override
    public void setRadius(int radius)
    {
        super.setRadius(radius);
        if (mBlurScript != null)
            mBlurScript.setRadius(radius);
    }

    private RenderScript getRenderScript()
    {
        if (mRenderScript == null)
            mRenderScript = RenderScript.create(mContext);
        return mRenderScript;
    }

    private ScriptIntrinsicBlur getBlurScript()
    {
        if (mBlurScript == null)
        {
            final RenderScript renderScript = getRenderScript();
            mBlurScript = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
            mBlurScript.setRadius(getRadius());
        }
        return mBlurScript;
    }

    @Override
    protected boolean isConfigurationChanged(int width, int height)
    {
        return super.isConfigurationChanged(width, height)
                || mRenderScript == null || mBlurScript == null;
    }

    @Override
    protected void onConfigurationChanged(int scaledWidth, int scaledHeight, float scale, Bitmap bitmapInput, Bitmap bitmapOutput)
    {
        super.onConfigurationChanged(scaledWidth, scaledHeight, scale, bitmapInput, bitmapOutput);
        mAllocationInput = Allocation.createFromBitmap(getRenderScript(), bitmapInput, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        mAllocationOutput = Allocation.createTyped(getRenderScript(), mAllocationInput.getType());
    }

    @Override
    protected void onBlurImplemention(Bitmap bitmapInput, Bitmap bitmapOutput)
    {
        mAllocationInput.copyFrom(bitmapInput);
        getBlurScript().setInput(mAllocationInput);
        getBlurScript().forEach(mAllocationOutput);
        mAllocationOutput.copyTo(bitmapOutput);
    }

    @Override
    public void destroy()
    {
        super.destroy();
        if (mRenderScript != null)
        {
            mRenderScript.destroy();
            mRenderScript = null;
        }
        if (mBlurScript != null)
        {
            mBlurScript.destroy();
            mBlurScript = null;
        }
    }
}
