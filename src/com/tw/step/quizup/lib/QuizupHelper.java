package com.tw.step.quizup.lib;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizupHelper {
    private List<Map<String, Object>> allAnswers;

    public List<Map<String, Object>> getAllAnswers() {
        return allAnswers;
    }

    public QuizupHelper() {
        this.allAnswers = new ArrayList<Map<String, Object>>();
    }
    public void putAnswerToFirebase(String chosenAnswer, Double timeTaken, Object currentQuestion, Firebase answerRef){
        Map<String, Object> answerDetail = createAnswerMap(chosenAnswer, timeTaken, currentQuestion);
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

    public Map<String, Object> createAnswerMap(String chosenAnswer, Double timeTaken, Object questionObject) {
        HashMap<String, Object> question = (HashMap<String, Object>) questionObject;
        Map<String, Object> answerDetails = new HashMap<String, Object>();

        Boolean isRightAnswer = chosenAnswer.equalsIgnoreCase(question.get("answer").toString());
        answerDetails.put("rightAnswerGiven", isRightAnswer);
        answerDetails.put("timeTaken", timeTaken);
        return answerDetails;
    }

}
