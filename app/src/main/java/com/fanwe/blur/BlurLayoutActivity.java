package com.fanwe.blur;

import android.graphics.Bitmap;
import android.graphics.Color;
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

        mBlurLayout.setBlurColor(Color.parseColor("#6600FF00"));
    }

    @Override
    public void onClick(View v)
    {
        final Bitmap bitmap = Utils.randomBitmap(BlurLayoutActivity.this);

        mImageView.setImageBitmap(bitmap);
        mBlurLayout.blur();
    }
}
