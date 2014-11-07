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
        String yourPoints = (String) extras.get("yourPoints");
        String opponentPoints = (String) extras.get("opponentPoints");
        TextView winnerView = (TextView) findViewById(R.id.win);
        TextView yourPointsView = (TextView) findViewById(R.id.yourPoints);
        TextView opponentPointsView = (TextView) findViewById(R.id.opponentPoints);
        winnerView.setText(winner + " wins..");
        yourPointsView.setText("You've got "+yourPoints+" points");
        opponentPointsView.setText("Your opponent got "+opponentPoints+" points");
    }
}
