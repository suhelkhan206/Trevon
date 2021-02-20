/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tws.trevon.service;

import android.content.Context;
import android.content.Intent;

import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.IPreferences;
import com.tws.trevon.constants.ISysConfig;


//http://javapapers.com/android/android-alarm-clock-tutorial/
public class AlarmService extends AbstractIntentService
{
    /*private NotificationManager alarmNotificationManager;*/

    public AlarmService() {
        super("AlarmService");
    }

    /**
     * Unique job ID for this service.
     */
    static final int JOB_ID = 1000;

    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, AlarmService.class, JOB_ID, work);
    }


    @Override
    public void onHandleWork(Intent intent)
    {
        /*try
        {
//            AppUtilities.showUserMessageInDevMode("AlarmService called");
            AppUtilities.writeLongToPref(IConstants.BACKGROUND_ALARM_LAST_TIME, (new Date()).getTime());

            if(!AppUtilities.readBooleanFromPref(IConstants.IS_USER_LOGGED_IN, false))
            {
                return;
            }

            checkForFcmRegToken();
            checkPendingMessage();
        }
        catch(Exception ex)
        {
            AppUtilities.processException(
                    ex,
                    "AlarmService onHandleIntent",
                    null,
                    AppUtilities.dumpIntent(intent));
        }*/
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

          /*  final CallApi callApi = new CallApi(API.fetchUserAndroidPendingMsg);
            callApi.isSilentCall = true;
            processCallApi(callApi);*/
        }
        catch(Exception ex)
        {
            AppUtilities.processException(
                    ex,
                    "AlarmService checkPendingMessage",
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

            *//*AppUtilities.showUserMessage("AlarmService Server response received " + msgList.size());*//*
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