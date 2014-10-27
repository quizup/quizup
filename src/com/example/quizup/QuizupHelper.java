package com.example.quizup;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

public class QuizupHelper {

    public void putAnswerToFirebase(String chosenAnswer,Map<String,Object> question,Firebase firebaseRef,String firebaseUrl,String token){
        FirebaseHelper helper = new FirebaseHelper();
        Map<String,String> answerDetails = new HashMap<String, String>() ;
        String isRightAnswer = chosenAnswer.equalsIgnoreCase(question.get("answer").toString()) ? "True":"False";

        answerDetails.put("rightAnswerGiven",isRightAnswer);
        answerDetails.put("timeTaken","5");
        if (firebaseRef == null) {
            firebaseRef = helper.authenticateToFirebase(firebaseUrl, token);
        }
        HashMap<String,Map> answerToQuestion = new HashMap<String, Map>();
        answerToQuestion.put(question.get("current_question").toString(), answerDetails);
        firebaseRef.setValue(question);
    }

}
