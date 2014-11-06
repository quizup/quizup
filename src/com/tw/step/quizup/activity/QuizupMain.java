package com.tw.step.quizup.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.quizup.R;
import com.firebase.client.Firebase;
import com.tw.step.quizup.services.QuizUpService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    private Drawable defaultButtonBackgroundDrawable;

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

        defaultButtonBackgroundDrawable = one.getBackground();

        questionArea = (TextView)findViewById(R.id.question);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                List<Object> questions = (List<Object>)extras.get("questions");
                if(questions != null && questions.size() > 0)
                    showQuestions(questions);
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

    public void onClick(View v) {
        clicked = true;
        Button button = (Button) v;
        String chosenAnswer = button.getText().toString();
        submitAnswer(chosenAnswer);
        changeColorOfSelectedButton(button);
    }

    private void changeColorOfSelectedButton(Button button) {
        button.setBackgroundColor(getResources().getColor(R.color.buttonClickedColor));
        button.setTextColor(Color.WHITE);
    }

    private void submitAnswer(String chosenAnswer) {
        TextView currentStringQuestion = ((TextView) findViewById(R.id.question));
        Object currentQuestion = myService.getCurrentQuestion(myService.getQuestions(), currentStringQuestion.getText().toString());
        Double millisecondDifference = (double) new Date().getTime() - questionShowingTime.getTime();
        double timeInSeconds = Double.parseDouble(String.format("%.2f", millisecondDifference / 1000));
        myService.putAnswerToFirebase(chosenAnswer, timeInSeconds, currentQuestion);
        setClickableAndRestoreBackground(false, one, two, three, four);
    }

    public void setClickableAndRestoreBackground(boolean setValue, View... v) {
        for (View view : v) {
            resetButtonColor(view);
            view.setClickable(setValue);
        }
    }

    private void resetButtonColor(View view) {
        view.setBackgroundDrawable(defaultButtonBackgroundDrawable);
        ((Button)view).setTextColor(Color.BLACK);
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
                        setClickableAndRestoreBackground(true, one, two, three, four);
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
        Integer questionInterval = Integer.parseInt(getResources().getString(R.string.questionInterval)) * 1000;
        timer.schedule(task, 0, questionInterval);
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
