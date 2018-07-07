package com.fanwe.lib.blur.core;

import android.graphics.Bitmap;
import android.view.View;

public interface Blur extends BlurConfig
{
    /**
     * 得到View的模糊Bitmap
     *
     * @param view
     * @return
     */
    Bitmap blur(View view);

    /**
     * 模糊Bitmap，传入的对象不会被回收
     *
     * @param bitmap
     * @return
     */
    Bitmap blur(Bitmap bitmap);

    /**
     * 释放资源，调用此方法后依旧可以使用此对象
     */
    void destroy();
}
