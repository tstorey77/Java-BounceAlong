package com.example.bounce_along;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;

public class Hunter {
    public int speed = 20;
    int x, y, height, width;
    private Bitmap hunter;

    Hunter(int screenY, Resources res){
        Matrix matrix = new Matrix();
        matrix.postRotate(180);

        hunter =  BitmapFactory.decodeResource(res, R.drawable.caveman);

        // get hunter dimensions
        width = hunter.getWidth();
        height = hunter.getHeight();

        // scale them
        width /= 4;
        height /= 4;

        // create bitmap
        hunter =  Bitmap.createScaledBitmap(hunter, width, height, false);
        y = -screenY;
        x = -550;

    }

    // gets rectangle for the collision box
    Rect getCollisionShape(){
        return new Rect(x + 100 , y, x + width -300, y + height);
    }

    Bitmap getHunter() {return hunter;}
}
