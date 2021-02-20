package com.tws.trevon.service;

import android.app.job.JobParameters;
import android.os.Build;
import androidx.annotation.RequiresApi;

import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.IPreferences;
import com.tws.trevon.constants.ISysConfig;

/**
 * Created by User on 01-02-2018.
 */

@RequiresApi(Build.VERSION_CODES.M)
public class PushJobService extends AbstractJobService
{
    private static final String TAG = "PushJobService";

    @Override
    public boolean onStartJob(JobParameters params)
    {
        /*try
        {
            //AppUtilities.showUserMessageInDevMode("PushJobService called");
            AppUtilities.writeLongToPref(IConstants.BACKGROUND_ALARM_LAST_TIME, (new Date()).getTime());

            if(!AppUtilities.readBooleanFromPref(IConstants.IS_USER_LOGGED_IN, false))
            {
                return true;
            }

            checkForFcmRegToken();
            checkPendingMessage();
        }
        catch(Exception ex)
        {
            AppUtilities.processException(
                    ex,
                    "PushJobService onHandleIntent",
                    null,
                    "");
        }*/

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

    private void checkForFcmRegToken()
    {
        if(!AppUtilities.readBooleanFromPref(IPreferences.FCM_REGISTRATION_TOKEN_SAVED_ON_SERVER, false))
        {
            final String regToken = AppUtilities.readFromPref(IPreferences.FCM_REGISTRATION_TOKEN);
            if(AppValidate.isNotEmpty(regToken))
            {
               /* final CallApi callApi = new CallApi(API.fcmAndroidTokenRefresh);
                callApi.addReqParams("regToken", regToken);
                callApi.isSilentCall = true;
                processCallApi(callApi);*/
            }
        }
    }

    public void checkPendingMessage()
    {
        try
        {
            if (!AppUtilities.isInternetConnectionAvailable())
            {
                return;
            }

            if (!AppUtilities.isRefreshRequired(ISysConfig.FIVE_SECOND_TIME_IN_SECONDS, IPreferences.CHECK_PENDING_MESSAGES_LEO))
            {
                return;
            }

            /*final CallApi callApi = new CallApi(API.fetchUserAndroidPendingMsg);
            callApi.isSilentCall = true;
            processCallApi(callApi);*/
        }
        catch(Exception ex)
        {
            AppUtilities.processException(
                    ex,
                    "PushJobService checkPendingMessage",
                    null,
                    null);
        }
    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi)
    {
       /* if(API.fetchUserAndroidPendingMsg.method.equals(callApi.networkActivity.method))
        {
            ArrayList<LinkedTreeMap> msgList = (ArrayList<LinkedTreeMap>) ((LinkedTreeMap) responseValues).get("messageList");

            AppFireBaseMessagingService appFireBaseMessagingService = new AppFireBaseMessagingService();
            for(LinkedTreeMap serverMsgMap : Utilities.getNotNullList(msgList))
            {
                Map<String, String> data = new HashMap<>();
                data.put("type", (String)serverMsgMap.get("type"));
                data.put("serverData", (String)serverMsgMap.get("serverData"));
                appFireBaseMessagingService.handleNow(data);
            }
        }
        else if(API.fcmAndroidTokenRefresh.method.equals(callApi.networkActivity.method))
        {
            AppUtilities.writeBooleanToPref(IPreferences.FCM_REGISTRATION_TOKEN_SAVED_ON_SERVER, true);
        }*/
    }
}
