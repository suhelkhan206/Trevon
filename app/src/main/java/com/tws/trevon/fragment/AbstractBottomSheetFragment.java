package com.tws.trevon.fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.tws.trevon.activity.AbstractActivity;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.common.NetworkHelper;


public abstract class AbstractBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener
{
    private String TAG = AbstractBottomSheetFragment.class.getSimpleName();

    protected abstract String getTAG();
    protected abstract void onViewClick(View view);
    protected abstract void onApiCallSuccess(Object responseValues, CallApi callApi);

    protected View rootView;



    public AbstractBottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onClick(View view)
    {
        try
        {
            AppUtilities.hideSoftKeyboard(getActivity());
            onViewClick(view);
        }
        catch(Exception ex)
        {
            showUserMessage("Unexpected error");
            AppUtilities.processException(
                    ex,
                    "onViewClick",
                    null,
                    TAG + " : " + AppUtilities.dumpBundle(getArguments()));
        }
    }

    protected void processCallApi(final CallApi callApi)
    {
        if(AppValidate.isEmpty(callApi.TAG))
        {
            callApi.TAG = getUniqueTag();
        }

        AppUtilities.processCallApi(new NetworkHelper() {
            @Override
            public AbstractActivity getActivity() {
                return (AbstractActivity)AbstractBottomSheetFragment.this.getActivity();
            }

            @Override
            public AbstractFragment getFragment() {
                return null;
            }

            @Override
            public CallApi getCallApi() {
                return callApi;
            }

            @Override
            public void onApiCallSuccess(String responseValues, CallApi callApi)
            {
                try
                {
                    AbstractBottomSheetFragment.this.onApiCallSuccess(responseValues, callApi);
                }
                catch(Exception ex)
                {
                    AppUtilities.processException(
                            ex,
                            "onApiCallSuccess",
                            null,
                            AppController.gson.toJson(responseValues));

                    throw ex;
                }
            }

            @Override
            public void onApiCallError(CallApi callApi, final String errorCode)
            {
                AbstractBottomSheetFragment.this.onApiCallError(callApi, errorCode);
            }
        });
    }

    public void onApiCallError(final CallApi callApi, final String errorCode)
    {

    }

    protected void showUserMessage(final String userMessage)
    {
        if(getActivity() != null)
        {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(rootView != null)
                    {
                        AppUtilities.showUserMessage(rootView, userMessage, Snackbar.LENGTH_LONG);
                    }
                    else
                    {
                        AppUtilities.showUserMessage(userMessage, Toast.LENGTH_SHORT);
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
       // CallApi.cancelCallWithTag(getUniqueTag());
    }

    public String getUniqueTag()
    {
        return getTAG() + hashCode();
    }
}
