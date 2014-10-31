package com.tw.step.quizup.lib;


import com.firebase.client.Firebase;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class QuizupHelperTest {
    String myanswer = null;
    QuizupMainLIb quizupMainLIb = null;
    Firebase firebaseRef = null;
    QuizupHelper helper = null;
    Map<String,Object> question = null;
    Map<String,Object> answerMap = null;
    @Before
    public void setUp() throws Exception {
        myanswer = "MY_ANSWER";
        quizupMainLIb = mock(QuizupMainLIb.class);
        firebaseRef = mock(Firebase.class);
        helper = new QuizupHelper(quizupMainLIb);
        question = new HashMap<String, Object>();
        answerMap = new HashMap<String, Object>();
    }

    @Test
    public void creates_answer_to_put_on_firebase() {
        int timeTaken = 4;
        when(quizupMainLIb.createAnswerMap(myanswer, timeTaken, question)).thenReturn(answerMap);

        helper.putAnswerToFirebase(myanswer, timeTaken,question , firebaseRef);

        verify(quizupMainLIb,only()).createAnswerMap(myanswer, timeTaken, question);
    }

    @Test
    public void creates_answer_map_but_does_not_put_to_firebase() {
        int timeTaken = 4;
        when(quizupMainLIb.createAnswerMap(myanswer, timeTaken, question)).thenReturn(answerMap);
        helper.putAnswerToFirebase(myanswer,timeTaken, question, firebaseRef);
        verify(quizupMainLIb,only()).createAnswerMap(myanswer, timeTaken,question);
        verify(firebaseRef,times(0)).setValue(answerMap);
    }

    @Test
    public void creates_answer_map_and_puts_to_firebase_when_10th_answer_has_been_given() {
        int timeTaken = 4;
        when(quizupMainLIb.createAnswerMap(myanswer, timeTaken, question)).thenReturn(answerMap);
        for (int index = 0; index < 10; index++) {
            helper.putAnswerToFirebase(myanswer,timeTaken, question, firebaseRef);
        }
        verify(quizupMainLIb,times(10)).createAnswerMap(myanswer, timeTaken,question);
        verify(firebaseRef,only()).setValue(answerMap);
    }
}
