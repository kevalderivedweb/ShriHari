package com.example.shreehari;

import android.util.Log;

import com.example.shreehari.UserSession.UserSession;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.e("newToken", token);
//Add your token in your sharepreferences.

        UserSession session = new UserSession(getApplicationContext());
        session.FirebaseToken(token);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

}