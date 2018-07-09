package com.fanwe.lib.blur.api;

import com.fanwe.lib.blur.core.Blur;

public interface BlurApiConfig
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
}
