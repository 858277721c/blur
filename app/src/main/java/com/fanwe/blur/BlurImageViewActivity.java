package com.fanwe.blur;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class BlurImageViewActivity extends Activity implements View.OnClickListener
{
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_blur_imageview);
        mImageView = findViewById(R.id.imageview);
    }

    @Override
    public void onClick(View v)
    {
        final Bitmap bitmap = Utils.randomBitmap(this);
        mImageView.setImageBitmap(bitmap);
    }
}
