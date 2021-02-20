package com.tws.trevon.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.Config;
import com.tws.trevon.constants.IConstants;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        //  Toast.makeText(this, token, Toast.LENGTH_SHORT).show();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

    }

    private void storeRegIdInPref(String token) {
      /*  SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();*/

        AppUtilities.writeToPref(IConstants.FCM_TOKEN, token);
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
    }


    // Override onMessageReceived() method to extract the
    // title and
    // body from the message passed in FCM
    @Override
    public void
    onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "From: " + remoteMessage.getFrom());
        // First case when notifications are received via
        // data event
        // Here, 'title' and 'message' are the assumed names
        // of JSON
        // attributes. Since here we do not have any data
        // payload, This section is commented out. It is
        // here only for reference purposes.
        /*if(remoteMessage.getData().size()>0){
            showNotification(remoteMessage.getData().get("title"),
                          remoteMessage.getData().get("message"));
        }*/

        // Second case when notification payload is
        // received.
        if (remoteMessage.getNotification() != null) {
            // Since the notification is received directly from
            // FCM, the title and the body can be fetched
            // directly as below.
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            //showNotificationDefault(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            Log.d("EEEE", "onMessageReceived: " + remoteMessage.getNotification().getTitle()
                    + "NNNNN: " + remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e("TAGGG", "Data Payload: " + String.valueOf(remoteMessage.getData()));

            try {
                JSONObject json = new JSONObject(String.valueOf(remoteMessage.getData()));
                handleDataMessage(json);

                Log.d("EEEEEE", "onMessageReceived: " + json);
            } catch (Exception e) {
                Log.e("TAGGGG", "Exception: " + e.getMessage());
            }
        }

    }

    private void handleDataMessage(JSONObject json) {
        Log.e("TAG", "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title;
//            if (data.getString("title") == null){
//                Log.d("WWWWWW", "handleDataMessage: " + data.getString("title"));
//                title = data.getString("body");
//            } else {
//                Log.d("WWWWWW", "handleDataMessage: " + data.getString("title"));
//                title = data.getString("title");
//            }

            title = data.getString("title");
            String message = data.getString("body");
            // boolean isBackground = data.getBoolean("is_background");
            //  String sound = data.getString("sound");
            String imageUrl = data.getString("image");
            ;
            String timestamp = "";
            //JSONObject payload = data.getJSONObject("payload");

            Log.e("TAG", "title: " + title);
            Log.e("TAG", "message: " + message);
            //  Log.e(TAG, "isBackground: " + isBackground);
            //  Log.e(TAG, "payload: " + payload.toString());
            Log.e("TAG", "imageUrl: " + imageUrl);
            Log.e("TAG", "timestamp: " + timestamp);
            showNotification(title, message);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

//    // Method to get the custom Design for the display of
//    // notification.
//    private RemoteViews getCustomDesign(String title, String message) {
//
//        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification_layout);
//        remoteViews.setTextViewText(R.id.title, title);
//        remoteViews.setTextViewText(R.id.message, message);
//        Log.d("RRRRR", "getCustomDesign: " + title + "MMMM::" + message);
//        remoteViews.setImageViewResource(R.id.icon, R.mipmap.ic_launcher);
//        return remoteViews;
//    }
//
//    public void showNotificationDefault(String title,String message){
//
//    }

    // Method to display the notifications
    public void showNotification(String title, String message) {

//        // Pass the intent to switch to the MainActivity
//        Intent intent = new Intent(getApplicationContext()  , MainActivity.class);
//
//        // Assign channel ID
//        String channel_id = "notification_channel";
//
//        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
//        // the activities present in the activity stack,
//        // on the top of the Activity that is to be launched
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        // Pass the intent to PendingIntent to start the
//        // next Activity
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
//
//        // Create a Builder object using NotificationCompat
//        // class. This will allow control over all the flags
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setAutoCancel(true)
//                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
//                .setOnlyAlertOnce(true)
//                .setContentIntent(pendingIntent);
//
//
//        // A customized design for the notification can be
//        // set only for Android versions 4.1 and above. Thus
//        // condition for the same is checked here.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            builder = builder.setContent(getCustomDesign(title, message));
//            Log.d("EEEE", "showNotification: " + title + "MMM: " + message);
//        }
//
//        // If Android Version is lower than Jelly Beans,
//        // customized layout cannot be used and thus the
//        // layout is set as follows
//        else {
//            builder = builder.setContentTitle(title).setContentText(message)
//                    .setSmallIcon(R.mipmap.ic_launcher);
//            Log.d("FFFFF", "showNotification: " + title + "MMM: " + message);
//        }
//
//
//        // Create an object of NotificationManager class to
//        // notify the
//        // user of events that happen in the background.
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // Check if the Android Version is greater than Oreo
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(channel_id, "Notification Channel",
//                    NotificationManager.IMPORTANCE_HIGH);
//            notificationManager.createNotificationChannel(notificationChannel);
//            Log.d("GGGGGG", "showNotification: " + title + "MMM: " + message);
//        }
//
        /*
         * If the device is having android oreo we will create a notification channel
         * */
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            //Adding notification sound
//            AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                    .setUsage(AudioAttributes.USAGE_ALARM)
//                    .build();
//            mChannel.setSound(Uri.parse(sound), audioAttributes);

            mNotificationManager.createNotificationChannel(mChannel);
        }

        MyNotificationManager.getInstance(getApplicationContext()).displayNotification(title,message);

        //notificationManager.notify(0, builder.build());
    }

}