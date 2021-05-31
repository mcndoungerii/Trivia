package com.ndunga.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.snackbar.Snackbar;
import com.ndunga.trivia.data.Repository;
import com.ndunga.trivia.databinding.ActivityMainBinding;
import com.ndunga.trivia.model.Question;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;

    private int currentQuestionIndex = 0;

    private final Question quest = new Question();

    List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        binding.setQuestion(quest);

        questionList = new Repository().getQuestion( questionArrayList -> {
            binding.questionTextView.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
            updateCounter((ArrayList<Question>) questionArrayList);

        });

        binding.buttonNext.setOnClickListener(view -> {
            currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();

            //% questionList.size() -- helps us to avoid the error ArrayOutBoundIndex

            updateQuestion();

        });

        binding.trueButton.setOnClickListener(view -> {
            checkAnswer(true);
            updateQuestion();

        });

        binding.falseButton.setOnClickListener(view -> {
            checkAnswer(false);
            updateQuestion();
        });


    }

    private void checkAnswer(boolean userChoiceAns) {
        boolean answerFromApi = questionList.get(currentQuestionIndex).isAnswerTrue();
        int snackIndex ;
        if(userChoiceAns == answerFromApi) {
            snackIndex = R.string.correct_answer;
            fadeAnimation();
        }
        else {
            snackIndex = R.string.incorrect_answer;
            shakeAnimation();
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


}