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
        final Bitmap bitmap = Utils.randomBitmap(this);
        mTimeLogger.start();

        final Bitmap blurBitmap = new CompatBlur(this)
                .setRadius(10)
                .setDownSampling(8)
                .setColor(Color.parseColor("#66FFFFFF"))
                .blur(bitmap);
        mImageView.setImageBitmap(blurBitmap);

        mTimeLogger.print("blur api");
    }
}
