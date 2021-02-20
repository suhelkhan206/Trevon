package com.tws.trevon.activity;

import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.tws.trevon.R;
import com.tws.trevon.co.MyAddressCO;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.API;

import java.util.Map;

public class ChangePassword extends AbstractActivity
{
    public EditText etCurrentPass, etNewPass, etConfirmPass;
    public  Button button_change_pass;
    LinearLayout clickForgotPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar toolbar = findViewById(R.id.change_password_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Change Password");

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        initializeApp();
        autoFillData();
    }

    private void autoFillData()
    {
        UserCO userCO = UserCO.getUserCOFromDB();
        //etCurrentPass.setText("123456");
    }

    private void initializeApp()
    {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        etCurrentPass= findViewById(R.id.et_current_password);
        etNewPass= findViewById(R.id.et_new_password);
        etConfirmPass= findViewById(R.id.et_confirm_password);
        button_change_pass= findViewById(R.id.button_change_pass);
        rootView = findViewById(R.id.root_view);
        clickForgotPassword= findViewById(R.id.click_forgot_password);


        button_change_pass.setOnClickListener(this);
        clickForgotPassword.setOnClickListener(this);


     /*   Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView textToolbarHeading = (TextView) findViewById(R.id.tv_toolbar_heading);
        textToolbarHeading.setText("Change Password");
        findViewById(R.id.button_change_pass).setOnClickListener(this);*/
    }

    @Override
    protected void onViewClick(View view)
    {
        switch(view.getId())
        {
            case R.id.button_change_pass:

                if(checkValidations())
                {
                    CallApi callApi = new CallApi(API.resatePassword);
                    callApi.addReqParams("user_id",UserCO.getUserCOFromDB().id);
                    callApi.addReqParams("password_1", etNewPass.getText().toString());
                    callApi.addReqParams("password_2", etCurrentPass.getText().toString());

                    processCallApi(callApi);
                }


                break;
            case R.id.click_forgot_password:
                /*Intent i_forgot=new Intent(ChangePassword.this,ForgotPassword.class);
                startActivity(i_forgot);*/
                break;

        }
    }



    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi)
    {
        if(API.resatePassword.method.equals(callApi.networkActivity.method))
        {
            String message = AppController.gson.toJson(((Map)responseValues).get("message"));
            AppUtilities.showUserMessage(message, Snackbar.LENGTH_LONG);
            finish();
        }
    }

    private Boolean checkValidations()
    {
        Boolean isValid = true;

        if(etCurrentPass.getText().toString().equalsIgnoreCase(""))
        {
            isValid = false;
            AppUtilities.showUserMessage(rootView,"Enter Current Password", Snackbar.LENGTH_LONG);
        }

        else if(etNewPass.getText().toString().equalsIgnoreCase(""))
        {
            isValid = false;
            AppUtilities.showUserMessage(rootView,"Enter New Password", Snackbar.LENGTH_LONG);
        }

        else if(etConfirmPass.getText().toString().equalsIgnoreCase(""))
        {
            isValid = false;
            AppUtilities.showUserMessage(rootView,"Enter Confirm Password", Snackbar.LENGTH_LONG);
        }

        else if(!(etNewPass.getText().toString().equalsIgnoreCase(etConfirmPass.getText().toString())))
        {
            isValid = false;
            AppUtilities.showUserMessage(rootView,"New Password and Confirm Password does not match", Snackbar.LENGTH_LONG);
        }

        return isValid;
    }


}
