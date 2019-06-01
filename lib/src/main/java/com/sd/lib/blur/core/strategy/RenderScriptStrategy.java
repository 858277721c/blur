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

    private void init()
    {
        if (mRenderScript == null)
            mRenderScript = RenderScript.create(mContext);

        if (mBlurScript == null)
            mBlurScript = ScriptIntrinsicBlur.create(mRenderScript, Element.U8_4(mRenderScript));
    }

    @Override
    public void blur(int radius, Bitmap bitmapInput, Bitmap bitmapOutput)
    {
        init();

        final Allocation allocationInput = Allocation.createFromBitmap(mRenderScript, bitmapInput, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        final Allocation allocationOutput = Allocation.createTyped(mRenderScript, allocationInput.getType());

        mBlurScript.setRadius(radius);
        allocationInput.copyFrom(bitmapInput);
        mBlurScript.setInput(allocationInput);
        mBlurScript.forEach(allocationOutput);
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
