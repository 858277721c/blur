package com.fanwe.blur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Utils
{
    private static int sIndex = -1;

    public static Bitmap randomBitmap(Context context)
    {
        while (true)
        {
            final int index = new Random().nextInt(10);
            if (index == sIndex)
                continue;

            sIndex = index;
            break;
        }

        final int resId = context.getResources().getIdentifier("fj" + sIndex, "drawable", context.getPackageName());
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        return bitmap;
    }
}
