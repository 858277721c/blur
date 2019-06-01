package com.sd.lib.blur.core.strategy;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

class RenderScriptStrategy extends BaseStrategy
{
    private final Context mContext;

    private RenderScript mRenderScript;
    private ScriptIntrinsicBlur mBlurScript;

    public RenderScriptStrategy(Context context)
    {
        mContext = context.getApplicationContext();
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
        }
        return mBlurScript;
    }

    @Override
    public void blur(int radius, Bitmap bitmapInput, Bitmap bitmapOutput)
    {
        final ScriptIntrinsicBlur blurScript = getBlurScript();
        final RenderScript renderScript = getRenderScript();

        final Allocation allocationInput = Allocation.createFromBitmap(renderScript, bitmapInput, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        final Allocation allocationOutput = Allocation.createTyped(renderScript, allocationInput.getType());

        blurScript.setRadius(radius);
        allocationInput.copyFrom(bitmapInput);
        blurScript.setInput(allocationInput);
        blurScript.forEach(allocationOutput);
        allocationOutput.copyTo(bitmapOutput);
    }

    @Override
    public void destroy()
    {
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
