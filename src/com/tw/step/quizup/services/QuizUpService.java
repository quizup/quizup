package com.tw.step.quizup.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.tw.step.quizup.activity.Winner;
import com.tw.step.quizup.lib.FirebaseHelper;
import com.tw.step.quizup.lib.QuizupHelper;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class QuizUpService extends Service {
    public final static String QUESTION_ACTION = "com.tw.step.quizup.QUESTION_RECIEVER";
    private FirebaseHelper firebaseHelper = new FirebaseHelper();
    private Firebase answerRef = null;
    private Firebase questionRef;

    private String token;
    private String gameUrl;
    private String username;
    private String playerId;

    private QuizupHelper helper = new QuizupHelper();

    public void setLoginDetails(Intent intent) {
        Bundle extras = intent.getExtras();
        token = (String) extras.get("token");
        gameUrl = (String) extras.get("gameUrl");
        username = (String) extras.get("username");
        playerId = (String) extras.get("playerId");
        initializeFirebaseReferences();
    }

    public void listenForWinner(String firebaseUrl, String token) {
        Firebase firebase = firebaseHelper.authenticateToFirebase(firebaseUrl, token);
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Intent intent = new Intent(getBaseContext(), Winner.class);
                if (dataSnapshot.getValue() != null) {
                    System.out.println(dataSnapshot.getValue());
                    HashMap<String,Object> winnerDetails = (HashMap<String, Object>) dataSnapshot.getValue();

                    String opponentPlayer = "player1";
                    String yourPlayer = "player2";
                    if (playerId.equalsIgnoreCase("player1")) {
                        yourPlayer = "player1";
                        opponentPlayer = "player2";
                    }
                    String yourPoints = ((HashMap)winnerDetails.get(yourPlayer)).get("points").toString();
                    String opponentPoints = ((HashMap)winnerDetails.get(opponentPlayer)).get("points").toString();
                    intent.putExtra("winner", winnerDetails.get("winner").toString());
                    intent.putExtra("yourPoints", yourPoints);
                    intent.putExtra("opponentPoints", opponentPoints);
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    private void initializeFirebaseReferences() {
        Firebase.setAndroidContext(this);
        questionRef = firebaseHelper.authenticateToFirebase(gameUrl + "/questions", token);
        answerRef = firebaseHelper.authenticateToFirebase(gameUrl + "/" + playerId + "/answers", token);
        startListenerForQuestions();
    }

    private void startListenerForQuestions() {
        questionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                questions = (ArrayList<Object>) dataSnapshot.getValue();
                broadcastQuestions();
                listenForWinner(gameUrl + "/gameStatus", token);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("Data Read Error: ", firebaseError.getDetails());
            }
        });
    }


    private ArrayList<Object> questions = new ArrayList<Object>();

    public ArrayList<Object> getQuestions() {
        return questions;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return new Factory();
    }

    public void broadcastQuestions() {
        Intent intent = new Intent();
        intent.putExtra("questions", questions);
        intent.setAction(QUESTION_ACTION);
        sendBroadcast(intent);
    }

    public Object getCurrentQuestion(ArrayList<Object> questions, String currentQuestionString) {
        return helper.getCurrentQuestion(questions, currentQuestionString);
    }

    public void putAnswerToFirebase(String chosenAnswer, double timeInSeconds, Object currentQuestion) {
        helper.putAnswerToFirebase(chosenAnswer, timeInSeconds, currentQuestion, answerRef);
    }

    public class Factory extends Binder {

        public QuizUpService getService() {
            return QuizUpService.this;
        }
    }

}
