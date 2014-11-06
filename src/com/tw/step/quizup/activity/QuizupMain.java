package com.tw.step.quizup.activity;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.quizup.R;
import com.firebase.client.Firebase;
import com.tw.step.quizup.services.QuizUpService;

import java.util.*;

import static com.tw.step.quizup.services.QuizUpService.QUESTION_ACTION;

public class QuizupMain extends Activity {
    private QuizUpService quizUpService;


    private Button one;
    private Button two;
    private Button three;
    private Button four;
    private TextView questionArea;
    private BroadcastReceiver receiver;
    private IntentFilter question_receiver;
    private ServiceConnection serviceConnection;
    private QuizUpService myService;
    private Date questionShowingTime;
    private boolean clicked = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        question_receiver = new IntentFilter(QUESTION_ACTION);
        setContentView(R.layout.game);
        startQuizUpService();
        Firebase.setAndroidContext(this);
        one = (Button) findViewById(R.id.one);
        two = (Button) findViewById(R.id.two);
        three = (Button) findViewById(R.id.three);
        four = (Button) findViewById(R.id.four);
        questionArea = (TextView) findViewById(R.id.question);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                List<Object> questions = (List<Object>) extras.get("questions");
                if (questions != null) {
                    showQuestions(questions);
                }
            }
        };
        registerReceiver(receiver, question_receiver);
    }

    private void startQuizUpService() {
        final Intent intent = new Intent(getBaseContext(), QuizUpService.class);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                Log.d("service", "Service Connected");
                myService = ((QuizUpService.Factory) service).getService();
                myService.setLoginDetails(getIntent());
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.d("service", "Service Disconnected");
            }
        };
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    public void onClickButton(View v) {
        clicked = true;
        String chosenAnswer = ((Button) v).getText().toString();
        submitAnswer(chosenAnswer);

    }

    private void submitAnswer(String chosenAnswer) {
        TextView currentStringQuestion = ((TextView) findViewById(R.id.question));
        Object currentQuestion = myService.getCurrentQuestion(myService.getQuestions(), currentStringQuestion.getText().toString());
        Double millisecondDifference = (double) new Date().getTime() - questionShowingTime.getTime();
        double timeInSeconds = Double.parseDouble(String.format("%.2f", millisecondDifference / 1000));
        myService.putAnswerToFirebase(chosenAnswer, timeInSeconds, currentQuestion);
        setClickable(false, one, two, three, four);
    }

    public void setClickable(boolean setValue, View... v) {
        for (View view : v) {
            view.setClickable(setValue);
        }
    }

    public void showQuestions(final List<Object> questions) {
        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            Integer currentIndex = 0;

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!clicked)
                            submitAnswer("");
                        setClickable(true, one, two, three, four);
                        if (currentIndex == questions.size()) timer.cancel();
                        try {
                            Object question = questions.get(currentIndex);
                            setQuestion(question);
                        } catch (IndexOutOfBoundsException e) {
                        }
                        questionShowingTime = new Date();
                        currentIndex++;
                        clicked = false;
                    }
                });
            }
        };
        timer.schedule(task, 0, 5000);
    }

    private void setQuestion(Object question) {
        HashMap<String, Object> questionMap = (HashMap<String, Object>) question;
        List<String> options = (List<String>) questionMap.get("options");
        questionArea.setText(questionMap.get("question").toString());
        one.setText(options.get(0));
        two.setText(options.get(1));
        three.setText(options.get(2));
        four.setText(options.get(3));
    }

    @Override
    protected void onStop() {
        unregisterReceiver(receiver);
        unbindService(serviceConnection);
        super.onStop();
    }
}
