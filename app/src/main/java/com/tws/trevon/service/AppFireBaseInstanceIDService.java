package com.tws.trevon.service;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.constants.IPreferences;


public class AppFireBaseInstanceIDService extends FirebaseInstanceIdService
{
    private static final String TAG = "AppFireBaseInstanceIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh()
    {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        AppUtilities.writeToPref(IPreferences.FCM_REGISTRATION_TOKEN, refreshedToken);
        AppUtilities.writeBooleanToPref(IPreferences.FCM_REGISTRATION_TOKEN_SAVED_ON_SERVER, false);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
  /*  private void sendRegistrationToServer(String token)
    {
        boolean isUserLoggedIn = AppUtilities.readBooleanFromPref(IConstants.IS_USER_LOGGED_IN, false);

        if(isUserLoggedIn)
        {
           *//* final CallApi callApi = new CallApi(API.fcmAndroidTokenRefresh);
            callApi.addReqParams("regToken", token);
            callApi.isSilentCall = true;*//*

            AppUtilities.processCallApi(new NetworkHelper() {
                @Override
                public AbstractActivity getActivity() {
                    return null;
                }

                @Override
                public CallApi getCallApi() {
                    return callApi;
                }

                @Override
                public AbstractFragment getFragment() {
                    return null;
                }

                @Override
                public void onApiCallSuccess(String responseValues, CallApi callApi) {

                }

                @Override
                public void onApiCallSuccess(Object responseValues, CallApi callApi)
                {
                    AppUtilities.writeBooleanToPref(IPreferences.FCM_REGISTRATION_TOKEN_SAVED_ON_SERVER, true);
                }

                @Override
                public void onApiCallError(CallApi callApi, final String errorCode)
                {
                    //do nothing
                }
            });
        }
    }*/
}