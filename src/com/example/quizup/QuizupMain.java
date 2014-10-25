package com.example.quizup;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.firebase.client.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizupMain extends Activity {
    private HashMap<String,Object> nextQuestion;
    private Firebase answerRef = null;
    private Firebase questionRef = null;
    private final String token = "lLwXjNDm5algYXbUEEWekyVr30cgH9nQVW3yiDAw";
    private final String game = "game";
    private final String player = "player_1";
    private Button one;
    private Button two;
    private Button three;
    private Button four;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        one = (Button) findViewById(R.id.one);
        two = (Button) findViewById(R.id.two);
        three = (Button) findViewById(R.id.three);
        four = (Button) findViewById(R.id.four);
        Firebase.setAndroidContext(this);
        if (questionRef == null){
            questionRef = authenticateToFirebase("https://quizup.firebaseio.com/test/"+game+"/current_question",token);
         }
        questionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView question = (TextView) findViewById(R.id.question);
                nextQuestion = (HashMap<String,Object>)dataSnapshot.getValue();
                Log.d("data: ", nextQuestion.toString());
                List<String> options = (List<String>)nextQuestion.get("options");
                setClickable(true,one,two,three,four);
                question.setText(nextQuestion.get("question").toString());
                one.setText(options.get(0));
                two.setText(options.get(1));
                three.setText(options.get(2));
                four.setText(options.get(3));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("Data Read Error: ",firebaseError.getDetails());
            }
        });
    }
    public void onClickButton(View v){
        String chosenAnswer = ((Button)v).getText().toString();
        Map<String,String> answerDetails = new HashMap<String, String>() ;
        String isRightAnswer = chosenAnswer.equalsIgnoreCase(nextQuestion.get("answer").toString()) ? "True":"False";

        answerDetails.put("rightAnswerGiven",isRightAnswer);
        answerDetails.put("timeTaken","5");
        if (answerRef == null) {
            answerRef = authenticateToFirebase("https://quizup.firebaseio.com/test/"+game+"/"+player,token);
        }
        HashMap<String,Map> answerToQuestion = new HashMap<String, Map>();
        answerToQuestion.put(nextQuestion.get("question").toString(), answerDetails);
        Log.d("Answer chose:",answerToQuestion.toString());
        answerRef.setValue(answerToQuestion);
        setClickable(false,one,two,three,four);
    }

    public Firebase authenticateToFirebase(String url,String token){
        Firebase firebaseRef = new Firebase(url);
        firebaseRef.authWithCustomToken(token, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Log.d("Login Successful:", authData.toString());
            }
            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Log.e("Login Error", firebaseError.getDetails());
            }
        });
        return firebaseRef;
    }

    public void setClickable(boolean setValue,View... v){
        for (View view : v) {
                view.setClickable(setValue);
        }
    }
}
