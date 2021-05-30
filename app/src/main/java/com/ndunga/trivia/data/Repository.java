package com.ndunga.trivia.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ndunga.trivia.controller.AppController;
import com.ndunga.trivia.model.Question;

import java.util.ArrayList;
import java.util.List;

public class Repository {

    private ArrayList<Question> questionArrayList = new ArrayList<>();
    String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestion(){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("Repo::::",response.toString());

                }, error -> {
            Log.d("Error::::",error.toString());
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return null;
    }

}
