package com.example.bounce_along;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnStart, btnHighscores, btnSettings;
    private MediaPlayer player;
    private SharedPreferences sharedPreferences;
    private Boolean music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.btnStart);
        btnHighscores = findViewById(R.id.btnHighscores);
        btnSettings = findViewById(R.id.btnSettings);

        EventHandler eventHandler = new EventHandler();
        btnStart.setOnClickListener(eventHandler);
        btnHighscores.setOnClickListener(eventHandler);
        btnSettings.setOnClickListener(eventHandler);


        if(player == null){
            player = MediaPlayer.create(this, R.raw.song);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get preferences
        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        music = sharedPreferences.getBoolean("Music", true);

        if(music){
            player.start();
        } else {
            player.pause();
        }
    }

    class EventHandler implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btnStart:
                    Intent startIntent = new Intent(MainActivity.this, Game.class);
                    startActivity(startIntent);
                    break;
                case R.id.btnHighscores:
                    Intent highIntent = new Intent(MainActivity.this, Highscores.class);
                    startActivity(highIntent);
                    break;
                case R.id.btnSettings:
                    Intent intent = new Intent(MainActivity.this, Settings.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}
