package com.ndunga.trivia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ndunga.trivia.controller.AppController;
import com.ndunga.trivia.data.Repository;
import com.ndunga.trivia.model.Question;

import org.json.JSONArray;

import java.lang.reflect.Method;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Question> question = new Repository().getQuestion( questionArrayList -> {
            Log.d("Question::::", String.valueOf(questionArrayList));
        });


    }
}