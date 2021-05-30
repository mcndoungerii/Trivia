package com.ndunga.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ndunga.trivia.controller.AppController;
import com.ndunga.trivia.data.Repository;
import com.ndunga.trivia.databinding.ActivityMainBinding;
import com.ndunga.trivia.model.Question;

import org.json.JSONArray;

import java.lang.reflect.Method;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;

    private int currentQuestionIndex = 0;

    private Question quest = new Question();

    List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        binding.setQuestion(quest);

        questionList = new Repository().getQuestion( questionArrayList -> {


            binding.questionTextView.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
        });

        binding.buttonNext.setOnClickListener(view -> {
            currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();

            //% questionList.size() -- helps us to avoid the error ArrayOutofBoundIndex

            updateQuestion();

        });

        binding.trueButton.setOnClickListener(view -> {

        });

        binding.falseButton.setOnClickListener(view -> {

        });


    }

    private void updateQuestion() {

        String question = questionList.get(currentQuestionIndex).getAnswer();

        binding.questionTextView.setText(question);
    }
}