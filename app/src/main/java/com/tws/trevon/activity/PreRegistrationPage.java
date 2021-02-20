package com.tws.trevon.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;
import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import com.tws.trevon.BuildConfig;
import com.tws.trevon.R;
import com.tws.trevon.co.ImageCO;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.common.FileUtils;
import com.tws.trevon.common.OkHttp3CountingFileRequestBody;
import com.tws.trevon.constants.API;
import com.tws.trevon.constants.IReqParams;
import com.tws.trevon.constants.ISysCodes;
import com.tws.trevon.constants.ISysConfig;
import com.tws.trevon.fragment.FragmentImageSlider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PreRegistrationPage extends AbstractActivity implements  GoogleApiClient.OnConnectionFailedListener , AdapterView.OnItemSelectedListener {
    private static final String TAG = "SignIn";
    private static final int RC_SIGN_IN = 9001;
    private static final int FB_LOGIN = 64206;
    private SignInButton google_login;
    private GoogleApiClient mGoogleApiClient;
    ImageView google_sign_in_custom;
    CallbackManager callbackManager;
    LoginButton loginFacebook;
    ImageView facebook_sign_in_custom;
    Button btn_signup;

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 1;
    private String cameraFilePath;
    ProgressDialog pDialog;
    ArrayList<String> doc_type_list = new ArrayList<>();

    EditText pre_regis_adhaar_num;
    public String selected_doc_type;
    LinearLayout front_side_image, back_side_image;
    public String side = null;
    public String frontImage=null, backImage= null;
    String email, seller_type,company, gst;
    public TextView signin_text;
    LinearLayout preview_image;
    ImageView front_image_preview,back_image_preview;
    UserCO userCO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_pre_registration_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        email = getIntent().getStringExtra("email");
        seller_type = getIntent().getStringExtra("seller_type");
        company = getIntent().getStringExtra("company");
        gst = getIntent().getStringExtra("gst");

        doc_type_list.add("ADHAAR CARD");
        doc_type_list.add("PAN CARD");
        signin_text = findViewById(R.id.signin_text);
        populateSpinner(doc_type_list);
        printHashKey(this);
        btn_signup = findViewById(R.id.btn_signup);
        front_side_image = findViewById(R.id.front_side_image);
        back_side_image = findViewById(R.id.back_side_image);
        preview_image = findViewById(R.id.preview_image);
        front_image_preview = findViewById(R.id.front_image_preview);
        back_image_preview = findViewById(R.id.back_image_preview);
        front_image_preview.setOnClickListener(this);
        back_image_preview.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        front_side_image.setOnClickListener(this);
        back_side_image.setOnClickListener(this);
        signin_text.setOnClickListener(this);
        google_sign_in_custom = findViewById(R.id.google_sign_in_custom);
        pre_regis_adhaar_num = findViewById(R.id.pre_regis_adhaar_num);
        this.google_login   =      (SignInButton)  findViewById(R.id.google_login);
        google_login = (SignInButton) findViewById(R.id.google_login);
        //   google_login.setSize(SignInButton.SIZE_STANDARD);
        TextView textView = (TextView) google_login.getChildAt(0);
        textView.setText(getResources().getString(R.string.login_gg_text));

        String userCOString = getIntent().getStringExtra("userCO");
         userCO = AppController.gson.fromJson(userCOString, UserCO.class);

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
                else
                    {
                    connected = false;
                    Toast.makeText(PreRegistrationPage.this, "No internet. Check Your Internet is connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private Boolean checkValidations()
    {
        Boolean isValid = true;

        if(AppValidate.isEmpty(selected_doc_type))
        {
            isValid = false;
            Toast.makeText(PreRegistrationPage.this, "Select Doc Type", Toast.LENGTH_SHORT).show();
            //loginId.setBackgroundResource(R.drawable.edit_text_border_red);
        }
        else if(pre_regis_adhaar_num.getText().toString().trim().length() == 0)
        {
            isValid = false;
            pre_regis_adhaar_num.requestFocus();
            Toast.makeText(PreRegistrationPage.this, "Enter Document No.", Toast.LENGTH_SHORT).show();
        }
        else if(selected_doc_type.equals("ADHAAR CARD"))
        {
            if(pre_regis_adhaar_num.getText().toString().trim().length() != 12)
            {
                isValid = false;
                pre_regis_adhaar_num.requestFocus();
                Toast.makeText(PreRegistrationPage.this, "Enter Correct Document No.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(selected_doc_type.equals("PAN CARD"))
        {
            if(pre_regis_adhaar_num.getText().toString().trim().length() != 10)
            {
                isValid = false;
                pre_regis_adhaar_num.requestFocus();
                Toast.makeText(PreRegistrationPage.this, "Enter Correct Document No.", Toast.LENGTH_SHORT).show();
            }
        }
        if(AppValidate.isEmpty(frontImage))
        {
            isValid = false;
            Toast.makeText(PreRegistrationPage.this, "Select Front Image", Toast.LENGTH_SHORT).show();
        }
        else if(AppValidate.isEmpty(backImage))
        {
            isValid = false;
            Toast.makeText(PreRegistrationPage.this, "Select Back Image", Toast.LENGTH_SHORT).show();
        }
        return isValid;
    }

    @Override
    protected void onViewClick(View view)
    {
        switch(view.getId())
        {
            case R.id.btn_signup:
                if(checkValidations())
                {
                    CallApi callApi = new CallApi(API.user_signup_with_doc);
                    callApi.addReqParams("user_id", userCO.id);
                    callApi.addReqParams("doc_type", selected_doc_type);
                    callApi.addReqParams("doc_no", pre_regis_adhaar_num.getText().toString().trim());
                    callApi.addReqParams("doc_front_image", frontImage);
                    callApi.addReqParams("doc_back_image", backImage);
                    processCallApi(callApi);
                }
                break;

            case R.id.front_side_image:
                side = "FRONT";
                selectImage();
                break;

            case R.id.back_side_image:
                side = "BACK";
                selectImage();
                break;

            case R.id.signin_text:
                /*Intent i = new Intent(PreRegistrationPage.this, LoginOriginal.class);
                startActivity(i);*/
                break;


            case R.id.front_image_preview:
                if(AppValidate.isNotEmpty(frontImage))
                {
                    ImageCO imageCO = new ImageCO();
                    imageCO.url = frontImage;
                    imageCO.type = "image";

                    if(imageCO.type.toLowerCase().contains("pdf"))
                    {
                        AppUtilities.showPdfFromUrl(PreRegistrationPage.this, imageCO.url);
                    }
                    else if(imageCO.type.toLowerCase().contains("image"))
                    {
                        List<ImageCO> imageCOList = new ArrayList<>();
                        imageCOList.add(imageCO);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("images", new ArrayList(imageCOList));
                        bundle.putString("isPdfShow","no");
                        bundle.putInt("position", 0);
                        bundle.putInt("type",2);

                        FragmentTransaction ft =  getSupportFragmentManager().beginTransaction();
                        FragmentImageSlider newFragment = FragmentImageSlider.newInstance();
                        newFragment.setArguments(bundle);
                        newFragment.show(ft, "slideshow");
                    }
                }
                break;
            case R.id.back_image_preview:
                if(AppValidate.isNotEmpty(backImage)) {
                    ImageCO imageCO = new ImageCO();
                    imageCO.url = backImage;
                    imageCO.type = "image";

                    if (imageCO.type.toLowerCase().contains("pdf")) {
                        AppUtilities.showPdfFromUrl(PreRegistrationPage.this, imageCO.url);
                    } else if (imageCO.type.toLowerCase().contains("image")) {
                        List<ImageCO> imageCOList = new ArrayList<>();
                        imageCOList.add(imageCO);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("images", new ArrayList(imageCOList));
                        bundle.putString("isPdfShow", "no");
                        bundle.putInt("position", 0);
                        bundle.putInt("type", 2);

                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        FragmentImageSlider newFragment = FragmentImageSlider.newInstance();
                        newFragment.setArguments(bundle);
                        newFragment.show(ft, "slideshow");
                    }
                }
                break;






        }
        }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {

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

                    Intent iDash =new Intent(PreRegistrationPage.this, MainActivity.class);
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

        else if(API.user_signup_with_doc.method.equals(callApi.networkActivity.method))
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
                        Intent iDash =new Intent(PreRegistrationPage.this, MainActivity.class);
                        iDash.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(iDash);
                        finish();
                    }
                    else
                    {
                        Intent iDash =new Intent(PreRegistrationPage.this, com.tws.trevon.activity.sp.MainActivity.class);
                        iDash.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(iDash);
                        finish();
                    }



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
        else if(requestCode == FB_LOGIN)
        {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        else if (resultCode == RESULT_OK)
        {
            if (requestCode == 1)
            {
                Uri selectedImage = Uri.parse(cameraFilePath);

                try {
                    File orignalFile = FileUtils.from(PreRegistrationPage.this, selectedImage);

                    if(side.equals("BACK"))
                    {
                        uploadUsingOkHttpback(orignalFile);
                    }
                    else
                    {
                        uploadUsingOkHttpFront(orignalFile);
                    }

                    //get the current timeStamp and strore that in the time Variable
                    Long tsLong = System.currentTimeMillis() / 1000;
                    String timestamp = tsLong.toString();
                    //user_image.setImageURI(selectedImage);

                    //  Bitmap image2 = ((BitmapDrawable) user_image.getDrawable()).getBitmap();
                    //   new Upload(image2,"IMG_"+timestamp).execute();


                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else
            if (requestCode == 2)
            {
                Uri selectedImage = data.getData();
                try {
                    File orignalFile = FileUtils.from(PreRegistrationPage.this, selectedImage);
                    if(side.equals("BACK"))
                    {
                        uploadUsingOkHttpback(orignalFile);
                    }
                    else
                    {
                        uploadUsingOkHttpFront(orignalFile);
                    }
                    //get the current timeStamp and strore that in the time Variable
                    Long tsLong = System.currentTimeMillis() / 1000;
                    String timestamp = tsLong.toString();
                    //user_image.setImageURI(selectedImage);

                    //  Bitmap image2 = ((BitmapDrawable) user_image.getDrawable()).getBitmap();
                    //  new Upload(image2,"IMG_"+timestamp).execute();


                } catch (IOException e) {
                    e.printStackTrace();
                }
               /* String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                // Log.w("path of image from gallery......******************.........", picturePath+"");
                viewImage.setImageBitmap(thumbnail);*/
            }
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


    private void selectImage()
    {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(PreRegistrationPage.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                   /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);*/
                    if(hasCameraPermission()) {
                        captureFromCamera();
                    }
                    else
                    {
                        requestCameraPermission();
                    }

                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(PreRegistrationPage.this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA},
                MY_CAMERA_REQUEST_CODE );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_CAMERA_REQUEST_CODE :

                captureFromCamera();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void captureFromCamera() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(PreRegistrationPage.this, BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private File createImageFile() throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File file = null;
        try {
            file = File.createTempFile(timeStamp, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cameraFilePath = String.valueOf(Uri.fromFile(file));
        return file;
    }

  /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result code is RESULT_OK only if the user captures an Image

        if (resultCode == RESULT_OK)
        {
            if (requestCode == 1)
            {
                Uri selectedImage = Uri.parse(cameraFilePath);

                try {
                    File orignalFile = FileUtils.from(PreRegistrationPage.this, selectedImage);
                    uploadUsingOkHttp(orignalFile);
                    //get the current timeStamp and strore that in the time Variable
                    Long tsLong = System.currentTimeMillis() / 1000;
                    String timestamp = tsLong.toString();
                    //user_image.setImageURI(selectedImage);

                    //  Bitmap image2 = ((BitmapDrawable) user_image.getDrawable()).getBitmap();
                    //   new Upload(image2,"IMG_"+timestamp).execute();


                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else
            if (requestCode == 2)
            {
                Uri selectedImage = data.getData();
                try {
                    File orignalFile = FileUtils.from(PreRegistrationPage.this, selectedImage);
                    uploadUsingOkHttp(orignalFile);
                    //get the current timeStamp and strore that in the time Variable
                    Long tsLong = System.currentTimeMillis() / 1000;
                    String timestamp = tsLong.toString();
                    //user_image.setImageURI(selectedImage);

                    //  Bitmap image2 = ((BitmapDrawable) user_image.getDrawable()).getBitmap();
                    //  new Upload(image2,"IMG_"+timestamp).execute();


                } catch (IOException e) {
                    e.printStackTrace();
                }
               *//* String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                // Log.w("path of image from gallery......******************.........", picturePath+"");
                viewImage.setImageBitmap(thumbnail);*//*
            }
        }
    }*/


    private void uploadUsingOkHttpFront(File orignalFile)
    {
        pDialog = new ProgressDialog(PreRegistrationPage.this);
        pDialog.setMessage("Uploading...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setMax(100);
        pDialog.setProgress(0);
        pDialog.setCancelable(false);
        pDialog.show();

        //File imageFile = FileUtils.getFile(getActivity(), uri);;
        final long totalSize = orignalFile.length();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"doc_front_image\"; filename=\"" + orignalFile.getName() + "\""),
                        new OkHttp3CountingFileRequestBody(orignalFile, "image/*", new OkHttp3CountingFileRequestBody.ProgressListener() {
                            @Override
                            public void transferred(long num)
                            {
                                Float progress = (num / (float) totalSize) * 100;
                                pDialog.setProgress(progress.intValue());
                            }
                        })
                )
                .addPart(MultipartBody.Part.createFormData("user_id", userCO.id))

                /*  .addPart(MultipartBody.Part.createFormData(IReqParams.ACTION_REQUEST_PARAMETER, "cms_uploadImage"))
                  .addPart(MultipartBody.Part.createFormData(IReqParams.APP_VERSION_TAG_REQUEST_PARAMETER, Integer.toString(AppUtilities.getAppVersion())))
                  .addPart(MultipartBody.Part.createFormData(IReqParams.REQUEST_SOURCE_REQUEST_PARAMETER, ISysConfig.ANDROID))
                  .addPart(MultipartBody.Part.createFormData(IReqParams.APP_TYPE_REQUEST_PARAMETER, ISysConfig.CUSTOMER))
                  .addPart(MultipartBody.Part.createFormData(IReqParams.ACCESS_TOKEN_REQUEST_PARAMETER, AppUtilities.readFromPref(IPreferences.API_ACCESS_TOKEN)))*/
                //.addPart(MultipartBody.Part.createFormData("userCO", AppController.gson.toJson(user)))
                //.addPart(MultipartBody.Part.createFormData("userId", user.id))
                .build();


        okhttp3.Request request = new Request.Builder()
                .tag("anyName")
                .addHeader("source", ISysCodes.REQUEST_SOURCE_ANDROID)
                // .url(ISysConfig.SERVER_HIT_URL)
                .url(ISysConfig.SERVER_IMAGE_UPLOAD_URL + "/upload_doc_images")


                .post(requestBody)
                .build();

        okhttp3.Call call = (new OkHttpClient()).newCall(request);

        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    public void run() {
                        String myResponse = null;
                        try {
                            //image_path

                            myResponse = response.body().string();
                            JSONObject jsonObject = new JSONObject(myResponse);
                            JSONObject action = jsonObject.getJSONObject("response");
                            String nessage = action.getString("message");
                            String status = action.getString("status");

                            if (status.equalsIgnoreCase("Success")) {
                                JSONObject onbjnew = action.getJSONObject("data");
                                frontImage = onbjnew.getString("doc_front_image");
                                preview_image.setVisibility(View.VISIBLE);

                                Glide.with(PreRegistrationPage.this)
                                        .load(frontImage)
                                        .apply(RequestOptions.circleCropTransform().placeholderOf(R.drawable.error_images).error(R.drawable.error_images))
                                        .dontAnimate()
                                        .into(front_image_preview);
                            }
                            else
                            {
                                Toast.makeText(PreRegistrationPage.this, nessage, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
                    }
                });

            }

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                try {
                    pDialog.dismiss();
                    Toast.makeText(PreRegistrationPage.this, "Network error.", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    e.addSuppressed(ex);
                }

                //showUserMessage("Network error.");
            }
        });
    }

    private void uploadUsingOkHttpback(File orignalFile)
    {
        pDialog = new ProgressDialog(PreRegistrationPage.this);
        pDialog.setMessage("Uploading...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setMax(100);
        pDialog.setProgress(0);
        pDialog.setCancelable(false);
        pDialog.show();

        //File imageFile = FileUtils.getFile(getActivity(), uri);;
        final long totalSize = orignalFile.length();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"doc_back_image\"; filename=\"" + orignalFile.getName() + "\""),
                        new OkHttp3CountingFileRequestBody(orignalFile, "image/*", new OkHttp3CountingFileRequestBody.ProgressListener() {
                            @Override
                            public void transferred(long num)
                            {
                                Float progress = (num / (float) totalSize) * 100;
                                pDialog.setProgress(progress.intValue());
                            }
                        })
                )
                .addPart(MultipartBody.Part.createFormData("user_id", userCO.id))

                /*  .addPart(MultipartBody.Part.createFormData(IReqParams.ACTION_REQUEST_PARAMETER, "cms_uploadImage"))
                  .addPart(MultipartBody.Part.createFormData(IReqParams.APP_VERSION_TAG_REQUEST_PARAMETER, Integer.toString(AppUtilities.getAppVersion())))
                  .addPart(MultipartBody.Part.createFormData(IReqParams.REQUEST_SOURCE_REQUEST_PARAMETER, ISysConfig.ANDROID))
                  .addPart(MultipartBody.Part.createFormData(IReqParams.APP_TYPE_REQUEST_PARAMETER, ISysConfig.CUSTOMER))
                  .addPart(MultipartBody.Part.createFormData(IReqParams.ACCESS_TOKEN_REQUEST_PARAMETER, AppUtilities.readFromPref(IPreferences.API_ACCESS_TOKEN)))*/
                //.addPart(MultipartBody.Part.createFormData("userCO", AppController.gson.toJson(user)))
                //.addPart(MultipartBody.Part.createFormData("userId", user.id))
                .build();


        okhttp3.Request request = new Request.Builder()
                .tag("anyName")
                .addHeader("source", ISysCodes.REQUEST_SOURCE_ANDROID)
                // .url(ISysConfig.SERVER_HIT_URL)
                .url(ISysConfig.SERVER_IMAGE_UPLOAD_URL + "/upload_doc_back_image")


                .post(requestBody)
                .build();

        okhttp3.Call call = (new OkHttpClient()).newCall(request);

        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    public void run() {
                        String myResponse = null;
                        try {
                            //image_path

                            myResponse = response.body().string();
                            JSONObject jsonObject = new JSONObject(myResponse);
                            JSONObject action = jsonObject.getJSONObject("response");
                            String nessage = action.getString("message");
                            String status = action.getString("status");

                            if (status.equalsIgnoreCase("Success")) {
                                JSONObject onbjnew = action.getJSONObject("data");
                                backImage = onbjnew.getString("doc_back_image");

                                preview_image.setVisibility(View.VISIBLE);

                                Glide.with(PreRegistrationPage.this)
                                        .load(backImage)
                                        .apply(RequestOptions.circleCropTransform().placeholderOf(R.drawable.error_images).error(R.drawable.error_images))
                                        .dontAnimate()
                                        .into(back_image_preview);

                            }
                            else
                            {
                                Toast.makeText(PreRegistrationPage.this, nessage, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
                    }
                });

            }

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                try {
                    pDialog.dismiss();
                    Toast.makeText(PreRegistrationPage.this, "Network error.", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    e.addSuppressed(ex);
                }

                //showUserMessage("Network error.");
            }
        });
    }


    private void populateSpinner(ArrayList<String> tenureCOList)
    {
        // Get reference of widgets from XML layout
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);

        final List<String> plantsList = new ArrayList<>();
        plantsList.add("Select Doc Type");
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
            selected_doc_type = (String) parent.getItemAtPosition(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        // TODO Auto-generated method stub
    }

}