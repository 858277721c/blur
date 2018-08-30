package com.sd.lib.blur.api;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.sd.lib.blur.core.Blur;
import com.sd.lib.blur.core.source.BlurSource;

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
     * 模糊{@link Bitmap}
     *
     * @param source
     * @return
     */
    Invoker blur(Bitmap source);

    /**
     * 模糊{@link View}
     *
     * @param source
     * @return
     */
    Invoker blur(View source);

    /**
     * 模糊{@link Drawable}
     *
     * @param source
     * @return
     */
    Invoker blur(Drawable source);

    /**
     * 模糊{@link BlurSource}
     *
     * @param source
     * @return
     */
    Invoker blur(BlurSource source);

    /**
     * {@link Blur#destroy()}
     *
     * @return
     */
    BlurApi destroy();

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
         * 得到模糊的Bitmap对象
         *
         * @return
         */
        Bitmap bitmap();

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
        Cancelable into(Target target);
    }

    interface Target
    {
        /**
         * 模糊回调
         *
         * @param bitmap 模糊后的Bitamp，可能为null
         */
        void onBlurred(Bitmap bitmap);
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
