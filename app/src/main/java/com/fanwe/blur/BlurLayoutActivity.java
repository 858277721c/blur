package com.fanwe.blur;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.fanwe.lib.blur.view.FBlurLayout;

public class BlurLayoutActivity extends AppCompatActivity implements View.OnClickListener
{
    private ImageView mImageView;
    private FBlurLayout mBlurLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_blur_layout);
        mImageView = findViewById(R.id.imageview);
        mBlurLayout = findViewById(R.id.view_blur);
    }

    @Override
    public void onClick(View v)
    {
        // 随机加载一张图片
        final Bitmap bitmap = Utils.randomBitmap(getApplicationContext());
        mImageView.setImageBitmap(bitmap);

        // 执行模糊操作
        mBlurLayout.blur();
    }
}
