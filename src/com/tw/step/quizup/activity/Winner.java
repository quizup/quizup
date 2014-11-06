package com.tw.step.quizup.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.example.quizup.R;

public class Winner extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String winner = (String) extras.get("winner");
        TextView view = (TextView) findViewById(R.id.win);
        view.setText(winner + " wins..");
    }
}
