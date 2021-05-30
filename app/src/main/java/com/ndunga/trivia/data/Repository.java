package com.ndunga.trivia.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ndunga.trivia.controller.AppController;
import com.ndunga.trivia.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Repository {

    private ArrayList<Question> questionArrayList = new ArrayList<>();
    String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestion(final answerListAsyncResponse callback){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    for (int i = 0; i < response.length() ; i++) {

                        try {
                            JSONArray jsonArray = response.getJSONArray(i);

                            Question question = new Question(jsonArray.getString(0),jsonArray.getBoolean(1));

                            //add question object to ArrayList
                            questionArrayList.add(question);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    //add this after the loop
                    if(null != callback) {
                        callback.processFinished(questionArrayList);
                    }

                }, error -> {
            Log.d("Error::::",error.toString());
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);


        return questionArrayList;
    }

}
