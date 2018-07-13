package com.fanwe.lib.blur.viewblur;

import android.view.View;

import com.fanwe.lib.blur.core.Blur;

public interface ViewBlur<S extends View>
{
    /**
     * {@link Blur#setRadius(int)}
     *
     * @param radius
     */
    void setRadius(int radius);

    /**
     * {@link Blur#setDownSampling(int)}
     *
     * @param downSampling
     */
    void setDownSampling(int downSampling);

    /**
     * {@link Blur#setColor(int)}
     *
     * @param color
     */
    void setColor(int color);

    /**
     * {@link #setTarget(View)}
     *
     * @return
     */
    S getTarget();

    /**
     * 设置得到模糊数据后，要模糊的目标
     */
    void setTarget(S target);

    /**
     * {@link #setSource(View)}
     *
     * @return
     */
    S getSource();

    /**
     * 设置要模糊数据的来源
     *
     * @param source
     */
    void setSource(S source);
}
