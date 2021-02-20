package com.tws.trevon.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.tws.trevon.R;
import com.tws.trevon.co.User;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.common.NetworkHelper;
import com.tws.trevon.constants.IConstants;
import com.tws.trevon.constants.IPreferences;
import com.tws.trevon.fragment.AbstractFragment;
import com.tws.trevon.fragment.CustomAlertDialog;


public abstract class AbstractActivity extends AppCompatActivity implements View.OnClickListener
{
    private String TAG = AbstractActivity.class.getSimpleName();
    protected abstract void onViewClick(View view);
    protected abstract void onApiCallSuccess(Object responseValues, CallApi callApi);
    protected View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getApplication();//just to initialise the application variable
        AppUtilities.checkForBackgroundProcess(this);

 /*        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        if(android.os.Build.VERSION.SDK_INT > 20)
        {
            getWindow().setAllowEnterTransitionOverlap(true);
        }

        if(AppController.forceLogout)
        {

        }

        AppUtilities.checkForBackgroundProcess();
        Utilities.logMessage(TAG, "onCreate called", ISysCodes.LOG_MODE_INFO);*/
    }

    protected void onCreateExtra(final int layoutResID)
    {
        try {
            setContentView(layoutResID);
            rootView = findViewById(R.id.root_view);

            if(AppValidate.isNotEmpty(getIntent().getStringExtra(IConstants.USER_MESSAGE)))
            {
                showUserMessage(getIntent().getStringExtra(IConstants.USER_MESSAGE));
            }
        }
        catch(Exception ex)
        {
            AppUtilities.processException(
                    ex,
                    "onCreateExtra",
                    null,
                    TAG + " : " + AppUtilities.dumpIntent(getIntent()));

            throw ex;
        }
    }

    @Override
    public void onClick(View view)
    {
        try
        {
            AppUtilities.hideSoftKeyboard(this);
            onViewClick(view);
        }
        catch(Exception ex)
        {
            showUserMessage("Unexpected error");
            AppUtilities.processException(
                    ex,
                    "onViewClick",
                    null,
                    TAG + " : " + AppUtilities.dumpBundle(getIntent().getExtras()));
        }
    }


    protected void processCallApi(final CallApi callApi)
    {
        AppUtilities.processCallApi(new NetworkHelper() {
            @Override
            public AbstractActivity getActivity()
            {
                return AbstractActivity.this;
            }

            @Override
            public CallApi getCallApi()
            {
                return callApi;
            }

            @Override
            public AbstractFragment getFragment() {
                return null;
            }

            @Override
            public void onApiCallSuccess(String responseValues, CallApi callApi)
            {
                AbstractActivity.this.onApiCallSuccess(responseValues, callApi);
            }

            @Override
            public void onApiCallError(CallApi callApi, String errorCode)
            {
                AbstractActivity.this.onApiCallError(callApi, errorCode);
            }
        });
    }

    public void onApiCallError(final CallApi callApi, final String errorCode)
    {

    }

/*    public void showUserMessage(String userMessage, boolean processInBackground)
    {
        if(processInBackground)
        {
            //do nothing
        }
        else
        {
            showUserMessage(userMessage);
        }
    }*/

    public void showUserMessage(String userMessage)
    {
        if(rootView == null)
        {
            AppUtilities.showUserMessage(userMessage, Toast.LENGTH_SHORT);
        }
        else
        {
            AppUtilities.showUserMessage(rootView, userMessage, Snackbar.LENGTH_LONG);
        }
    }

    protected void showCustomAlertDialog(final String title, final String description, final boolean isCancelable, final CustomAlertDialog.OnDialogFragmentListener listener)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        CustomAlertDialog customAlertDialog = new CustomAlertDialog();
        Bundle bundle = new Bundle();
        bundle.putString(IConstants.DIALOG_TITLE, title);
        bundle.putString(IConstants.DESCRIPTION, description);
        bundle.putBoolean(IConstants.BOOLEAN, isCancelable);
        customAlertDialog.setArguments(bundle);
        customAlertDialog.setOnDialogClickListener(listener);
        customAlertDialog.show(fragmentManager,"");
    }

    public void processLogout(Intent destinationIntent)
    {
        String fcmRegToken = AppUtilities.readFromPref(IPreferences.FCM_REGISTRATION_TOKEN);
        AppUtilities.clearDatabase();
        User.getUserCOFromDB().deleteFromDB();
        AppUtilities.writeBooleanToPref(IConstants.IS_USER_LOGGED_IN, false);
        AppUtilities.writeBooleanToPref(IPreferences.FCM_REGISTRATION_TOKEN_SAVED_ON_SERVER, false);
        AppUtilities.writeToPref(IPreferences.FCM_REGISTRATION_TOKEN, fcmRegToken);

        if(destinationIntent == null)
        {
            destinationIntent = AppUtilities.getNewFreshActivityIntent(this, LoginOriginal.class);
        }

        AppUtilities.addUserMessageOnActivity(destinationIntent, "Please login to proceed...");
        startActivity(destinationIntent);
        finish();
    }

    public boolean isInternetConnectionAvailable()
    {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())
        {
            return true;
        }
        else
        {
            showUserMessage("Internet connection not available");
            return false;
        }
    }
}
