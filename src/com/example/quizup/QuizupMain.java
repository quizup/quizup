package com.example.quizup;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class QuizupMain extends Activity {
    private final FirebaseHelper firebaseHelper = new FirebaseHelper();
    private final QuizupHelper quizupHelper = new QuizupHelper();
    private final String token = "lLwXjNDm5algYXbUEEWekyVr30cgH9nQVW3yiDAw";
    private final String game = "game";
    private final String player = "player_1";
    private HashMap<String,Object> nextQuestion;
    private Firebase questionRef = null;
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
            questionRef = firebaseHelper.authenticateToFirebase("https://quizup.firebaseio.com/test/" + game + "/current_question", token);
         }
        questionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView question = (TextView) findViewById(R.id.question);
                nextQuestion = (HashMap<String, Object>) dataSnapshot.getValue();
                Log.d("data: ", nextQuestion.toString());
                List<String> options = (List<String>) nextQuestion.get("options");
                setClickable(true, one, two, three, four);
                question.setText(nextQuestion.get("current_question").toString());
                one.setText(options.get(0));
                two.setText(options.get(1));
                three.setText(options.get(2));
                four.setText(options.get(3));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("Data Read Error: ", firebaseError.getDetails());
            }
        });
    }
    public void onClickButton(View v){
        String chosenAnswer = ((Button)v).getText().toString();
        quizupHelper.putAnswerToFirebase(chosenAnswer,nextQuestion,questionRef,"https://quizup.firebaseio.com/test/" + game + "/" + player,token);
        Log.d("Answer chosen:", chosenAnswer);
        setClickable(false, one, two, three, four);
    }

    public void setClickable(boolean setValue,View... v){
        for (View view : v) {
                view.setClickable(setValue);
        }
    }
}
