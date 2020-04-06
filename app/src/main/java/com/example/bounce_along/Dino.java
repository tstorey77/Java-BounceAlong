package com.example.bounce_along;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Dino {
    public boolean isJumping = false;
    int x, y, width, height;
    Bitmap dino;

    Dino(int screenY, Resources res){
        dino = BitmapFactory.decodeResource(res, R.drawable.dino);

        width = dino.getWidth();
        height = dino.getHeight();

        width /= 4;
        height /= 4;

        dino = Bitmap.createScaledBitmap(dino, width, height, false);

        y = screenY - 225;
        x = 64;
    }

    Bitmap getDino(){
        return dino;
    }

}
