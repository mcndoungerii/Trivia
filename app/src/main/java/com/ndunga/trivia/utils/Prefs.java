package com.ndunga.trivia.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    public static final String HIGHEST_SCORE = "highest_score";
    private SharedPreferences preferences;

    public Prefs(Activity context) {
        this.preferences = context.getPreferences(Context.MODE_PRIVATE);
    }

    public void saveHighestScore(int score){
        int currentScore = score;

        int lastScore = preferences.getInt(HIGHEST_SCORE,0);

        //check if current score > lastScore , then save to disk
        if(currentScore > lastScore) {
            preferences.edit().putInt(HIGHEST_SCORE,currentScore).apply();
        }
    }

    public int getHighestScore() {
        return preferences.getInt(HIGHEST_SCORE,0);
    }
}
