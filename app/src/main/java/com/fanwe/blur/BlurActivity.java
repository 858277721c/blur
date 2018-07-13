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

    private BlurApi getBlurApi()
    {
        if (mBlurApi == null)
        {
            mBlurApi = BlurApiFactory.create(this);
            /**
             * 设置当前对象在模糊方法被调用之后是否自动释放资源
             * 当API版本支持RenderScript的时候，如果需要频繁的模糊操作，可以持有BlurApi对象，并设置为false，避免一直创建对象，效率会高很多
             * 在最后需要销毁的地方销毁BlurlApi对象即可
             */
            mBlurApi.setDestroyAfterBlur(false);
        }
        return mBlurApi;
    }

    @Override
    public void onClick(final View view)
    {
        // 随机加载一张图片
        final Bitmap bitmap = Utils.randomBitmap(this);

        getBlurApi()
                .setRadius(15) // 设置模糊半径
                .setDownSampling(8) // 设置压缩倍数
                .setColor(Color.parseColor("#66FFFFFF")) // 设置覆盖层颜色
                .blur(bitmap)
                .async(true) // 设置是否在子线程执行
                .into(mImageView);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        /**
         * 释放资源，并取消所有和该api对象关联的子线程任务
         */
        if (mBlurApi != null)
            mBlurApi.destroy();
    }
}
