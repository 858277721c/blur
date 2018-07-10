package com.fanwe.lib.blur.api;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

/**
 * 注意：每次执行模糊操作{@link #into(ImageView)},{@link #intoBackground(View)},{@link #into(Target)}之前，都会先调用{@link #cancelAsync()}方法尝试取消已经发起的子线程任务
 */
public interface BlurInvoker
{
    /**
     * 设置是否在子线程执行
     *
     * @param async
     * @return
     */
    BlurInvoker async(boolean async);

    /**
     * 模糊后设置给ImageView
     *
     * @param imageView
     * @return
     */
    BlurInvoker into(ImageView imageView);

    /**
     * 模糊后设置给view的背景
     *
     * @param view
     * @return
     */
    BlurInvoker intoBackground(View view);

    /**
     * 模糊后设置给某个目标
     *
     * @param target
     * @return
     */
    BlurInvoker into(Target target);

    /**
     * 取消子线程任务
     *
     * @return
     */
    BlurInvoker cancelAsync();

    interface Target
    {
        /**
         * 模糊回调
         *
         * @param bitmap 模糊后的Bitamp，可能为null
         */
        void onBlurred(Bitmap bitmap);
    }
}
