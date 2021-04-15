package com.mystudycanada.shreehari;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.mystudycanada.shreehari.API.ServerUtils;
import com.mystudycanada.shreehari.UserSession.UserSessionFirebase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static int NOTIFICATION_ID = 1;
    boolean is_noty = false;

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.e("newToken", token);
//Add your token in your sharepreferences.

        UserSessionFirebase session = new UserSessionFirebase(getApplicationContext());
        session.FirebaseToken(token);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("remoteMessage", ""+remoteMessage.getData().toString());
        Log.e("CollapseKey", ""+remoteMessage.getNotification());
        sendNotification(remoteMessage.getData());
    }



    private void sendNotification(Map<String, String> data) {

        // handle notification here
        /*
         * types of notification 1. result update 2. circular update 3. student
         * corner update 4. App custom update 5. Custom Message 6. Notice from
         * College custom
         */
        int num = ++NOTIFICATION_ID;
        Bundle msg = new Bundle();
        for (String key : data.keySet()) {
            Log.e(key, data.get(key));
            msg.putString(key, data.get(key));
        }
        Log.e("Type",msg.getString("type"));

        Intent backIntent;
        PendingIntent pendingIntent = null;
        backIntent = new Intent(getApplicationContext(), MPin_Activity.class);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        ServerUtils.TYPE = msg.getString("type");

        if (!is_noty) {

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String CHANNEL_ID = getString(R.string.app_name) + "channel_01";// The id of the channel.
            CharSequence name = getString(R.string.app_name);// The user-visible name of the channel.
            NotificationCompat.Builder mBuilder;

            int importance = NotificationManager.IMPORTANCE_HIGH;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

                mBuilder = new NotificationCompat.Builder(
                        this, CHANNEL_ID);



                mNotificationManager.createNotificationChannel(mChannel);

            } else {
                mBuilder = new NotificationCompat.Builder(
                        this, CHANNEL_ID);
            }

            pendingIntent = PendingIntent.getActivity(this, 0,
                    backIntent, 0);

            mBuilder.setSmallIcon(R.drawable.app_icon)
                    .setContentTitle(msg.getString("body"))
                    .setAutoCancel(true)
                    .setChannelId(CHANNEL_ID)
                    .setContentText(msg.getString("title"));

            //if (Integer.parseInt(msg.getString("type")) != 1) {
            mBuilder.setContentIntent(pendingIntent);


            mBuilder.setDefaults(Notification.DEFAULT_ALL);

            mNotificationManager.notify(++NOTIFICATION_ID, mBuilder.build());

        }

    }




}