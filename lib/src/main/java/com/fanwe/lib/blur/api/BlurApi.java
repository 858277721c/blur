package com.fanwe.lib.blur.api;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.fanwe.lib.blur.api.target.BlurTarget;
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
    Invoker blur(Bitmap bitmap);

    /**
     * 模糊view
     *
     * @param view
     * @return
     */
    Invoker blur(View view);

    /**
     * 模糊drawable
     *
     * @param drawable
     * @return
     */
    Invoker blur(Drawable drawable);


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

    interface Invoker
    {
        /**
         * 模糊后设置给ImageView
         *
         * @param imageView
         * @return
         */
        Invoker into(ImageView imageView);

        /**
         * 模糊后设置给view的背景
         *
         * @param view
         * @return
         */
        Invoker intoBackground(View view);

        /**
         * 模糊后设置给某个目标
         *
         * @param target
         * @return
         */
        Invoker into(BlurTarget target);

        /**
         * 取消子线程任务
         */
        void cancelAsync();
    }
}
