package com.sd.lib.blur.core;

import android.graphics.Bitmap;

import com.sd.lib.blur.core.source.BlurSource;

public interface Blur
{
    /**
     * 设置模糊半径
     *
     * @param radius
     */
    void setRadius(int radius);

    /**
     * 设置压缩倍数
     *
     * @param downSampling
     */
    void setDownSampling(int downSampling);

    /**
     * 设置覆盖层颜色
     *
     * @param color
     */
    void setColor(int color);

    /**
     * 设置返回的模糊Bitmap是否要保持压缩后的宽和高，默认false-保持原来的宽和高
     *
     * @param keepDownSamplingSize true-保持压缩后的宽和高，false-保持原来的宽和高
     */
    void setKeepDownSamplingSize(boolean keepDownSamplingSize);

    /**
     * 设置调用模糊方法后是否自动调用{@link #destroy()}，默认true自动释放
     *
     * @param destroyAfterBlur
     */
    void setDestroyAfterBlur(boolean destroyAfterBlur);

    /**
     * 返回模糊半径
     *
     * @return
     */
    int getRadius();

    /**
     * 返回压缩倍数
     *
     * @return
     */
    int getDownSampling();

    /**
     * 返回模糊颜色
     *
     * @return
     */
    int getColor();

    /**
     * 是否保持压缩后的宽和高
     *
     * @return
     */
    boolean isKeepDownSamplingSize();

    /**
     * 模糊操作后是否自动调用{@link #destroy()}方法
     *
     * @return
     */
    boolean isDestroyAfterBlur();

    /**
     * 模糊
     *
     * @param source
     * @return
     */
    Bitmap blur(BlurSource source);

    /**
     * 释放资源，调用此方法后依旧可以使用此对象
     */
    void destroy();
}
