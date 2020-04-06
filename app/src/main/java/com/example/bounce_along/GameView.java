package com.example.bounce_along;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {
    private Thread thread;
    private boolean isPlaying;
    private Background background1, background2;
    private int screenX, screenY;
    private Paint paint;
    private Dino dino;
    private Hunter hunter;
    private MediaPlayer player;

    public GameView(Context context, int screenX, int screenY){
        super(context);
        this.screenX = screenX;
        this.screenY = screenY;

        paint = new Paint();
        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());
        dino = new Dino(screenY, getResources());
        hunter = new Hunter(screenY, getResources());

        background2.x = screenX;
    }
    @Override
    public void run() {
        while(isPlaying){
            update();
            draw();
            sleep();

        }
    }

    private void update(){
        background1.x -= 5;
        background2.x -= 5;

        // hunter stuff
        hunter.x = 200;
        hunter.y = screenY/2;

        // less than zero means off the screen
        if(background1.x + background1.background.getWidth() < 0){
            background1.x = screenX;
        }
        if(background2.x + background2.background.getWidth() < 0){
            background2.x = screenX;
        }

        if(dino.isJumping){
            dino.y -= 300;
        } else {
            dino.y += 300;
        }

        if(dino.y < screenY / 2){
            dino.y = screenY / 2;
        }
        if(dino.y > screenY - dino.height){
            dino.y = screenY - dino.height;
        }
    }

    private void draw(){
        if(getHolder().getSurface().isValid()){
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            canvas.drawBitmap(dino.getDino(), dino.x, dino.y, paint);
            canvas.drawBitmap(hunter.getHunter(), hunter.x, hunter.y, paint);
            canvas.drawBitmap(hunter.getHunter(), hunter.x + 500, hunter.y, paint);

            getHolder().unlockCanvasAndPost(canvas);
        }
    }
// use firebase for the db - internet and db credit
    private void sleep(){
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume(){
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause(){
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(x > dino.x && x < dino.x + dino.width && y > dino.y && y < dino.y + dino.height ){
                    dino.isJumping = true;
                } else {
                    dino.isJumping = false;
                }
             return true;
        }
        return false;

    }
}
