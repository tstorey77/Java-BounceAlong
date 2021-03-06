package com.example.bounce_along;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class GameView extends SurfaceView implements Runnable {
    private Thread thread;
    private boolean isPlaying;
    private Background background1, background2;
    private int screenX, screenY;
    private Paint paint;
    private Dino dino;
    private String m_text;
    private Hunter hunter;
    private Hunter[] mediumModeHunter;
    private Hunter[] hardModeHunter;
    private MediaPlayer player;
    private Random random;
    private boolean isGameOver = false;
    private String username;
    int score = 10;
    private int high = -10;
    private int this_score;
    private DatabaseReference mDatabase;
    private SharedPreferences sharedPreferences;
    private String mode;
    int max = 400;
    int min = 375;
    int randomNumber;


    public GameView(Context context, int screenX, int screenY){
        super(context);
        this.screenX = screenX;
        this.screenY = screenY;

        // get settings
        sharedPreferences = this.getContext().getSharedPreferences("Settings", this.getContext().MODE_PRIVATE);
        mode = sharedPreferences.getString("Mode", "Easy");

        // for future implementations
        mediumModeHunter = new Hunter[2];
        hardModeHunter = new Hunter[4];

        // database stuff - get reference and get user
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser curr_user = FirebaseAuth.getInstance().getCurrentUser();
        username = curr_user.getDisplayName();

        // get medium mode ready
        for(int i = 0 ; i < 2 ; i++){
            hunter = new Hunter(screenY, getResources());
            mediumModeHunter[i] = hunter;
        }

        // get hard mode ready
        for(int i = 0 ; i < 4 ; i++){
            hunter = new Hunter(screenY, getResources());
            hardModeHunter[i] = hunter;
        }

        // create paint object and set values
        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.BLACK);

        // create background and players
        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());
        dino = new Dino(screenY, getResources());
        hunter = new Hunter(screenY, getResources());
        background2.x = screenX;

    }
    @Override
    public void run() {
        // game loop. Runs while isPlaying is true
        while(isPlaying){
            update();
            draw();
            sleep();

        }
    }

    private void update(){
        random = new Random();
        // check settings and update game accordingly
        if (mode == "Easy"){
            background1.x -= 5;
            background2.x -= 5;
            hunter.y = screenY - 375;
        } else if (mode == "Medium"){
            randomNumber = random.nextInt(max - min) + min;
            background1.x -= 10;
            background2.x -= 10;
            hunter.speed = 30;
            hunter.y = screenY - randomNumber;
        } else {
            randomNumber = random.nextInt(725 - 375) + 375;
            background1.x -= 20;
            background2.x -= 20;
            hunter.speed = 50;
            hunter.y = screenY - randomNumber;
        }

        // hunter speed
        hunter.x -= hunter.speed;

        // off screen
        if(hunter.x + hunter.width < 0){
            // add to highscores
            hunter.x = screenX;
            hunter.y = random.nextInt(screenY/2 - hunter.height);
            high += score;
        }

        // checks if dino gets hit by hunter
        if(Rect.intersects(hunter.getCollisionShape(), dino.getCollisionShape())){
            isGameOver = true;
            return;
        }

        // less than zero means off the screen
        if(background1.x + background1.background.getWidth() < 0){
            background1.x = screenX;
        }
        if(background2.x + background2.background.getWidth() < 0){
            background2.x = screenX;
        }

        // lets the dino jump - hard coded values because no animation
        if(dino.isJumping){
            dino.y -= 300;
        } else {
            dino.y += 300;
        }

        // sets dino location
        if(dino.y < screenY / 2){
            dino.y = screenY / 2;
        }
        if(dino.y > screenY - dino.height){
            dino.y = screenY - dino.height;
        }
    }

    private void draw(){
        // draws the background and dino + hunters
        if(getHolder().getSurface().isValid()){
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            canvas.drawBitmap(dino.getDino(), dino.x, dino.y, paint);

            // game is over
            if(isGameOver){
                isPlaying = false;
                // game over show the highscore
                // save highscore
                // get the current logged in user
                paint.setTextSize(96);
                canvas.drawText("You lost! Score: " + high,screenX / 8, screenY / 3, paint );

                // check if new score is higher than their score
                FirebaseDatabase data = FirebaseDatabase.getInstance();
                DatabaseReference mReferenceUsers = data.getReference().child(username);
                mReferenceUsers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User this_user = dataSnapshot.getValue(User.class);
                        this_score = this_user.score;
                        // if high is greater than this score add to db
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                // checks if it should add to db or not
                if(high > this_score){
                    User user = new User(username, high);
                    String score_id = mDatabase.push().getKey();
                    mDatabase.child(username).child(score_id).setValue(user);
                } // else don't add

            }

            // draw hunter
            canvas.drawBitmap(hunter.getHunter(), hunter.x, hunter.y, paint);

            // draw score message on canvas
            if(isPlaying){
                canvas.drawText("Score: " + high, screenX / 3 -50, screenY / 6, paint);
            }

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void sleep(){
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // starts the game on resume
    public void resume(){
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    // handles the end game
    public void pause(){
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // This helps the dino jump on touch
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
