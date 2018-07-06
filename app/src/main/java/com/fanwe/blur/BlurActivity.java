package com.fanwe.blur;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.fanwe.lib.blur.api.FBlur;

import java.util.Random;

public class BlurActivity extends AppCompatActivity implements View.OnClickListener
{
    private final TimeLogger mTimeLogger = new TimeLogger(BlurActivity.class.getSimpleName());
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
        final int index = new Random().nextInt(10);
        final int resId = getResources().getIdentifier("fj" + index, "drawable", getPackageName());
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);

        mTimeLogger.start();
        FBlur.newInstance(BlurActivity.this)
                .from(bitmap)
                .into(mImageView)
                .destroy();
        mTimeLogger.print("blur api");
    }
}
