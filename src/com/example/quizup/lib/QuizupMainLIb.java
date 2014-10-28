package com.example.quizup.lib;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class QuizupMainLIb {
    public QuizupMainLIb() {
    }

    public Map<String, Map> createAnswerMap(String chosenAnswer, Map<String, Object> question) {
        Map<String, String> answerDetails = new HashMap<String, String>();
        String isRightAnswer = chosenAnswer.equalsIgnoreCase(question.get("answer").toString()) ? "True" : "False";
        answerDetails.put("rightAnswerGiven", isRightAnswer);
        answerDetails.put("timeTaken", "5");
        HashMap<String,Map> answerToQuestion = new HashMap<String, Map>();
        answerToQuestion.put(question.get("question").toString(), answerDetails);
        Log.d("answer details",answerToQuestion.toString());
        return answerToQuestion;
    }
}