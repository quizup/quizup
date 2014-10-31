package com.tw.step.quizup.lib;

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
        answerDetails.put("question", question.get("question").toString());
        HashMap<String,Map> answerToQuestion = new HashMap<String, Map>();
        answerToQuestion.put("current_answer", answerDetails);
        return answerToQuestion;
    }

}