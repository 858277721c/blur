package com.fanwe.lib.blur.api.target;

import android.graphics.Bitmap;

public interface BlurTarget
{
    /**
     * 模糊回调
     *
     * @param bitmap 模糊后的Bitamp，可能为null
     */
    void onBlurred(Bitmap bitmap);
}
