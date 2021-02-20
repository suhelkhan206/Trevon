package com.tws.trevon.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.API;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginOriginal extends AbstractActivity implements  GoogleApiClient.OnConnectionFailedListener  {

    private static final String TAG = "SignIn";
    private static final int RC_SIGN_IN = 9001;
    private SignInButton google_login;
    private GoogleApiClient mGoogleApiClient;
    ImageView google_sign_in_custom;
    CallbackManager callbackManager;
    LoginButton loginFacebook;
    EditText login_email_mobile;
    Button btn_login;
    ImageView facebook_sign_in_custom;
    TextView forget_password;
    private LinearLayout signup_redirect;
    TextView signup_text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login_mef);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        printHashKey(this);

        google_sign_in_custom = findViewById(R.id.google_sign_in_custom);
        login_email_mobile = findViewById(R.id.login_email_mobile);
        btn_login = findViewById(R.id.btn_login);
        forget_password = findViewById(R.id.forget_password);
        forget_password.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        signup_redirect = findViewById(R.id.click_for_signup);
        signup_redirect.setOnClickListener(this);
        signup_text = findViewById(R.id.signup_text);

        String text = "Or Sign up";
        signup_text.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

        this.google_login   =      (SignInButton)  findViewById(R.id.google_login);
        google_login = (SignInButton) findViewById(R.id.google_login);
        //   google_login.setSize(SignInButton.SIZE_STANDARD);
        TextView textView = (TextView) google_login.getChildAt(0);
        textView.setText(getResources().getString(R.string.login_gg_text));

        callbackManager = CallbackManager.Factory.create();
        loginFacebook = (LoginButton)findViewById(R.id.login_button);
        facebook_sign_in_custom = findViewById(R.id.facebook_sign_in_custom);

        //    loginFacebook.setReadPermissions("public_profile");
        //   loginFacebook.setReadPermissions("email");
        //  loginFacebook.setReadPermissions("user_birthday");
       /* loginFacebook.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
*/
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
                    Toast.makeText(LoginOriginal.this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onViewClick(View view)
    {
        switch(view.getId())
        {
            case R.id.btn_login:

                if(checkValidations())
                {
                    CallApi callApi = new CallApi(API.login_with_otp);
                    callApi.addReqParams("email", login_email_mobile.getText().toString().trim());
                    processCallApi(callApi);
                }


                break;
            case R.id.forget_password:
                Intent i_forgot=new Intent(LoginOriginal.this,ForgotPassword.class);
                startActivity(i_forgot);

                break;

            case R.id.click_for_signup:
                /*Intent i = new Intent(LoginOriginal.this, Registration.class);
                startActivity(i);*/
                break;


        }

    }

    private Boolean checkValidations()
    {
        Boolean isValid = true;

        if(login_email_mobile.getText().toString().trim().length() == 0)
        {
            isValid = false;
            login_email_mobile.requestFocus();
            Toast.makeText(LoginOriginal.this, "Enter Mobile", Toast.LENGTH_SHORT).show();
            //loginId.setBackgroundResource(R.drawable.edit_text_border_red);
        }
        else if(!AppValidate.isMobileNumber(login_email_mobile.getText().toString().trim()))
        {
            isValid = false;
            login_email_mobile.requestFocus();
            Toast.makeText(LoginOriginal.this, "Enter correct Mobile no.", Toast.LENGTH_SHORT).show();
        }
        return isValid;
    }


    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {
        if(API.login_with_otp.method.equals(callApi.networkActivity.method))
        {
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");

                if (status.equalsIgnoreCase("Success"))
                {
                    JSONObject jsonObject1= action.getJSONObject("data");
                    UserCO userCO = AppController.gson.fromJson(jsonObject1.toString(), UserCO.class);

                    String userCoString = AppController.gson.toJson(userCO);
                    Intent i = new Intent(LoginOriginal.this, ForgotOTP.class);
                    i.putExtra("userCO", userCoString);
                    i.putExtra("isForgot", "NO");
                    startActivity(i);

                  /*  userCO.saveInDB();

                    Intent iDash =new Intent(LoginOriginal.this, MainActivity.class);
                    iDash.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(iDash);
                    finish();
*/
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

                    Intent iDash =new Intent(LoginOriginal.this, MainActivity.class);
                    iDash.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

}
