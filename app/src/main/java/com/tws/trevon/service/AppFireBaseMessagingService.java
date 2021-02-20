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

import android.content.Intent;

import com.tws.trevon.co.NotificationCO;
import com.tws.trevon.co.ServerMessageTrackerCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.constants.IConstants;
import com.tws.trevon.constants.IReqParams;
import com.tws.trevon.constants.ISysCodes;
import com.tws.trevon.constants.ISysConfig;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Date;
import java.util.Map;


public class AppFireBaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "AppFireBaseMessagingService";

    public static final Gson gson = new Gson();

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0)
        {
            if (/* Check if data needs to be processed by long running job */ false) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow(remoteMessage.getData());
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null)
        {

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {

    }

    public void handleNow(final Map<String, String> data)
    {
        final String notifType = data.get(IReqParams.NOTIFICATION_TYPE_REQUEST_PARAMETER);

        if(isAlreadyProcessed(data.get(IReqParams.MESSAGE_ID_REQUEST_PARAMETER)))
        {
            return;
        }

        if (AppValidate.isEmpty(notifType))
        {
            AppUtilities.showUserMessage("Empty Notification type received.");
        }
        else if(isValidUser(data.get(IReqParams.USER_ID_REQUEST_PARAMETER), data.get(IReqParams.COMMON_GCM_MESSAGE)))
        {
            if (notifType.equals(ISysCodes.NOTIFICATION_TYPE_NOTIF))
            {
                processGcmUserNotification(data);
            }
            else if (notifType.equals(ISysCodes.NOTIFICATION_TYPE_UPDATE))
            {
                processGcmUserNotification(data);
                processGcmUpdateNotification(data);
            }
            else
            {
                //ignore
                AppUtilities.showUserMessage("Invalid Notification.");
            }
        }
    }

    private void processGcmUserNotification(final Map<String, String> data)
    {
        try
        {
            String notificationCOJson = data.get(ISysCodes.NOTIFICATION_CO);
            if(AppValidate.isNotEmpty(notificationCOJson))
            {
                NotificationCO notificationCO = gson.fromJson(data.get(ISysCodes.NOTIFICATION_CO), NotificationCO.class);

                notificationCO.saveInDB();
                AppUtilities.writeToPref(IReqParams.LAST_NOTIF_ID, data.get(IReqParams.LAST_NOTIF_ID));

                AppUtilities.sendDefaultNotification(notificationCO.title, notificationCO.description, getResultIntentForNotifications());
            }
        }
        catch(Exception ex)
        {
            AppUtilities.processException(
                    ex,
                    "processGcmUserNotification",
                    null,
                    data);
        }
    }

    private Intent getResultIntentForNotifications()
    {
        Intent resultIntent = new Intent(AppController.getInstance(), AppUtilities.getHomeClass());
        resultIntent.putExtra(IConstants.IS_NOTIF_CLICK, true);
        resultIntent.putExtra(IConstants.FORWARD_TO, IConstants.NOTIFICATIONS);
        return resultIntent;
    }

    public void processGcmUpdateNotification(final Map<String, String> data)
    {
        try
        {
            final int updateType = Integer.valueOf(data.get(ISysCodes.UPDATE_NOTIF_TYPE));

            switch (updateType)
            {
                case ISysConfig.CUSTOM_NOTIFICATION_TASK_UPDATE_TYPE:
                {

                    break;
                }

                case ISysConfig.SAVE_QUERY_UPDATE_TYPE:
                {

                    break;
                }

                case ISysConfig.FORWARD_QUERY_UPDATE_TYPE:
                {
                 //   QueryCO queryCO = AppController.gson.fromJson((String)data.get(ISysCodes.UPDATE_NOTIF_DATA), QueryCO.class);
                    //queryCO.saveInDB();
                    break;
                }

                default:
                {
                    AppUtilities.showUserMessage("Invalid Update type.");
                }
            }
        }
        catch(Exception ex)
        {
            String updateType = (String)data.get(ISysCodes.UPDATE_NOTIF_TYPE);
            String notifData = (String)data.get(ISysCodes.UPDATE_NOTIF_DATA);
            String relevantData = IConstants.EMPTY_STRING;

            if(AppValidate.isNotEmpty(updateType))
            {
                relevantData = relevantData + "UpdateType : " +updateType;
            }

            if(AppValidate.isNotEmpty(notifData))
            {
                relevantData = relevantData + ", NotifData : " + notifData;
            }

            relevantData = relevantData + ", Extra data values : " + gson.toJson(relevantData);

            AppUtilities.processException(
                    ex,
                    "processGcmUpdateNotification",
                    null,
                    relevantData);
        }
    }

   /* private Intent getResultIntentForOrgDashboard()
    {
        Intent resultIntent = new Intent(AppController.getInstance(), Utilities.getHomeClass());
        resultIntent.putExtra(IConstants.IS_NOTIF_CLICK, true);
        resultIntent.putExtra(IConstants.FORWARD_TO, IConstants.ORG_DASHBOARD);
        resultIntent.putExtra(IReqParams.ORG_ID_REQUEST_PARAMETER, organization.orgId);
        return resultIntent;
    }*/

    private boolean isAlreadyProcessed(final String messageId)
    {
        boolean isAlreadyProcessed = false;
        if(AppValidate.isEmpty(messageId))
        {
            isAlreadyProcessed = false;
        }
        else
        {
            ServerMessageTrackerCO serverMessageTrackerCO = ServerMessageTrackerCO.getServerMessageTrackerCOByMessageId(messageId);
            if(serverMessageTrackerCO == null)
            {
                isAlreadyProcessed = false;
                serverMessageTrackerCO = new ServerMessageTrackerCO();
                serverMessageTrackerCO.messageId = messageId;
                serverMessageTrackerCO.createdOn = Long.toString((new Date()).getTime());
                serverMessageTrackerCO.saveInDB();
            }
            else
            {
                isAlreadyProcessed = true;
            }
        }

        return isAlreadyProcessed;
    }

    private boolean isValidUser(final String userIdInMsg, final String isCommonMsg)
    {
        boolean isValidUser;
        if(AppValidate.isEmpty(userIdInMsg))
        {
            if(IConstants.YES.equals(isCommonMsg))
            {
                isValidUser = true;
            }
            else
            {
                isValidUser = false;
            }
        }
        else
        {
            final String storedUserId = AppUtilities.readFromPref(IReqParams.LOGGED_IN_USER_ID);
            isValidUser = userIdInMsg.equals(storedUserId);
        }

/*        if(!isValidUser)
        {
            if(ISysConfig.isPersonalApp)
            {
                if(!ISysConfig.isLoginCompulsory)
                {
                    isValidUser = true;
                }
            }
        }*/

        return isValidUser;
    }
}