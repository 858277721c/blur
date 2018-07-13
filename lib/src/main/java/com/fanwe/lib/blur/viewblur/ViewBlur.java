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
     * 是否在子线程进行模糊，默认false
     *
     * @param async
     * @return
     */
    ViewBlur<V> setBlurAsync(boolean async);

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
     * 设置模糊的数据源
     *
     * @param source
     * @return
     */
    ViewBlur<V> setSource(V source);

    /**
     * 清空{@link #setSource(View)}和{@link #setTarget(View)}设置的View
     * <p>
     * 释放指向当前对象的引用
     */
    void release();
}
