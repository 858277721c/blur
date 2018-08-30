package com.sd.lib.blur.view;

import com.sd.lib.blur.core.Blur;

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
     * 是否在子线程进行模糊，默认false
     *
     * @param async
     */
    void setBlurAsync(boolean async);

    /**
     * 模糊
     */
    void blur();
}
