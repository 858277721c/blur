package com.fanwe.lib.blur.api;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.fanwe.lib.blur.core.Blur;

public interface BlurApi
{
    /**
     * {@link Blur#setRadius(int)}
     *
     * @param radius
     * @return
     */
    BlurApi radius(int radius);

    /**
     * {@link Blur#setDownSampling(int)}
     *
     * @param downSampling
     * @return
     */
    BlurApi downSampling(int downSampling);

    /**
     * {@link Blur#setColor(int)}
     *
     * @param color
     * @return
     */
    BlurApi color(int color);

    /**
     * {@link Blur#setKeepDownSamplingSize(boolean)}
     *
     * @param keepDownSamplingSize
     * @return
     */
    BlurApi keepDownSamplingSize(boolean keepDownSamplingSize);

    /**
     * {@link Blur#setDestroyAfterBlur(boolean)}
     *
     * @param destroyAfterBlur
     * @return
     */
    BlurApi destroyAfterBlur(boolean destroyAfterBlur);

    /**
     * 是否在子线程执行模糊操作
     *
     * @param async
     * @return
     */
    BlurApi async(boolean async);

    /**
     * 返回{@link Settings}对象，可以查询设置的参数
     *
     * @return
     */
    Settings settings();

    /**
     * {@link Blur#destroy()}
     *
     * @return
     */
    BlurApi destroy();

    /**
     * 模糊bitmap
     *
     * @param bitmap
     * @return
     */
    BlurInvoker blur(Bitmap bitmap);

    /**
     * 模糊view
     *
     * @param view
     * @return
     */
    BlurInvoker blur(View view);

    /**
     * 模糊drawable
     *
     * @param drawable
     * @return
     */
    BlurInvoker blur(Drawable drawable);


    interface Settings
    {
        /**
         * {@link Blur#getRadius()}
         *
         * @return
         */
        int getRadius();

        /**
         * {@link Blur#getDownSampling()}
         *
         * @return
         */
        int getDownSampling();

        /**
         * {@link Blur#getColor()}
         *
         * @return
         */
        int getColor();

        /**
         * {@link Blur#isKeepDownSamplingSize()}
         *
         * @return
         */
        boolean isKeepDownSamplingSize();

        /**
         * {@link Blur#isDestroyAfterBlur()}
         *
         * @return
         */
        boolean isDestroyAfterBlur();
    }
}
