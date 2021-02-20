package com.tws.trevon.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.core.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tws.trevon.R;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.API;
import com.tws.trevon.constants.IReqParams;
import com.tws.trevon.constants.ISysConfig;

import org.json.JSONObject;


public class  ForgotOTP extends AbstractActivity implements View.OnFocusChangeListener, View.OnKeyListener, TextWatcher
{
    private EditText set_pass,conf_pass;
    private TextView OTP_mob_text,resend,resend_count;
    private Button set_password_button;
    private EditText otpPinFirstDigitEditText;
    private EditText otpPinSecondDigitEditText;
    private EditText otpPinThirdDigitEditText;
    private EditText otpPinForthDigitEditText;
    private EditText otpPinHiddenEditText;
    private String otpMatcher,otp,mobileNumber,dbAccessToken;
    private LinearLayout forgot_otp_error;
    private ScrollView rootView;
    CountDownTimer mCountDownTimer;
    private String s="";
    RelativeLayout resend_layout;
    boolean isResendActive = false;
    private LinearLayout password_layout_forgot;
    final int REQUEST_CODE_READ_SMS = 100;
    String isForgot;
    String mobile;
    UserCO userCO;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_o_t_p);

        String userCOString = getIntent().getStringExtra("userCO");
        isForgot = getIntent().getStringExtra("isForgot");
       // mobile = getIntent().getStringExtra("mobile");

        userCO = AppController.gson.fromJson(userCOString,UserCO.class);
        otp = userCO.otp;

        mobileNumber =userCO.mobile;
        if(!AppValidate.isEmail(mobileNumber))
        {
            ActivityCompat.requestPermissions(ForgotOTP.this, new String[]{android.Manifest.permission.RECEIVE_SMS}, REQUEST_CODE_READ_SMS);
        }


        initializeApp();
    }

    @Override
    protected void onViewClick(View view)
    {
        switch(view.getId())
        {
            case R.id.set_password_button:

                if(checkValidations())
                {
                    CallApi callApi = new CallApi(API.resatePassword);
                    callApi.addReqParams("password_1", set_pass.getText().toString());
                    callApi.addReqParams("password_2", set_pass.getText().toString());
                    callApi.addReqParams("user_id", userCO.id);
                    processCallApi(callApi);
                }
                break;

            case R.id.resend:

                resend.setEnabled(true);
                resend.setClickable(true);
                resend.setTextColor(Color.parseColor("#828282"));
                //Toast.makeText(ForgotOTP.this, "Hello", Toast.LENGTH_SHORT).show();
                resend_count.setTextColor(Color.parseColor("#828282"));
                mCountDownTimer.cancel();
                isResendActive=false;
                mCountDownTimer.start();

                CallApi callApi = new CallApi(API.resend_otp);
                callApi.addReqParams("mobile", mobileNumber);
                processCallApi(callApi);

                break;
        }
    }


    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi)
    {
        if(API.resend_otp.method.equals(callApi.networkActivity.method))
        {
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");
                if (status.equalsIgnoreCase("Success"))
                {
                    JSONObject jsonObject1= action.getJSONObject("data");

                    otp = jsonObject1.getString("otp");
                   // finish();
                }
                else {

                    Toast.makeText(this, nessage, Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e) {
                Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            /* UserCO userCO = AppController.gson.fromJson(userCOJsonString, UserCO.class);
            userCO.saveInDB();

            Toast.makeText(this, UserCO.getUserCOFromDB()+"", Toast.LENGTH_SHORT).show();
            UserCO.getUserCOFromDB();*/
        }
    }

    private void initializeApp()
    {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        rootView = findViewById(R.id.root_view);
        forgot_otp_error = findViewById(R.id.forgot_otp_error);
        TextView forgot_otp_error_text= findViewById(R.id.forgot_otp_error_text);
        password_layout_forgot = findViewById(R.id.password_layout_forgot);
        OTP_mob_text= findViewById(R.id.OTP_mob_text);
        resend= findViewById(R.id.resend);
        resend_count= findViewById(R.id.resend_count);
        set_pass= findViewById(R.id.set_pass);
        conf_pass= findViewById(R.id.conf_pass);
        resend_layout=findViewById(R.id.resend_layout);
        set_password_button= findViewById(R.id.set_password_button);
        set_password_button.setOnClickListener(this);
        otpPinFirstDigitEditText = findViewById(R.id.pin_first_edittext_forgot_otp);
        otpPinSecondDigitEditText = findViewById(R.id.pin_second_edittext_forgot_otp);
        otpPinThirdDigitEditText = findViewById(R.id.pin_third_edittext_forgot_otp);
        otpPinForthDigitEditText = findViewById(R.id.pin_forth_edittext_forgot_otp);
        otpPinHiddenEditText = findViewById(R.id.pin_hidden_edittext_forgot_otp);
        otpPinHiddenEditText.addTextChangedListener(this);
        otpPinFirstDigitEditText.setOnFocusChangeListener(this);
        otpPinSecondDigitEditText.setOnFocusChangeListener(this);
        otpPinThirdDigitEditText.setOnFocusChangeListener(this);
        otpPinForthDigitEditText.setOnFocusChangeListener(this);
        otpPinFirstDigitEditText.setOnKeyListener(this);
        otpPinSecondDigitEditText.setOnKeyListener(this);
        otpPinThirdDigitEditText.setOnKeyListener(this);
        otpPinForthDigitEditText.setOnKeyListener(this);
        otpPinHiddenEditText.setOnKeyListener(this);
        resend.setClickable(false);
        resend.setOnClickListener(this);

        String mobileHint= mobileNumber.substring(mobileNumber.length() - 3);

        if(AppValidate.isEmail(mobileNumber))
        {
            OTP_mob_text.setText( getString(R.string.OTP_text_email)+ " "+mobileHint+ " "+getString(R.string.OTP_mob_text2));
        }
        else
        {
            OTP_mob_text.setText(getString(R.string.OTP_mob_text1)+ " "+mobileHint+" "+getString(R.string.OTP_mob_text2));
        }

        mCountDownTimer=new CountDownTimer(ISysConfig.COUNT_TIMER, 1000)
        {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            public void onTick(long millisUntilFinished)
            {
                resend_count.setVisibility(View.VISIBLE);
                resend_count.setTextColor(Color.parseColor("#727272"));
                long seconds = (int) (millisUntilFinished / 1000) % 60 ;
                long minutes = (int) ((millisUntilFinished / (1000*60)) % 60);
                resend_count.setText(String.format("%02d", minutes)+" : "+String.format("%02d", seconds));

                if(seconds >= 45)
                {
                    resend.setEnabled(true);
                    resend.setClickable(true);
                    resend.setTextColor(Color.parseColor("#0BA8DF"));
                    isResendActive = true;
                }
                if(!isResendActive)
                {
                    resend.setEnabled(false);
                    resend.setClickable(false);
                    resend.setTextColor(Color.parseColor("#828282"));
                }
                //here you can have your logic to set text to edittext
            }
            public void onFinish()
            {
                resend_count.setTextColor(Color.parseColor("#FF0000"));
                resend_count.setVisibility(View.GONE);
                // resend_count.setText(getString(R.string.resend_expired));
                // otp="";
                resend.setEnabled(true);
                resend.setClickable(true);
                isResendActive = true;
                resend.setTextColor(Color.parseColor("#0BA8DF"));
            }
        }.start();

    }



    private boolean checkValidations()
    {
        Boolean isValid = true;

        if(set_pass.getText().toString().equalsIgnoreCase(""))
        {
            isValid = false;
            //AppUtilities.showUserMessage(rootView,getString(R.string.otp_pass_toast), Snackbar.LENGTH_LONG);
        }
        else if(conf_pass.getText().toString().equalsIgnoreCase(""))
        {
            isValid = false;
            //  AppUtilities.showUserMessage(rootView,getString(R.string.otp_con_toast), Snackbar.LENGTH_LONG);
        }
        else if(!(set_pass.getText().toString().equalsIgnoreCase(conf_pass.getText().toString())))
        {
            isValid = false;
            //  AppUtilities.showUserMessage(rootView,getString(R.string.otp_match_toast), Snackbar.LENGTH_LONG);
        }
        return isValid;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
        // otpPinHiddenEditText.makeText(this, s, Toast.LENGTH_SHORT).show();
        otpPinHiddenEditText.setSelection(otpPinHiddenEditText.getText().length());
    }

    @Override
    public void onTextChanged(CharSequence seq, int start, int before, int count)
    {
        otpPinHiddenEditText.setSelection(otpPinHiddenEditText.getText().length());
    }

    @Override
    public void afterTextChanged(Editable value)
    {
        s=String.valueOf(value);

        if (s.length() == 0)
        {

            otpPinFirstDigitEditText.setText("");
        } else if (s.length() == 1) {

            otpPinFirstDigitEditText.setText(s.charAt(0) + "");
            otpPinSecondDigitEditText.setText("");
            otpPinThirdDigitEditText.setText("");
            otpPinForthDigitEditText.setText("");

        } else if (s.length() == 2) {

            otpPinSecondDigitEditText.setText(s.charAt(1) + "");
            otpPinThirdDigitEditText.setText("");
            otpPinForthDigitEditText.setText("");

        } else if (s.length() == 3) {
            otpPinThirdDigitEditText.setText(s.charAt(2) + "");
            otpPinForthDigitEditText.setText("");

        } else if (s.length() == 4)
        {
            otpPinForthDigitEditText.setText(s.charAt(3) + "");
            hideSoftKeyboard(otpPinForthDigitEditText);

            String s1 = otpPinFirstDigitEditText.getText().toString();
            String s2 = otpPinSecondDigitEditText.getText().toString();
            String s3 = otpPinThirdDigitEditText.getText().toString();
            String s4 = otpPinForthDigitEditText.getText().toString();

            String userOtp = s1+s2+s3+s4;
            if(userOtp.equals(otp))
            {
                if(userCO.is_registered.equals("0"))
                {
                    Intent i = new Intent(ForgotOTP.this, Registration.class);
                    i.putExtra("mobile", mobileNumber);
                    startActivity(i);
                }
                else
                {
                    password_layout_forgot.setVisibility(View.GONE);
                    userCO.saveInDB();
                    if (userCO.seller_type.equals("RESELLER"))
                    {
                        Intent intent_in = new Intent(ForgotOTP.this, MainActivity.class);
                        intent_in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent_in);
                        finish();
                    }
                    else
                        {
                        Intent intent_in = new Intent(ForgotOTP.this, com.tws.trevon.activity.sp.MainActivity.class);
                        intent_in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent_in);
                        finish();
                    }

                }

                // verifiedOtp();
            }
            else
            {
                forgot_otp_error.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        final int id = v.getId();
        switch (id) {
            case R.id.pin_first_edittext_forgot_otp:
                if (hasFocus)
                {
                    setFocus(otpPinHiddenEditText);
                    showSoftKeyboard(otpPinHiddenEditText);
                }
                break;

            case R.id.pin_second_edittext_forgot_otp:
                if (hasFocus) {
                    setFocus(otpPinHiddenEditText);
                    showSoftKeyboard(otpPinHiddenEditText);
                }
                break;

            case R.id.pin_third_edittext_forgot_otp:
                if (hasFocus) {
                    setFocus(otpPinHiddenEditText);
                    showSoftKeyboard(otpPinHiddenEditText);
                }
                break;

            case R.id.pin_forth_edittext_forgot_otp:
                if (hasFocus) {
                    setFocus(otpPinHiddenEditText);
                    showSoftKeyboard(otpPinHiddenEditText);
                }
                break;

            default:
                break;
        }
    }

    public static void setFocus(EditText editText)
    {
        if (editText == null)
            return;

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();

    }

    public void hideSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }

    public void showSoftKeyboard(EditText editText)
    {
        if (editText == null)
            return;

        InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm1.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_DOWN)
        {
            final int id = v.getId();
            switch (id)
            {
                case R.id.pin_hidden_edittext_forgot_otp:
                    if (keyCode == KeyEvent.KEYCODE_DEL)
                    {
                        if (otpPinHiddenEditText.getText().length() == 4)
                        {
                            otpPinForthDigitEditText.setText("");
                            s=s.substring(0, s.length() - 1);
                            otpPinHiddenEditText.setText(s);


                        }
                        else if (otpPinHiddenEditText.getText().length() == 3)
                        {
                            otpPinThirdDigitEditText.setText("");
                            s=s.substring(0, s.length() - 1);
                            otpPinHiddenEditText.setText(s);

                        }
                        else if (otpPinHiddenEditText.getText().length() == 2)
                        {
                            otpPinSecondDigitEditText.setText("");
                            s=s.substring(0, s.length() - 1);
                            otpPinHiddenEditText.setText(s);

                        }
                        else if (otpPinHiddenEditText.getText().length() == 1)
                        {
                            otpPinFirstDigitEditText.setText("");
                            s=s.substring(0, s.length() - 1);
                            otpPinHiddenEditText.setText(s);

                        }

                        if (otpPinHiddenEditText.length() > 0)
                        {
                            otpPinHiddenEditText.setText(s);
                            // otpPinHiddenEditText.setText(otpPinHiddenEditText.getText().subSequence(0, otpPinHiddenEditText.length() - 1));
                        }
                        return true;
                    }
                    break;

                default:
                    return false;
            }
        }

        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // SMSReceiver.unbindListener();
    }

}

