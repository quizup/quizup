package com.tw.step.quizup.lib;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizupHelper {
    private final QuizupMainLIb quizupMainLIb;
    private List<Map<String, Object>> allAnswers;

    public QuizupHelper(QuizupMainLIb quizupMainLIb) {
        this.quizupMainLIb = quizupMainLIb;
        this.allAnswers = new ArrayList<Map<String, Object>>();
    }
    public void putAnswerToFirebase(String chosenAnswer, Integer timeTaken, Object currentQuestion, Firebase answerRef){
        Map<String, Object> answerDetail = quizupMainLIb.createAnswerMap(chosenAnswer, timeTaken, currentQuestion);
        allAnswers.add(answerDetail);
        if(allAnswers.size() == 10)
            answerRef.setValue(allAnswers);
    }


    public Object getCurrentQuestion(ArrayList<Object> questions, String currentQuestion) {
        for (Object question : questions) {
            HashMap<String, Object> questionMap = (HashMap<String, Object>) question;
            if(questionMap.get("question").toString().equalsIgnoreCase(currentQuestion))
                return question;
        }
        return null;
    }
}
