package com.tws.trevon.common;

import com.tws.trevon.activity.AbstractActivity;
import com.tws.trevon.fragment.AbstractFragment;

public interface NetworkHelper
{
    AbstractActivity getActivity();
    CallApi getCallApi();
    AbstractFragment getFragment();
    void onApiCallSuccess(String responseValues, CallApi callApi);
    void onApiCallError(CallApi callApi, String errorCode);
}
