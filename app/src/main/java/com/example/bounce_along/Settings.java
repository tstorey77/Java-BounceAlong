package com.example.bounce_along;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.EventLog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;

public class Settings extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private RadioButton rbEasy, rbMedium, rbHard;
    private CheckBox cbMusic;
    private Boolean music;
    private String mode;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        rbEasy = findViewById(R.id.rbEasy);
        rbMedium = findViewById(R.id.rbMedium);
        rbHard = findViewById(R.id.rbHard);
        cbMusic = findViewById(R.id.cbMusic);

        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        music = sharedPreferences.getBoolean("Music", false);
        mode = sharedPreferences.getString("Mode", "Easy");

        EventHandler eventHandler = new EventHandler();

        rbEasy.setOnClickListener(eventHandler);
        rbMedium.setOnClickListener(eventHandler);
        rbHard.setOnClickListener(eventHandler);
        cbMusic.setOnClickListener(eventHandler);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //editor = sharedPreferences.edit();
        cbMusic.setChecked(music);
        if(mode == "Easy"){
            rbEasy.setChecked(true);
        } else if (mode == "Medium"){
            rbMedium.setChecked(true);
        } else {
            rbHard.setChecked(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor = sharedPreferences.edit();
        if(cbMusic.isChecked()){
           editor.putBoolean("Music", true);
        } else {
            editor.putBoolean("Music", false);
        }
    }

    class EventHandler implements View.OnClickListener{
        private SharedPreferences.Editor editor = sharedPreferences.edit();

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.rbEasy:
                    if(rbEasy.isChecked()){
                        // set Easy mode
                        editor.putString("Mode", "Easy");
                    }
                    break;
                case R.id.rbMedium:
                    if(rbMedium.isChecked()){
                        // set Medium mode
                        editor.putString("Mode", "Medium");
                    }
                    break;
                case R.id.rbHard:
                    if(rbHard.isChecked()){
                        // set Hard mode
                        editor.putString("Mode", "Hard");
                    }
                    break;
                case R.id.cbMusic:
                    if(cbMusic.isChecked()){
                        // Allow music
                        editor.putBoolean("Music", true);
                    } else {
                        editor.putBoolean("Music", false);
                    }
                    break;
            }
            editor.commit();
        }
    }
}
