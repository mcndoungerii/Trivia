package com.ndunga.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.ndunga.trivia.data.Repository;
import com.ndunga.trivia.databinding.ActivityMainBinding;
import com.ndunga.trivia.model.Question;

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
            updateCounter();

        });

        binding.buttonNext.setOnClickListener(view -> {
            currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();

            //% questionList.size() -- helps us to avoid the error ArrayOutBoundIndex

            updateQuestion();

        });

        binding.trueButton.setOnClickListener(view -> checkAnswer(true));

        binding.falseButton.setOnClickListener(view ->checkAnswer(false));


    }

    private void checkAnswer(boolean userChoiceAns) {
        boolean answerFromApi = questionList.get(currentQuestionIndex).isAnswerTrue();
        int snackIndex ;
        if(userChoiceAns == answerFromApi) {
            snackIndex = R.string.correct_answer;

        }
        else {
            snackIndex = R.string.incorrect_answer;
        }

        Snackbar.make(binding.cardView,snackIndex,Snackbar.LENGTH_SHORT).show();
    }

    private void updateCounter() {
        binding.counterView.setText(String.format("%s %d/%d", getString(R.string.counter_text), currentQuestionIndex, questionList.size()));
    }

    private void updateQuestion() {

        String question = questionList.get(currentQuestionIndex).getAnswer();

        binding.questionTextView.setText(question);
        updateCounter();
    }
}