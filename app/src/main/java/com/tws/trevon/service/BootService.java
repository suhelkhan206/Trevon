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

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.CallApi;

//http://javapapers.com/android/android-alarm-clock-tutorial/
public class BootService extends AbstractIntentService
{
    private NotificationManager alarmNotificationManager;

    public BootService() {
        super("BootService");
    }

    /**
     * Unique job ID for this service.
     */
    static final int JOB_ID = 1002;

    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, BootService.class, JOB_ID, work);
    }

    @Override
    public void onHandleWork(Intent intent)
    {
        try
        {
        //    AppUtilities.sendNotification("Reboot", null);

            AppUtilities.startScheduledService(this);
        }
        catch(Exception ex)
        {
            AppUtilities.processException(
                    ex,
                    "BootService onHandleIntent",
                    null,
                    AppUtilities.dumpIntent(intent));
        }
    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {

    }
}