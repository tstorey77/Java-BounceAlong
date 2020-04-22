package com.example.bounce_along;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

// Create the dinosaur object
public class Dino {
    public boolean isJumping = false;
    int x, y, width, height;
    Bitmap dino;

    Dino(int screenY, Resources res){
        dino = BitmapFactory.decodeResource(res, R.drawable.dino);
        // get dimensions
        width = dino.getWidth();
        height = dino.getHeight();
        // scale them
        width /= 4;
        height /= 4;
        // draw
        dino = Bitmap.createScaledBitmap(dino, width, height, false);
        // get x and y for collision detection
        y = screenY - 225;
        x = 64;
    }

    // the rectangle we are using for collision
    Rect getCollisionShape(){
        return new Rect(x, y, x + width - 100, y + height);
    }

    Bitmap getDino(){
        return dino;
    }

}
