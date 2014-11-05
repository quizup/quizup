package com.tw.step.quizup.lib;


import com.firebase.client.Firebase;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class QuizupHelperTest {
    String myanswer = null;
    Firebase firebaseRef = null;
    QuizupHelper helper = null;
    Map<String,Object> question = null;
    Map<String,Object> answerMap = null;
    @Before
    public void setUp() throws Exception {
        myanswer = "MY_ANSWER";
        firebaseRef = mock(Firebase.class);
        helper = new QuizupHelper();
        question = new HashMap<String, Object>();
        question.put("answer", "SOME-ANSWER");
        answerMap = new HashMap<String, Object>();
    }

    @Test
    public void creates_answer_to_put_on_firebase() {
        Double timeTaken = Double.valueOf(4);

        helper.putAnswerToFirebase(myanswer, timeTaken, question, firebaseRef);

        assertEquals( helper.getAllAnswers().size(), 1);
        verify(firebaseRef,times(0)).setValue(this.answerMap);
    }

    @Test
    public void creates_answer_map_but_does_not_put_to_firebase() {
        double timeTaken = 4;
        helper.putAnswerToFirebase(myanswer, timeTaken, question, firebaseRef);

        assertEquals( helper.getAllAnswers().size(), 1);
        verify(firebaseRef,times(0)).setValue(answerMap);
    }

    @Test
    public void creates_answer_map_and_puts_to_firebase_when_10th_answer_has_been_given() {
        double timeTaken = 4;
        for (int index = 0; index < 10; index++) {
            helper.putAnswerToFirebase(myanswer,timeTaken, question, firebaseRef);
        }
        verify(firebaseRef,only()).setValue(anyList());
    }

    @Test
    public void creates_a_hashmap_of_answer_when_given_question_and_chosen_answer() {
        final String ANSWER = "ANSWER";
        final String QUESTION = "Is this my question?";
        Map<String,Object> questionMap = new HashMap<String, Object>();
        questionMap.put("answer", ANSWER);
        questionMap.put("question", QUESTION);
        Map<String,Object> answerMap =  helper.createAnswerMap(ANSWER, Double.valueOf(5), questionMap);

        assertEquals(true,answerMap.get("rightAnswerGiven"));
        assertEquals(Double.valueOf(5),answerMap.get("timeTaken"));
    }

    @Test
    public void says_wrong_answer_when_wrong_option_chosen() {
        final String ANSWER = "ANSWER";
        final String CHOSEN_ANSWER = "CHOSEN_ANSWER";
        final String QUESTION = "Is this my question?";
        Map<String,Object> questionMap = new HashMap<String, Object>();
        questionMap.put("answer", ANSWER);
        questionMap.put("question", QUESTION);
        Map<String,Object> answerMap =  helper.createAnswerMap(CHOSEN_ANSWER,Double.valueOf(5),questionMap);
        assertEquals(false,answerMap.get("rightAnswerGiven"));
        assertEquals(Double.valueOf(5),answerMap.get("timeTaken"));
    }

}
