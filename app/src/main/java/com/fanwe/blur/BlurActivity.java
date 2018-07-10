package com.fanwe.blur;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.fanwe.lib.blur.api.BlurApi;
import com.fanwe.lib.blur.api.BlurApiFactory;

public class BlurActivity extends AppCompatActivity implements View.OnClickListener
{
    private ImageView mImageView;
    private BlurApi mBlurApi;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_blur);
        mImageView = findViewById(R.id.imageview);
    }

    @Override
    public void onClick(final View view)
    {
        // 随机加载一张图片
        final Bitmap bitmap = Utils.randomBitmap(this);

        mBlurApi = BlurApiFactory.create(this);
        mBlurApi
                // 设置模糊半径
                .radius(15)
                // 设置压缩倍数
                .downSampling(8)
                // 设置覆盖层颜色
                .color(Color.parseColor("#66FFFFFF"))
                // 设置是否在子线程执行
                .async(true)
                .blur(bitmap)
                .into(mImageView);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        /**
         * 释放资源，并取消所有的子线程任务
         */
        if (mBlurApi != null)
            mBlurApi.destroy();
    }
}
