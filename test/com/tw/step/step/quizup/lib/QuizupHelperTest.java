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
    Map<String,Map> answerMap = null;
    @Before
    public void setUp() throws Exception {
        myanswer = "MY_ANSWER";
        quizupMainLIb = mock(QuizupMainLIb.class);
        firebaseRef = mock(Firebase.class);
        helper = new QuizupHelper(quizupMainLIb);
        question = new HashMap<String, Object>();
        answerMap = new HashMap<String, Map>();
    }

    @Test
    public void creates_answer_to_put_on_firebase() {
        when(quizupMainLIb.createAnswerMap(myanswer,question)).thenReturn(answerMap);

        helper.putAnswerToFirebase(myanswer, question, firebaseRef);

        verify(quizupMainLIb,only()).createAnswerMap(myanswer,question);
    }

    @Test
    public void creates_answer_and_puts_back_to_firebase() {
        when(quizupMainLIb.createAnswerMap(myanswer,question)).thenReturn(answerMap);
        helper.putAnswerToFirebase(myanswer, question, firebaseRef);
        verify(quizupMainLIb,only()).createAnswerMap(myanswer,question);
        verify(firebaseRef,only()).setValue(answerMap);
    }
}
