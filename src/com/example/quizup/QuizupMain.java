package com.example.quizup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

public class QuizupMain extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        ProgressDialog progressDialog = new ProgressDialog(this,ProgressDialog.THEME_DEVICE_DEFAULT_DARK);
        progressDialog.setMessage("Wait while other player joins...");
        progressDialog.show();
    }
}
