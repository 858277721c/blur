package com.fanwe.blur;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;


import com.fanwe.lib.blur.view.FBlurLayout;

import java.util.Random;

public class BlurLayoutActivity extends AppCompatActivity
{
    public static final int COLOR_OVERLAY_TRANSPARENT = Color.TRANSPARENT;
    public static final int COLOR_OVERLAY_GREEN = Color.parseColor("#6600FF00");

    private ImageView mImageView;
    private FBlurLayout mBlurLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_blur_layout);
        mImageView = findViewById(R.id.imageview);
        mBlurLayout = findViewById(R.id.view_blur);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final int index = new Random().nextInt(10);
                final int resId = getResources().getIdentifier("fj" + index, "drawable", getPackageName());
                final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);

                if (index % 2 == 0)
                    mBlurLayout.getBlurView().setColorOverlay(COLOR_OVERLAY_GREEN);
                else
                    mBlurLayout.getBlurView().setColorOverlay(COLOR_OVERLAY_TRANSPARENT);

                mImageView.setImageBitmap(bitmap);
                mBlurLayout.getBlurView().invalidate();
            }
        });
    }
}
