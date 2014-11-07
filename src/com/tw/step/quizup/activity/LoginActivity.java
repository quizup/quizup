package com.tw.step.quizup.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.quizup.R;
import com.tw.step.quizup.lib.Doable;
import com.tw.step.quizup.services.LoginService;
import org.json.JSONException;

import java.io.IOException;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.widget.Toast.LENGTH_SHORT;
import static com.example.quizup.R.id;

public class LoginActivity extends Activity {
    private ServiceConnection serviceConnection;
    private LoginService loginService;
    private ProgressDialog waitLoader;
    private Doable startLoader = new Doable() {
        @Override
        public void doWork() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    waitLoader = ProgressDialog.show(LoginActivity.this, "Signing in. Please wait...", "", true);
                }
            });
        }
    };
    private Doable stopLoader = new Doable() {
        @Override
        public void doWork() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    waitLoader.dismiss();
                }
            });
        }
    };
    private Doable showInvalidMail = new Doable() {
        @Override
        public void doWork() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.this, "Invalid Login", LENGTH_SHORT).show();
                }
            });
        }
    };

    private Doable mainActivityStarter = new Doable() {
        @Override
        public void doWork() throws JSONException {
            Intent intent = new Intent(getBaseContext(), QuizupMain.class);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            loginService.setLoginDetails(intent);
            startActivity(intent);
            stopLoader.doWork();

        }
    };

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
                loginService.setStartLoader(startLoader);
                loginService.setStopLoader(stopLoader);
                loginService.setInvalidLoginAlert(showInvalidMail);
                loginService.setMainActivityStarter(mainActivityStarter);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.d("service", "Login Service Disconnected");
            }
        };
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

    }

    public void login(View view) throws IOException, JSONException {
        String email = ((EditText) findViewById(id.email)).getText().toString();
        String password = ((EditText) findViewById(id.password)).getText().toString();
        loginService.loginToGame(email, password);
    }
}
