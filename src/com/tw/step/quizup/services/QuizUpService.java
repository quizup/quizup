package com.tw.step.quizup.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.tw.step.quizup.lib.FirebaseHelper;

import java.util.ArrayList;


public class QuizUpService extends Service{
    public final static String QUESTION_ACTION = "com.tw.step.quizup.QUESTION_RECIEVER";
    private FirebaseHelper firebaseHelper = new FirebaseHelper();
    private Firebase questionRef;
    private final String token = "lLwXjNDm5algYXbUEEWekyVr30cgH9nQVW3yiDAw";
    private final String environment = "test";
    private ArrayList<Object> questions = new ArrayList<Object>();


    @Override
    public IBinder onBind(Intent intent) {
        Firebase.setAndroidContext(this);
        questionRef = firebaseHelper.authenticateToFirebase("https://quizup.firebaseio.com/"+ environment +"/game/questions", token);


        questionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                questions = (ArrayList<Object>)dataSnapshot.getValue();
                broadcastQuestions();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("Data Read Error: ", firebaseError.getDetails());
            }
        });

        return new MyBinder();
    }

    public void broadcastQuestions() {
        Intent intent = new Intent();
        intent.putExtra("questions",questions);
        intent.setAction(QUESTION_ACTION);
        sendBroadcast(intent);
    }


    public class MyBinder extends Binder {

        public QuizUpService getService() {
            return QuizUpService.this;
        }
    }

}
