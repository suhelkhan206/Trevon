package com.tws.trevon.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tws.trevon.R;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.API;

import org.json.JSONObject;

public class ForgotPassword extends AbstractActivity {

    private EditText mobile_number_forgot;
    private TextView forgot_email_error;
    private  TextView login_text_forgot;

    @Override
    protected void onViewClick(View view) {

        switch(view.getId())
        {
            case R.id.btn_send_otp:
                if(checkValidations())
                {
                    CallApi callApi = new CallApi(API.forgotPassword);
                    callApi.addReqParams("mobile", mobile_number_forgot.getText().toString());
                    processCallApi(callApi);
                }
                break;

            case R.id.click_for_login_forgot:

                Intent i=new Intent(ForgotPassword.this, LoginOriginal.class);
                startActivity(i);
                break;
        }
    }


    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi)
    {
        if(API.forgotPassword.method.equals(callApi.networkActivity.method))
        {
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");

                if (status.equalsIgnoreCase("Success"))
                {
                    JSONObject onbjnew = action.getJSONObject("data");

                    // JSONObject jsonObject1= onbjnew.getJSONObject(0);
                    UserCO userCO = AppController.gson.fromJson(onbjnew.toString(), UserCO.class);

                    String userCoString = AppController.gson.toJson(userCO);
                    Intent i =new Intent(ForgotPassword.this, ForgotOTP.class);
                    i.putExtra("userCO",userCoString);
                    i.putExtra("isForgot","YES");
                    startActivity(i);

                    /*Intent iDash =new Intent(Registration.this, DashBoard.class);
                    iDash.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(iDash);
                    finish();*/

                }
                else
                {
                    Toast.makeText(this, nessage, Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            /* UserCO userCO = AppController.gson.fromJson(userCOJsonString, UserCO.class);
            userCO.saveInDB();

            Toast.makeText(this, UserCO.getUserCOFromDB()+"", Toast.LENGTH_SHORT).show();
            UserCO.getUserCOFromDB();*/
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initailizeApp();
    }


    public void initailizeApp()
    {
        mobile_number_forgot = findViewById(R.id.mobile_number_forgot);
        forgot_email_error = findViewById(R.id.forgot_email_error);
        Button btn_send_otp = findViewById(R.id.btn_send_otp);
        LinearLayout click_for_login_forgot = findViewById(R.id.click_for_login_forgot);
        login_text_forgot = findViewById(R.id.login_text_forgot);
        String text = "Don't have an account? <font color='#FFFFFF';><strong>Register</strong></font>.";
        login_text_forgot.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        btn_send_otp.setOnClickListener(this);
        click_for_login_forgot.setOnClickListener(this);
    }

    private boolean checkValidations()
    {
        Boolean isValid = true;
        mobile_number_forgot.setText(AppUtilities.removeSpace(mobile_number_forgot.getText().toString().trim()));
        if(mobile_number_forgot.getText().toString().equalsIgnoreCase(""))
        {
            isValid = false;
            mobile_number_forgot.requestFocus();
            forgot_email_error.setVisibility(View.VISIBLE);
            forgot_email_error.setText(getString(R.string.tv_login_email_error));
            mobile_number_forgot.setBackgroundResource(R.drawable.edit_text_border_red);
        }
        else if(AppValidate.isNumber(mobile_number_forgot.getText().toString()))
        {
            if (!AppValidate.isMobileNumber(mobile_number_forgot.getText().toString()))
            {
                isValid = false;
                mobile_number_forgot.requestFocus();
                forgot_email_error.setVisibility(View.VISIBLE);
                forgot_email_error.setText(getString(R.string.tv_login_mobile_error));
                mobile_number_forgot.setBackgroundResource(R.drawable.edit_text_border_red);
            }
        }

        return isValid;
    }
}
