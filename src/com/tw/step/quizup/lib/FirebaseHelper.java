package com.tw.step.quizup.lib;

import android.util.Log;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class FirebaseHelper {

    public Firebase authenticateToFirebase(String url, String token) {
        Firebase firebaseRef = new Firebase(url);
        firebaseRef.authWithCustomToken(token, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Log.d("Login Successful:", authData.toString());
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Log.e("Login Error :", firebaseError.getDetails());
            }
        });
        return firebaseRef;
    }

}