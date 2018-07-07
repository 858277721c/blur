package com.fanwe.lib.blur.core;

public interface BlurConfig
{
    /**
     * 设置模糊半径，默认10
     *
     * @param radius
     */
    void setRadius(int radius);

    /**
     * 设置压缩倍数，默认8
     *
     * @param downSampling
     */
    void setDownSampling(int downSampling);

    /**
     * 设置覆盖层颜色，默认透明
     *
     * @param colorOverlay
     */
    void setColorOverlay(int colorOverlay);

    /**
     * 设置返回的模糊Bitmap是否要保持压缩后的宽和高，默认false-保持原来的宽和高
     *
     * @param keepDownSamplingSize true-保持压缩后的宽和高，false-保持原来的宽和高
     */
    void setKeepDownSamplingSize(boolean keepDownSamplingSize);

    /**
     * 返回压缩倍数
     *
     * @return
     */
    int getDownSampling();
}
