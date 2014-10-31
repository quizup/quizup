package com.tw.step.quizup.lib;

import java.util.HashMap;
import java.util.Map;

public class QuizupMainLIb {
    public QuizupMainLIb() {
    }

    public Map<String, Object> createAnswerMap(String chosenAnswer, Integer timeTaken, Object questionObject) {
        HashMap<String, Object> question = (HashMap<String, Object>) questionObject;
        Map<String, Object> answerDetails = new HashMap<String, Object>();

        Boolean isRightAnswer = chosenAnswer.equalsIgnoreCase(question.get("answer").toString());
        answerDetails.put("rightAnswerGiven", isRightAnswer);
        answerDetails.put("timeTaken", timeTaken);
        return answerDetails;
    }

}