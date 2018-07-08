package com.fanwe.blur;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.fanwe.lib.blur.core.CompatBlur;

public class BlurActivity extends AppCompatActivity implements View.OnClickListener
{
    private ImageView mImageView;

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
        final Bitmap bitmap = Utils.randomBitmap(this);

        final Bitmap blurBitmap = new CompatBlur(this)
                // 设置模糊半径，默认10
                .setRadius(10)
                // 设置压缩倍数，默认8
                .setDownSampling(8)
                // 设置覆盖层颜色，默认透明
                .setColor(Color.parseColor("#66FFFFFF"))
                // 执行模糊操作，得到模糊的Bitmap
                .blur(bitmap);

        mImageView.setImageBitmap(blurBitmap);
    }
}
