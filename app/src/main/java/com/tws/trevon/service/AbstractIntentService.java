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

import androidx.core.app.JobIntentService;

import com.tws.trevon.activity.AbstractActivity;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.common.NetworkHelper;
import com.tws.trevon.fragment.AbstractFragment;


//http://javapapers.com/android/android-alarm-clock-tutorial/
public abstract class AbstractIntentService extends JobIntentService
{
    private String TAG = AbstractIntentService.class.getSimpleName();

    protected abstract void onApiCallSuccess(Object responseValues, CallApi callApi);

    public AbstractIntentService(final String name) {
        super();
    }

    protected void processCallApi(final CallApi callApi)
    {
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
            public void onApiCallSuccess(String responseValues, CallApi callApi)
            {
                AbstractIntentService.this.onApiCallSuccess(responseValues, callApi);
            }

            @Override
            public void onApiCallError(CallApi callApi, final String errorCode)
            {
                AbstractIntentService.this.onApiCallError(callApi, errorCode);
            }
        });
    }

    public void onApiCallError(final CallApi callApi, final String errorCode)
    {

    }
}