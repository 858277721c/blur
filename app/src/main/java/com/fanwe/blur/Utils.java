package com.fanwe.blur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Utils
{
    private static final Integer[] IMAGES = {
            R.drawable.fj0,
            R.drawable.fj1,
            R.drawable.fj2,
            R.drawable.fj3,
            R.drawable.fj4,
            R.drawable.fj5,
            R.drawable.fj6,
            R.drawable.fj7,
            R.drawable.fj8,
            R.drawable.fj9,
    };

    private static int sIndex = -1;

    public static Bitmap randomBitmap(Context context)
    {
        while (true)
        {
            final int index = new Random().nextInt(IMAGES.length);
            if (index == sIndex)
                continue;

            sIndex = index;
            break;
        }

        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), IMAGES[sIndex]);
        return bitmap;
    }
}
