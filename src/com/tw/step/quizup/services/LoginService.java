package com.tw.step.quizup.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.example.quizup.R;
import com.tw.step.quizup.lib.Doable;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static java.net.URLDecoder.decode;

public class LoginService extends Service {
    public static String LOGIN_DETAILS_ACTION = "com.tw.step.quizup.LOGIN_DETAILS_RECEIVER";
    private JSONObject jsonresponse;

    private Doable mainActivityStarter;
    private Doable startLoader = null;
    private Doable stopLoader = null;
    private Doable invalidLoginAlert = null;

    public void setStartLoader(Doable startLoader) {
        this.startLoader = startLoader;
    }

    public void setStopLoader(Doable stopLoader) {
        this.stopLoader = stopLoader;
    }

    public void setInvalidLoginAlert(Doable invalidLoginAlert) {
        this.invalidLoginAlert = invalidLoginAlert;
    }

    public void setMainActivityStarter(Doable mainActivityStarter) {
        this.mainActivityStarter = mainActivityStarter;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return new LoginServiceFactory();
    }

    public void loginToGame(String email, String password) throws IOException {
        String loginUrl = getResources().getString(R.string.gameUrl);
        String credentials = prepareCredentials(email, password);
        final HttpClient client = new DefaultHttpClient();
        final HttpPost post = new HttpPost(loginUrl + credentials);

        Thread loginThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startLoader.doWork();
                    HttpResponse response = client.execute(post);
                    jsonresponse = new JSONObject(EntityUtils.toString(response.getEntity()));
                    int statusCode = response.getStatusLine().getStatusCode();
                    if(statusCode == 404){
                        stopLoader.doWork();
                        invalidLoginAlert.doWork();
                    }
                    else{
                        mainActivityStarter.doWork();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        loginThread.start();
    }

    public void setLoginDetails(Intent intent) throws JSONException {
        intent.putExtra("token", (String) jsonresponse.get("token"));
        intent.putExtra("gameUrl", (String) jsonresponse.get("gameUrl"));
        intent.putExtra("username", (String) jsonresponse.get("username"));
        intent.putExtra("playerId", (String) jsonresponse.get("player"));
        intent.setAction(LOGIN_DETAILS_ACTION);
    }

    private String prepareCredentials(String email, String password) throws UnsupportedEncodingException {
        List<NameValuePair> credentials = new ArrayList<NameValuePair>();
        credentials.add(new BasicNameValuePair("email", email));
        credentials.add(new BasicNameValuePair("password", password));
        String urlParams = URLEncodedUtils.format(credentials, "utf-8");
        return decode(urlParams, "utf-8");
    }

    public class LoginServiceFactory extends Binder {

        public LoginService getService() {
            return LoginService.this;
        }
    }

}
