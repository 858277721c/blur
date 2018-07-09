package com.fanwe.blur;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_blur:
                startActivity(new Intent(this, BlurActivity.class));
                break;
            case R.id.btn_blur_imageview:
                startActivity(new Intent(this, BlurImageViewActivity.class));
                break;
            case R.id.btn_blur_layout:
                startActivity(new Intent(this, BlurLayoutActivity.class));
                break;
            case R.id.btn_blur_view:
                startActivity(new Intent(this, BlurViewActivity.class));
                break;
        }
    }
}
