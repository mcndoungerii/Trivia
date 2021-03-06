package com.ndunga.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.snackbar.Snackbar;
import com.ndunga.trivia.data.Repository;
import com.ndunga.trivia.databinding.ActivityMainBinding;
import com.ndunga.trivia.model.Question;
import com.ndunga.trivia.model.Score;
import com.ndunga.trivia.utils.Prefs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final String MESSAGE_ID = "score_prefs";
    private ActivityMainBinding binding;

    private int currentQuestionIndex = 0;

    private final Question quest = new Question();

    List<Question> questionList;

    private int scoreCounter = 0;

    private Score score;

    private Prefs prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        binding.setQuestion(quest);

        questionList = new Repository().getQuestion( questionArrayList -> {
            binding.questionTextView.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
            updateCounter((ArrayList<Question>) questionArrayList);

        });

        //create score object
        score = new Score();
        prefs = new Prefs(this);

        //GET currentIndex
        currentQuestionIndex = prefs.getState();


        binding.highScoreText.setText(MessageFormat.format("Highest: {0}", String.valueOf(prefs.getHighestScore())));

        binding.buttonNext.setOnClickListener(view -> {
            getNextQuestion();
        });

        binding.trueButton.setOnClickListener(view -> {
            checkAnswer(true);
            updateQuestion();

        });

        binding.falseButton.setOnClickListener(view -> {
            checkAnswer(false);
            updateQuestion();
        });




        binding.scoreCounter.setText(String.valueOf(MessageFormat.format("Score Counter: {0}", score.getScore())));

        binding.sendButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, MessageFormat.format("My current score: {0}, And my highest is: {1}", score.getScore(), prefs.getHighestScore()));
            intent.putExtra(Intent.EXTRA_SUBJECT,"I am playing Trivia");
            startActivity(intent);
        });
    }

    private void getNextQuestion() {
        currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();

        updateQuestion();
    }

    private void checkAnswer(boolean userChoiceAns) {
        boolean answerFromApi = questionList.get(currentQuestionIndex).isAnswerTrue();
        int snackIndex ;
        if(userChoiceAns == answerFromApi) {
            snackIndex = R.string.correct_answer;
            fadeAnimation();
            addPoints();
        }
        else {
            snackIndex = R.string.incorrect_answer;
            shakeAnimation();
            deductPoints();
        }

        Snackbar.make(binding.cardView,snackIndex,Snackbar.LENGTH_SHORT).show();
    }

    private void fadeAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);

        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        binding.questionTextView.setAnimation(alphaAnimation);

        //Listen to animation events
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextView.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextView.setTextColor(Color.WHITE);
                getNextQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(this,R.anim.shake_animation);

        binding.cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextView.setTextColor(Color.RED);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextView.setTextColor(Color.WHITE);
                getNextQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void updateCounter(ArrayList<Question> questionArrayList) {
        binding.counterView.setText(String.format("%s %d/%d", getString(R.string.counter_text), currentQuestionIndex, questionArrayList.size()));
    }

    private void updateQuestion() {

        String question = questionList.get(currentQuestionIndex).getAnswer();

        binding.questionTextView.setText(question);
        updateCounter((ArrayList<Question>) questionList);
    }

    private void addPoints() {
        scoreCounter += 100;
        score.setScore(scoreCounter);
        //saveScore(scoreCounter);
        binding.scoreCounter.setText(String.valueOf(MessageFormat.format("Score Counter: {0}", score.getScore())));

    }



    private  void  deductPoints(){
        if(scoreCounter > 0) {
            scoreCounter -= 100;
            score.setScore(scoreCounter);
            //saveScore(scoreCounter);
            binding.scoreCounter.setText(String.valueOf(MessageFormat.format("Score Counter: {0}", score.getScore())));

        }
        else {
            scoreCounter = 0;
            score.setScore(scoreCounter);
            //saveScore(scoreCounter);
            binding.scoreCounter.setText(String.valueOf(MessageFormat.format("Score Counter: {0}", score.getScore())));

        }
    }

//    private void saveScore(int scoreCounter) {
//        //save to shared prefs
//        SharedPreferences sharedPreferences = getSharedPreferences(MESSAGE_ID,MODE_PRIVATE);
//
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        editor.putInt("score_counter",scoreCounter);
//
//        editor.apply();
//    }

//    private void retrieveScore() {
//        //get sharedData
//        SharedPreferences getSharedData = getSharedPreferences(MESSAGE_ID,MODE_PRIVATE);
//
//        int value = getSharedData.getInt("score_counter",0);
//
//        binding.scoreCounter.setText(String.valueOf(MessageFormat.format("Score Counter: {0}", score.getScore())));
//    }


    @Override
    protected void onPause() {

        //save to disk
        prefs.saveHighestScore(score.getScore());

        //save currentIndex
        prefs.setState(currentQuestionIndex);

        //check the saved Score
        Log.d("Pause:::", String.valueOf(prefs.getHighestScore()));
        super.onPause();
    }
}