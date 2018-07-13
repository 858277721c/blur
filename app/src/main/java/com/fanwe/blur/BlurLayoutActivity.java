package com.fanwe.blur;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.fanwe.lib.blur.view.BlurView;

public class BlurLayoutActivity extends AppCompatActivity implements View.OnClickListener
{
    private ImageView mImageView;
    private BlurView mBlurView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_blur_layout);
        mBlurView = findViewById(R.id.view_blur);
        mImageView = findViewById(R.id.imageview);

        mImageView.getViewTreeObserver().addOnPreDrawListener(mOnPreDrawListener);
    }

    private final ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener()
    {
        @Override
        public boolean onPreDraw()
        {
            // 监听onPreDraw，实时刷新
            mBlurView.blur();
            return true;
        }
    };

    @Override
    public void onClick(View v)
    {
        // 随机加载一张图片
        final Bitmap bitmap = Utils.randomBitmap(this);
        mImageView.setImageBitmap(bitmap);
    }
}
