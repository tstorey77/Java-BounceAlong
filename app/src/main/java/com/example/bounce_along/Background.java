package com.example.bounce_along;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

// Sets up the bitmap for the background
public class Background
{
    int x = 0;
    int y = 0;
    Bitmap background;

    Background(int screenX, int screenY, Resources res){
        background = BitmapFactory.decodeResource(res, R.drawable.background);
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false);

    }
}
