package com.fanwe.lib.blur.api;

import android.view.View;
import android.widget.ImageView;

public interface BlurInvoker
{
    /**
     * 模糊后设置给ImageView
     *
     * @param imageView
     * @return
     */
    BlurInvoker into(ImageView imageView);

    /**
     * 模糊后设置给view的背景
     *
     * @param view
     * @return
     */
    BlurInvoker intoBackground(View view);

    /**
     * 模糊后设置给某个目标
     *
     * @param target
     * @return
     */
    BlurInvoker into(BlurTarget target);

    /**
     * 取消子线程任务
     */
    void cancelAsync();
}
