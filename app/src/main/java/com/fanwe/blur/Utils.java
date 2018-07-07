package com.fanwe.blur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Utils
{
    public static Bitmap randomBitmap(Context context)
    {
        final int index = new Random().nextInt(10);
        final int resId = context.getResources().getIdentifier("fj" + index, "drawable", context.getPackageName());
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        return bitmap;
    }
}
