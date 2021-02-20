package com.tws.trevon.fragment;

import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.DialogFragment;
import android.view.View;
import android.widget.Toast;

import com.tws.trevon.activity.AbstractActivity;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.common.NetworkHelper;

public abstract class AbstractDialogFragment extends DialogFragment implements View.OnClickListener
{
    private String TAG = AbstractFragment.class.getSimpleName();

    protected abstract void onViewClick(View view);
    protected abstract void onApiCallSuccess(Object responseValues, CallApi callApi);

    protected View rootView;

    public AbstractDialogFragment()
    {
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
        AppUtilities.processCallApi(new NetworkHelper() {
            @Override
            public AbstractActivity getActivity() {
                return (AbstractActivity)AbstractDialogFragment.this.getActivity();
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
                AbstractDialogFragment.this.onApiCallSuccess(responseValues, callApi);
            }

            @Override
            public void onApiCallError(CallApi callApi, final String errorCode)
            {
                AbstractDialogFragment.this.onApiCallError(callApi, errorCode);
            }
        });
    }

    public void onApiCallError(final CallApi callApi, final String errorCode)
    {

    }

    protected void showUserMessage(String userMessage)
    {
        if(rootView == null)
        {
            AppUtilities.showUserMessage(rootView, userMessage, Snackbar.LENGTH_LONG);
        }
        else
        {
            AppUtilities.showUserMessage(userMessage, Toast.LENGTH_SHORT);
        }
    }
}
