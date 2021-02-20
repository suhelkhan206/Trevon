package com.tws.trevon.service;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tws.trevon.activity.MainActivity;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.common.Config;
import com.tws.trevon.common.NotificationUtils;
import com.tws.trevon.constants.API;
import com.tws.trevon.constants.IConstants;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiService extends AbstractIntentService
{
    public ApiService()
    {
        super("ApiService");
    }

    static final int JOB_ID = 1003;



    @Override
    public void onHandleWork(Intent intent)
    {
        try
        {

            callCfgTutorCalculatedPoints();
        }
        catch(Exception ex)
        {
            AppUtilities.processException(
                    ex,
                    "AlarmService onHandleIntent",
                    null,
                    AppUtilities.dumpIntent(intent));
        }
    }


    @Override
    public void onApiCallError(CallApi callApi, String errorCode)
    {
        super.onApiCallError(callApi, errorCode);
    }


    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi)
    {
        if (API.upDateFCMToken.method.equals(callApi.networkActivity.method))
        {
            Log.d("service_start_done","done"+responseValues);
        }
    }


    private void callCfgTutorCalculatedPoints()
    {
        final String token =  AppUtilities.readFromPref(IConstants.FCM_TOKEN);
        String userId = null;
        UserCO userCO =  UserCO.getUserCOFromDB();
        if(AppValidate.isNotEmpty(userCO.id))
        {
            userId = UserCO.getUserCOFromDB().id;
        }

        Handler handler = new Handler(Looper.getMainLooper());
        final String finalUserId = userId;
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                CallApi callApi = new CallApi(API.upDateFCMToken);
                callApi.addReqParams("user_id", finalUserId);
                callApi.addReqParams("token",token);
                callApi.processInBackground = true;
                callApi.isSilentCall = true;
                processCallApi(callApi);
            }
        });

    }

    /**
     * Created by Ravi Tamada on 08/08/16.
     * www.androidhive.info
     */
    public static class MyFirebaseMessagingService extends FirebaseMessagingService {

        private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

        private NotificationUtils notificationUtils;

        @Override
        public void onNewToken(String token)
        {
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


        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
            Log.e(TAG, "From: " + remoteMessage.getFrom());

            if (remoteMessage == null)
                return;

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
                handleNotification(remoteMessage.getNotification().getBody());
            }

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

                try {
                    JSONObject json = new JSONObject(remoteMessage.getData().toString());
                    handleDataMessage(json);
                } catch (Exception e) {
                    Log.e(TAG, "Exception: " + e.getMessage());
                }
            }
        }

        private void handleNotification(String message) {
            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            }else{
                // If the app is in background, firebase itself handles the notification
            }
        }

        private void handleDataMessage(JSONObject json)
        {
            Log.e(TAG, "push json: " + json.toString());

            try
            {
                JSONObject data = json.getJSONObject("data");

                String title = data.getString("body");
                String message = data.getString("body");
                // boolean isBackground = data.getBoolean("is_background");
                String imageUrl = data.getString("image");;
                String timestamp = "";
                //JSONObject payload = data.getJSONObject("payload");

                Log.e(TAG, "title: " + title);
                Log.e(TAG, "message: " + message);
                //  Log.e(TAG, "isBackground: " + isBackground);
                //  Log.e(TAG, "payload: " + payload.toString());
                Log.e(TAG, "imageUrl: " + imageUrl);
                Log.e(TAG, "timestamp: " + timestamp);

                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                    // app is in foreground, broadcast the push message
                    Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                    pushNotification.putExtra("message", message);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                    // play notification sound
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.playNotificationSound();
                } else {
                    // app is in background, show the notification in notification tray
                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                    resultIntent.putExtra("message", message);

                    // check for image attachment
                    if (TextUtils.isEmpty(imageUrl)) {
                        showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                    } else {
                        // image is present, show notification with image
                        showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, "Json Exception: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }

        /**
         * Showing notification with text only
         */
        private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
            notificationUtils = new NotificationUtils(context);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
        }

        /**
         * Showing notification with text and image
         */
        private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
            notificationUtils = new NotificationUtils(context);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
        }

        private void sendRegistrationToServer(final String token) {
            // sending gcm token to server
            Log.e(TAG, "sendRegistrationToServer: " + token);
        }

        private void storeRegIdInPref(String token) {
           /* SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("regId", token);
            editor.commit();*/

            AppUtilities.writeToPref(IConstants.FCM_TOKEN, token);
        }
    }
}