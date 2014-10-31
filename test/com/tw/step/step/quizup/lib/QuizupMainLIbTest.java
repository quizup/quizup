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
        Map<String,Map> answerMap =  lIb.createAnswerMap(ANSWER,questionMap);
        Map<String,String> answerDetails = answerMap.get("current_answer");
        assertEquals("True",answerDetails.get("rightAnswerGiven"));
        assertEquals("5",answerDetails.get("timeTaken"));
        assertEquals(QUESTION,answerDetails.get("question"));
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
        Map<String,Map> answerMap =  lIb.createAnswerMap(CHOSEN_ANSWER,questionMap);
        Map<String,String> answerDetails = answerMap.get("current_answer");
        assertEquals("False",answerDetails.get("rightAnswerGiven"));
        assertEquals("5",answerDetails.get("timeTaken"));
        assertEquals(QUESTION,answerDetails.get("question"));
    }
}
