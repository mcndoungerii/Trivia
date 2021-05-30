package com.ndunga.trivia.data;

import com.ndunga.trivia.model.Question;

import java.util.ArrayList;

public interface answerListAsyncResponse {

    void processFinished(ArrayList<Question> questionArrayList);
}
