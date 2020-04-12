package com.example.bounce_along;

public class User {
    public String userId;
    public int score;

    public User(){

    }

    public User(String userId, int score){
        this.userId = userId;
        this.score = score;
    }

    public String getUserId(){return userId;}
    public int getScore(){return score;}
}
