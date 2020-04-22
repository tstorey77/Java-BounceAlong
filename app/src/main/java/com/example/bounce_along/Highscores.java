package com.example.bounce_along;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class Highscores extends AppCompatActivity {

    private DatabaseReference database;
    private DatabaseReference ref;
    private String username;
    private User user;
    private int[] highscores;
    private JSONObject jsonObject;
    private int true_score;
    private int counter = 0;
    private String keyValue;
    private String valueString;
    private String[] score_user;
    private String this_user;
    private TextView score1, score2, score3, score4, score5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);
        getSupportActionBar().hide();

        // initializing variables
        score1 = findViewById(R.id.tvScore1);
        score2 = findViewById(R.id.tvScore2);
        score3 = findViewById(R.id.tvScore3);
        score4 = findViewById(R.id.tvScore4);
        score5 = findViewById(R.id.tvScore5);

        // database stuff
        final FirebaseUser curr_user = FirebaseAuth.getInstance().getCurrentUser();
        username = curr_user.getDisplayName();
        database = FirebaseDatabase.getInstance().getReference();
        ref = database.child(username);
        String score_id = database.push().getKey();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                highscores = new int[(int)dataSnapshot.getChildrenCount()];
                score_user = new String[(int)dataSnapshot.getChildrenCount()];
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    user = keyNode.getValue(User.class);
                    this_user = user.getUserId();
                    true_score = user.getScore();
                    highscores[counter] = true_score;
                    score_user[counter] = this_user;
                    counter++ ;
                }

                // sorting array so we can just display the top 5
                for(int i = 0 ; i < highscores.length -1 ; i++){
                    for(int j = 0 ; j < highscores.length-1; j++){
                        if(highscores[j] < highscores[j + 1]){
                            int temp = highscores[j];
                            highscores[j] = highscores[j + 1];
                            highscores[j + 1] = temp;
                        }
                    }
                }

                // displaying the highscores
                if(highscores.length < 5){
                   score1.setText("Play a minimum of 5 games!");
                   score2.setVisibility(View.GONE);
                   score3.setVisibility(View.GONE);
                   score4.setVisibility(View.GONE);
                   score5.setVisibility(View.GONE);
                } else {
                    score1.setText("#1... " + highscores[0] + " By: " + score_user[0]);
                    score2.setText("#2... " + highscores[1] + " By: " + score_user[1]);
                    score3.setText("#3... " + highscores[2] + " By: " + score_user[2]);
                    score4.setText("#4... " + highscores[3] + " By: " + score_user[3]);
                    score5.setText("#5... " + highscores[4] + " By: " + score_user[4]);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
