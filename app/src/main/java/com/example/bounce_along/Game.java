package com.example.bounce_along;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

// Runs the gameView and sets values
public class Game extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private String mode;
    private boolean music;
    private TextView tvMode, tvMusic;

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        // create and set gameview
        gameView = new GameView(this, point.x, point.y);
        setContentView(gameView);

    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}
