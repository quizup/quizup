package com.tw.step.quizup.lib;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class QuizupMainLIbTest {
    @Test
    public void creates_a_hashmap_of_answer_when_given_question_and_chosen_answer() {
        final String ANSWER = "ANSWER";
        final String QUESTION = "Is this my question?";
        QuizupMainLIb lIb = new QuizupMainLIb();
        Map<String,Object> questionMap = new HashMap<String, Object>();
        questionMap.put("answer", ANSWER);
        questionMap.put("question", QUESTION);
        Map<String,Object> answerMap =  lIb.createAnswerMap(ANSWER, Double.valueOf(5), questionMap);

        assertEquals(true,answerMap.get("rightAnswerGiven"));
        assertEquals(Double.valueOf(5),answerMap.get("timeTaken"));
    }

    @Test
    public void says_wrong_answer_when_wrong_option_chosen() {
        final String ANSWER = "ANSWER";
        final String CHOSEN_ANSWER = "CHOSEN_ANSWER";
        final String QUESTION = "Is this my question?";
        QuizupMainLIb lIb = new QuizupMainLIb();
        Map<String,Object> questionMap = new HashMap<String, Object>();
        questionMap.put("answer", ANSWER);
        questionMap.put("question", QUESTION);
        Map<String,Object> answerMap =  lIb.createAnswerMap(CHOSEN_ANSWER,Double.valueOf(5),questionMap);
        assertEquals(false,answerMap.get("rightAnswerGiven"));
        assertEquals(Double.valueOf(5),answerMap.get("timeTaken"));
    }
}
