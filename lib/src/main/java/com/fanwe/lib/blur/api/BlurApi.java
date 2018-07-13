package com.fanwe.lib.blur.api;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.fanwe.lib.blur.api.target.BlurTarget;
import com.fanwe.lib.blur.core.Blur;

import java.util.concurrent.ExecutorService;

public interface BlurApi
{
    /**
     * {@link Blur#setRadius(int)}
     *
     * @param radius
     * @return
     */
    BlurApi setRadius(int radius);

    /**
     * {@link Blur#setDownSampling(int)}
     *
     * @param downSampling
     * @return
     */
    BlurApi setDownSampling(int downSampling);

    /**
     * {@link Blur#setColor(int)}
     *
     * @param color
     * @return
     */
    BlurApi setColor(int color);

    /**
     * {@link Blur#setKeepDownSamplingSize(boolean)}
     *
     * @param keepDownSamplingSize
     * @return
     */
    BlurApi setKeepDownSamplingSize(boolean keepDownSamplingSize);

    /**
     * {@link Blur#setDestroyAfterBlur(boolean)}
     *
     * @param destroyAfterBlur
     * @return
     */
    BlurApi setDestroyAfterBlur(boolean destroyAfterBlur);

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

    interface Invoker extends Cancelable
    {
        /**
         * 是否在子线程执行模糊操作
         *
         * @param async
         * @return
         */
        Invoker async(boolean async);

        /**
         * 模糊后设置给ImageView
         *
         * @param imageView
         * @return
         */
        Cancelable into(ImageView imageView);

        /**
         * 模糊后设置给view的背景
         *
         * @param view
         * @return
         */
        Cancelable intoBackground(View view);

        /**
         * 模糊后设置给某个目标
         *
         * @param target
         * @return
         */
        Cancelable into(BlurTarget target);
    }

    interface Cancelable
    {
        /**
         * 取消任务
         */
        void cancel();
    }

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
