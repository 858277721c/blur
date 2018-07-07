package com.fanwe.lib.blur.view;

import com.fanwe.lib.blur.core.Blur;

public interface BlurView
{
    /**
     * {@link Blur#setRadius(int)}
     *
     * @param radius
     */
    void setBlurRadius(int radius);

    /**
     * {@link Blur#setDownSampling(int)}
     *
     * @param downSampling
     */
    void setBlurDownSampling(int downSampling);

    /**
     * {@link Blur#setColor(int)}
     *
     * @param color
     */
    void setBlurColor(int color);

    /**
     * 模糊
     */
    void blur();
}
