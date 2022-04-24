package com.example.myapplication.firebase;


//import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {
    private final static String TAG = "tag";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        Log.d("FCM" , "Message " +remoteMessage.getNotification().getBody());

    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
//        Log.d("FCM" , "Token " + token);
    }
}
