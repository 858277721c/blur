package com.fanwe.blur;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.fanwe.lib.blur.api.FBlurry;

import java.util.Random;

public class BlurActivity extends AppCompatActivity
{
    public static final int COLOR_OVERLAY_TRANSPARENT = Color.TRANSPARENT;
    public static final int COLOR_OVERLAY_RED = Color.parseColor("#66FF0000");
    private final TimeLogger mTimeLogger = new TimeLogger(BlurActivity.class.getSimpleName());

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mTimeLogger.setDebug(true);
        setContentView(R.layout.act_blur);
        mImageView = findViewById(R.id.imageview);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final int index = new Random().nextInt(10);
                final int resId = getResources().getIdentifier("fj" + index, "drawable", getPackageName());
                final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);

                mTimeLogger.start();
                FBlurry.newInstance(BlurActivity.this)
                        .from(bitmap)
                        .into(mImageView);
                mTimeLogger.print("blurry");
            }
        });
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
