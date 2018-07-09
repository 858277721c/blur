package com.fanwe.blur;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.fanwe.lib.blur.api.BlurApiFactory;
import com.fanwe.lib.blur.api.BlurInvoker;

public class BlurActivity extends AppCompatActivity implements View.OnClickListener
{
    private ImageView mImageView;
    private BlurInvoker mBlurInvoker;

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
        final Bitmap bitmap = Utils.randomBitmap(getApplicationContext());

        mBlurInvoker = BlurApiFactory.create(getApplicationContext())
                // 设置模糊半径，默认10
                .radius(10)
                // 设置压缩倍数，默认8
                .downSampling(8)
                // 设置覆盖层颜色，默认透明
                .color(Color.parseColor("#66FFFFFF"))
                .blur(bitmap)
                // 设置是否在子线程执行
                .async(true)
                .into(mImageView);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        // 如果有未完成的子线程任务，取消任务
        if (mBlurInvoker != null)
            mBlurInvoker.cancelAsync();
    }
}
