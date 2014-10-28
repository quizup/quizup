package com.example.quizup.lib;

import com.firebase.client.Firebase;

import java.util.Map;

public class QuizupHelper {
    private final QuizupMainLIb quizupMainLIb;

    public QuizupHelper(QuizupMainLIb quizupMainLIb) {
        this.quizupMainLIb = quizupMainLIb;
    }
    public void putAnswerToFirebase(String chosenAnswer,Map<String,Object> question,Firebase firebaseRef){
        Map<String, Map> answerDetails = quizupMainLIb.createAnswerMap(chosenAnswer, question);
        firebaseRef.setValue(answerDetails);
    }

}
