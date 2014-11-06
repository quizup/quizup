package com.tw.step.quizup.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.example.quizup.R;
import com.tw.step.quizup.services.LoginService;

import java.io.IOException;

import static com.example.quizup.R.id;

public class LoginActivity extends Activity {
    private ServiceConnection serviceConnection;
    private LoginService loginService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        startLoginService();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void startLoginService() {
        Intent intent = new Intent(getApplicationContext(), LoginService.class);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                Log.d("service", "Login Service Connected");
                loginService = ((LoginService.LoginServiceFactory) service).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.d("service", "Login Service Disconnected");
            }
        };
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

    }

    public void login(View view) throws IOException {
        String email = ((EditText) findViewById(id.email)).getText().toString();
        String password = ((EditText) findViewById(id.password)).getText().toString();
        loginService.loginToGame(email, password);
    }
}
