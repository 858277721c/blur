package com.fanwe.lib.blur.viewblur;

import android.view.View;

import com.fanwe.lib.blur.core.Blur;

public interface ViewBlur<V extends View>
{
    /**
     * {@link Blur#setRadius(int)}
     *
     * @param radius
     * @return
     */
    ViewBlur<V> setBlurRadius(int radius);

    /**
     * {@link Blur#setDownSampling(int)}
     *
     * @param downSampling
     * @return
     */
    ViewBlur<V> setBlurDownSampling(int downSampling);

    /**
     * {@link Blur#setColor(int)}
     *
     * @param color
     * @return
     */
    ViewBlur<V> setBlurColor(int color);

    /**
     * {@link #setTarget(View)}
     *
     * @return
     */
    V getTarget();

    /**
     * 设置得到模糊数据后，要模糊的目标
     *
     * @param target
     * @return
     */
    ViewBlur<V> setTarget(V target);

    /**
     * {@link #setSource(View)}
     *
     * @return
     */
    V getSource();

    /**
     * 设置要模糊数据的来源
     *
     * @param source
     * @return
     */
    ViewBlur<V> setSource(V source);
}
