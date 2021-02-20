package com.tws.trevon.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.tws.trevon.R;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.API;
import com.tws.trevon.constants.IReqParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class Registration extends AbstractActivity implements  GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemSelectedListener
{
    private static final String TAG = "SignIn";
    private static final int RC_SIGN_IN = 9001;
    private SignInButton google_login;
    private GoogleApiClient mGoogleApiClient;
    ImageView google_sign_in_custom;
    CallbackManager callbackManager;
    LoginButton loginFacebook;
    EditText pre_regis_mobile,register_email,register_store_company_name,register_gst_no;
    Button btn_continue_register;
    ImageView facebook_sign_in_custom;
    private LinearLayout click_for_signin;
    TextView signup_text;
    ArrayList<String> seller_type_list = new ArrayList<>();
    public String selected_seller_type;
    EditText pre_regis_name;
    String mobile;
    TextView signin_text;
    LinearLayout company_reg_layout,gst_reg_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_registration);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mobile = getIntent().getStringExtra("mobile");
        seller_type_list.add("RESELLER");
        seller_type_list.add("SALE PERSON");

        populateSpinner(seller_type_list);

        printHashKey(this);
        pre_regis_name = findViewById(R.id.pre_regis_name);
        google_sign_in_custom = findViewById(R.id.google_sign_in_custom);
        register_email = findViewById(R.id.register_email);
        pre_regis_mobile = findViewById(R.id.pre_regis_mobile);
        register_store_company_name = findViewById(R.id.register_store_company_name);
        register_gst_no = findViewById(R.id.register_gst_no);
        pre_regis_mobile.setText(mobile);
        company_reg_layout = findViewById(R.id.company_reg_layout);
        gst_reg_layout = findViewById(R.id.gst_reg_layout);
        register_gst_no.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }
            @Override
            public void afterTextChanged(Editable et) {
                String s=et.toString();
                if(!s.equals(s.toUpperCase()))
                {
                    s=s.toUpperCase();
                    register_gst_no.setText(s);
                    register_gst_no.setSelection(register_gst_no.length()); //fix reverse texting
                }
            }
        });
        btn_continue_register = findViewById(R.id.btn_continue_register);
        btn_continue_register.setOnClickListener(this);
        signin_text = findViewById(R.id.signin_text);
        signin_text.setOnClickListener(this);
        this.google_login   =      (SignInButton)  findViewById(R.id.google_login);
        google_login = (SignInButton) findViewById(R.id.google_login);
        //   google_login.setSize(SignInButton.SIZE_STANDARD);
        TextView textView = (TextView) google_login.getChildAt(0);
        textView.setText(getResources().getString(R.string.login_gg_text));

        callbackManager = CallbackManager.Factory.create();
        loginFacebook = (LoginButton)findViewById(R.id.login_button);
        facebook_sign_in_custom = findViewById(R.id.facebook_sign_in_custom);

        if(AccessToken.getCurrentAccessToken() != null)
        {
            RequestData();
        }

        facebook_sign_in_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFacebook.performClick();

            }
        });

        loginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                if(AccessToken.getCurrentAccessToken() != null){
                    RequestData();
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {
            }
        });


        try {
            GoogleSignIn();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
        }

        this.google_sign_in_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    connected = true;
                    signIn();
                }
                else {
                    connected = false;
                    Toast.makeText(Registration.this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onViewClick(View view)
    {
        switch(view.getId())
        {
            case R.id.btn_continue_register:

                if(checkValidations())
                {
                    CallApi callApi = new CallApi(API.user_registration);
                    callApi.addReqParams("email", register_email.getText().toString().trim());
                    callApi.addReqParams("username", pre_regis_name.getText().toString().trim());
                    callApi.addReqParams("mobile", pre_regis_mobile.getText().toString().trim());
                    callApi.addReqParams("seller_type", selected_seller_type);
                    callApi.addReqParams("company_name", register_store_company_name.getText().toString().trim());
                    callApi.addReqParams("gst_no", register_gst_no.getText().toString().trim());
                    callApi.addReqParams("affiliate_id",  AppUtilities.readFromPref(IReqParams.REFERAL_CODE));
                    processCallApi(callApi);

                }

                break;

            case R.id.signin_text:
               /* Intent i = new Intent(Registration.this, LoginOriginal.class);
                startActivity(i);*/
                break;




        }

    }

    private Boolean checkValidations()
    {
        Boolean isValid = true;

        if(register_email.getText().toString().trim().length() == 0)
        {
            isValid = false;
            register_email.requestFocus();
            Toast.makeText(Registration.this, "Enter Email", Toast.LENGTH_SHORT).show();
            //loginId.setBackgroundResource(R.drawable.edit_text_border_red);
        }
        else if(!AppValidate.isEmail(register_email.getText().toString().trim()))
        {
            isValid = false;
            register_email.requestFocus();
            Toast.makeText(Registration.this, "Enter Correct Email", Toast.LENGTH_SHORT).show();
        }
        else if (pre_regis_name.getText().toString().trim().length() == 0)
        {
            isValid = false;
            pre_regis_name.requestFocus();
            Toast.makeText(Registration.this, "Enter Name", Toast.LENGTH_SHORT).show();
        }
        else if (pre_regis_mobile.getText().toString().trim().length() == 0)
        {
            isValid = false;
            pre_regis_mobile.requestFocus();
            Toast.makeText(Registration.this, "Enter Mobile no.", Toast.LENGTH_SHORT).show();
        }
        else if(!AppValidate.isMobileNumber(pre_regis_mobile.getText().toString().trim()))
        {
            isValid = false;
            pre_regis_mobile.requestFocus();
            Toast.makeText(Registration.this, "Enter Correct Mobile no.", Toast.LENGTH_SHORT).show();
        }


        else if(AppValidate.isEmpty(selected_seller_type))
        {
            isValid = false;
            Toast.makeText(Registration.this, "Select Seller Type", Toast.LENGTH_SHORT).show();
            //loginId.setBackgroundResource(R.drawable.edit_text_border_red);
        }

        else if(selected_seller_type.equals("RESELLER"))
        {
            if(register_store_company_name.getText().toString().trim().length() == 0)
            {
                isValid = false;
                register_store_company_name.requestFocus();
                Toast.makeText(Registration.this, "Enter Store/Company name", Toast.LENGTH_SHORT).show();
            }

            else if(register_gst_no.getText().toString().trim().length() == 0)
            {
                isValid = false;
                register_gst_no.requestFocus();
                Toast.makeText(Registration.this, "Enter GST No.", Toast.LENGTH_SHORT).show();
            }
            else if(register_gst_no.getText().toString().trim().length() != 15)
            {
                isValid = false;
                register_gst_no.requestFocus();
                Toast.makeText(Registration.this, "Enter Correct GST No.", Toast.LENGTH_SHORT).show();
            }
        }


        return isValid;
    }


    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {
        if(API.user_registration.method.equals(callApi.networkActivity.method))
        {
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");

                if (status.equalsIgnoreCase("Success"))
                {
                   // AppUtilities.writeToPref(IReqParams.REFERAL_CODE, "");
                    // JSONArray onbjnew = action.getJSONArray("data");
                    JSONObject jsonObject1= action.getJSONObject("data");
                   // UserCO userCO = AppController.gson.fromJson(jsonObject1.toString(), UserCO.class);
                   // userCO.saveInDB();

                    Intent iDash =new Intent(Registration.this, PreRegistrationPage.class);
                    iDash.putExtra("userCO",jsonObject1.toString());
                    startActivity(iDash);
                    finish();

                }
                else
                {
                    Toast.makeText(this, nessage, Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            /* UserCO userCO = AppController.gson.fromJson(userCOJsonString, UserCO.class);
            userCO.saveInDB();

            Toast.makeText(this, UserCO.getUserCOFromDB()+"", Toast.LENGTH_SHORT).show();
            UserCO.getUserCOFromDB();*/
        }
        if(API.socialLogin.method.equals(callApi.networkActivity.method))
        {
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");

                if (status.equalsIgnoreCase("Success"))
                {
                    // JSONArray onbjnew = action.getJSONArray("data");

                    JSONObject jsonObject1= action.getJSONObject("data");
                    UserCO userCO = AppController.gson.fromJson(jsonObject1.toString(), UserCO.class);
                    userCO.saveInDB();

                    if(userCO.seller_type.equals("RESELLER"))
                    {
                        Intent intent_in = new Intent(Registration.this, MainActivity.class);
                        intent_in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent_in);
                        finish();
                    }
                    else
                    {
                        Intent intent_in = new Intent(Registration.this, com.tws.trevon.activity.sp.MainActivity.class);
                        intent_in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent_in);
                        finish();
                    }

                  /*  Intent iDash =new Intent(Registration.this, MainActivity.class);
                    iDash.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(iDash);*/


                }
                else
                {
                    Toast.makeText(this, nessage, Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            /* UserCO userCO = AppController.gson.fromJson(userCOJsonString, UserCO.class);
            userCO.saveInDB();

            Toast.makeText(this, UserCO.getUserCOFromDB()+"", Toast.LENGTH_SHORT).show();
            UserCO.getUserCOFromDB();*/
        }

    }


    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }


    public void RequestData(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {


             /*   Profile profile = Profile.getCurrentProfile();
                System.out.println(profile.getFirstName());
                System.out.println(profile.getId());
*/

                JSONObject json = response.getJSONObject();
                System.out.println(json);
                try {
                    if(json != null)
                    {
                        //String text = "<b>Name :</b> "+json.getString("first_name")+"<br><br><b>Last_name : </b>"+json.getString("last_name")+"<br><br><b>Email :</b> "+json.getString("id")+"<br><br><b>Profile link :</b> "+json.getString("link")+"<br><br><b>email : </b>"+json.getString("email");
                        // details_txt.setText(Html.fromHtml(text));

                        /*  profile1.setProfileId(json.getJSONObject("picture").getJSONObject("data").getString("url"));*/
                        // profile1.setProfileId(json.getString("id"));
                        String email = "";
                        if (json.has("email")) {
                            email = json.getString("email");
                        }


                        LoginManager.getInstance().logOut();
                        CallApi callApi = new CallApi(API.socialLogin);
                        callApi.addReqParams("type", "fb");
                        callApi.addReqParams("fb_id", json.getString("id"));
                        callApi.addReqParams("g_id", "");
                        callApi.addReqParams("name", json.getString("first_name")+json.getString("last_name"));
                        try{
                            callApi.addReqParams("email", json.getString("email"));
                        }
                        catch(Exception e)
                        {

                        }
                        try{
                            callApi.addReqParams("profile", json.getJSONObject("picture").getJSONObject("data").getString("url"));
                        }
                        catch(Exception e)
                        {

                        }

                        processCallApi(callApi);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();

        parameters.putString( "fields", " id , email, first_name, last_name ,gender, birthday, link  ,picture");
        /*   parameters.putString("fields", "id, email ,name ,link  ,picture");*/
        request.setParameters(parameters);
        request.executeAsync();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            getResultGoogle(result);
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void GoogleSignIn(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("Rajan_google_login_onConnectionFailed:" + connectionResult);

    }

    private void getResultGoogle(GoogleSignInResult result) {
//        System.out.println("Rajan_google_login_handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            String photo = "https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg" ;
            if (acct.getPhotoUrl()!=null){
                photo =  acct.getPhotoUrl().toString();
            }

//            System.out.println("Rajan_google_login_detail"+acct.getId().toString()+acct.getId()+ acct.getDisplayName().toString()+"google"+photo);
            String gid;
            if(acct.getId() != null) {
                gid = acct.getId();
            } else {
                gid = "1234567890";
            }


            if(acct.getDisplayName()!=null)
            {
                signUp("google",gid, acct.getEmail(), acct.getDisplayName().replaceAll("[^A-Za-z0-9]", ""), "google", photo);
            } else {
                signUp("google",gid,acct.getEmail(), acct.getEmail().replaceAll("[^A-Za-z0-9]",""), "google", photo);
            }
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        } else {
//            System.out.println("Rajan_google_login_handleSignInResult:" + result.getStatus());

        }
    }

    private void signUp(String type, String g_id, String strEmail, String strName, String google, String photo) {
        try {
            CallApi callApi = new CallApi(API.socialLogin);
            callApi.addReqParams("fb_id", "");
            callApi.addReqParams("type", type);
            callApi.addReqParams("g_id", g_id);
            callApi.addReqParams("name", strName);
            callApi.addReqParams("email", strEmail);
            callApi.addReqParams("profile", photo);
            processCallApi(callApi);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
        }
    }

    private void populateSpinner(ArrayList<String> tenureCOList)
    {
        // Get reference of widgets from XML layout
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);

        final List<String> plantsList = new ArrayList<>();
        plantsList.add("Seller Type");
        for(String tenureCO : tenureCOList)
        {
            plantsList.add(tenureCO);
        }

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,plantsList){
            @Override
            public boolean isEnabled(int position)
            {
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent)
            {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else
                {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
    {

        // If user change the default selection
        // First item is disable and it is used for hint
        if(position > 0)
        {
            selected_seller_type = (String) parent.getItemAtPosition(position);
            if(selected_seller_type.equals("SALE PERSON"))
            {
                selected_seller_type = "SALE_PERSON";
                company_reg_layout.setVisibility(View.GONE);
                gst_reg_layout.setVisibility(View.GONE);
            }
            else
            {
                company_reg_layout.setVisibility(View.VISIBLE);
                gst_reg_layout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        // TODO Auto-generated method stub
    }



}
