package com.tws.trevon.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentTransaction;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tws.trevon.BuildConfig;
import com.tws.trevon.R;
import com.tws.trevon.co.ImageCO;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.common.FileUtils;
import com.tws.trevon.common.OkHttp3CountingFileRequestBody;
import com.tws.trevon.constants.API;
import com.tws.trevon.constants.IConstants;
import com.tws.trevon.constants.IReqParams;
import com.tws.trevon.constants.ISysCodes;
import com.tws.trevon.constants.ISysConfig;
import com.tws.trevon.fragment.FragmentImageSlider;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditBuisnessProfile extends AbstractActivity implements AdapterView.OnItemSelectedListener {

    EditText firm_name, firm_address, gst_no,phone_no;
    LinearLayout upload_gst_certificate,submit_button,ll_select_user_profile;

    CircleImageView user_image;
    TextView user_name,user_phone_number, wallet_amount;

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 1;
    private String cameraFilePath;
    ProgressDialog pDialog;
    public String uploadedDoc="";
    ImageView certificate_gst;
    ImageView crown;
    ArrayList<String> seller_type_list = new ArrayList<>();
    public String selected_seller_type;
    public String IMAGE_TYPE="";
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_buisness_profile);

        Toolbar toolbar = findViewById(R.id.edit_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit Business");

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        spinner = findViewById(R.id.spinner);
        ll_select_user_profile = findViewById(R.id.ll_select_user_profile);
        ll_select_user_profile.setVisibility(View.VISIBLE);
        ll_select_user_profile.setOnClickListener(this);
        user_image = findViewById(R.id.user_image);
        user_name = findViewById(R.id.user_name);
        user_phone_number = findViewById(R.id.user_phone_number);
        wallet_amount = findViewById(R.id.wallet_amount);

        certificate_gst = findViewById(R.id.certificate_gst);
        firm_name = findViewById(R.id.firm_name);
        firm_address = findViewById(R.id.firm_address);
        gst_no = findViewById(R.id.gst_no);
        phone_no = findViewById(R.id.phone_no);
        upload_gst_certificate = findViewById(R.id.upload_gst_certificate);
        submit_button= findViewById(R.id.submit_button);
        upload_gst_certificate.setOnClickListener(this);
        submit_button.setOnClickListener(this);
        user_image.setOnClickListener(this);

        seller_type_list.add("RETAILER");
        seller_type_list.add("WHOLESELLER");

        populateSpinner(seller_type_list);


        firm_name.setText(UserCO.getUserCOFromDB().company_name);

        firm_address.setText(UserCO.getUserCOFromDB().firm_address);
        gst_no.setText(UserCO.getUserCOFromDB().gst_no);
        phone_no.setText(UserCO.getUserCOFromDB().business_mobile_no);

        gst_no.addTextChangedListener(new TextWatcher() {

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
                    gst_no.setText(s);
                    gst_no.setSelection(gst_no.length()); //fix reverse texting
                }
            }
        });

        crown = findViewById(R.id.crown);
        if(UserCO.getUserCOFromDB().is_premium.equals("1"))
        {
            crown.setVisibility(View.VISIBLE);
        }
        else
        {
            crown.setVisibility(View.GONE);
        }

        if(AppValidate.isEmpty(UserCO.getUserCOFromDB().gst_certificate))
        {
            uploadedDoc="";
            certificate_gst.setVisibility(View.GONE);
        }
        else
        {
            certificate_gst.setVisibility(View.VISIBLE);
            Glide.with(EditBuisnessProfile.this)
                    .load(UserCO.getUserCOFromDB().gst_certificate)
                    .apply(RequestOptions.circleCropTransform().placeholderOf(R.drawable.error_images).error(R.drawable.error_images))
                    .dontAnimate()
                    .into(certificate_gst);
            uploadedDoc = UserCO.getUserCOFromDB().gst_certificate;

        }

        user_name.setText(UserCO.getUserCOFromDB().username);
        user_phone_number.setText(UserCO.getUserCOFromDB().mobile);
        wallet_amount.setText(UserCO.getUserCOFromDB().wallet_amount);

        Glide.with(EditBuisnessProfile.this)
                .load(UserCO.getUserCOFromDB().business_profile_image)
                .apply(RequestOptions.circleCropTransform().placeholderOf(R.drawable.error_images).error(R.drawable.error_images))
                .dontAnimate()
                .into(user_image);
        certificate_gst.setOnClickListener(this);


                if( UserCO.getUserCOFromDB().firm_type.equals("RESELLER"))
                {
                    spinner.setSelection(1);
                    selected_seller_type=UserCO.getUserCOFromDB().firm_type;
                }
                else
                {
                    selected_seller_type=UserCO.getUserCOFromDB().firm_type;
                    spinner.setSelection(2);
                }

    }

    private Boolean checkValidations()
    {
        Boolean isValid = true;

        if(firm_name.getText().toString().trim().length() == 0)
        {
            isValid = false;
            firm_name.requestFocus();
            Toast.makeText(EditBuisnessProfile.this, "Enter firm name", Toast.LENGTH_SHORT).show();
            //loginId.setBackgroundResource(R.drawable.edit_text_border_red);
        }

        else if(AppValidate.isEmpty(selected_seller_type))
        {
            isValid = false;
            Toast.makeText(EditBuisnessProfile.this, "Select firm type", Toast.LENGTH_SHORT).show();
            //loginId.setBackgroundResource(R.drawable.edit_text_border_red);
        }
        else if(firm_address.getText().toString().trim().length() == 0)
        {
            isValid = false;
            firm_address.requestFocus();
            Toast.makeText(EditBuisnessProfile.this, "Enter firm address", Toast.LENGTH_SHORT).show();
            //loginId.setBackgroundResource(R.drawable.edit_text_border_red);
        }
        else if(gst_no.getText().toString().trim().length() == 0)
        {
            isValid = false;
            gst_no.requestFocus();
            Toast.makeText(EditBuisnessProfile.this, "Enter GST no.", Toast.LENGTH_SHORT).show();
            //loginId.setBackgroundResource(R.drawable.edit_text_border_red);
        }
        else if(gst_no.getText().toString().trim().length() != 15)
        {
            isValid = false;
            gst_no.requestFocus();
            Toast.makeText(EditBuisnessProfile.this, "Enter Correct GST No.", Toast.LENGTH_SHORT).show();
        }
        else if(phone_no.getText().toString().trim().length() == 0)
        {
            isValid = false;
            phone_no.requestFocus();
            Toast.makeText(EditBuisnessProfile.this, "Enter mobile no.", Toast.LENGTH_SHORT).show();
            //loginId.setBackgroundResource(R.drawable.edit_text_border_red);
        }
        else if(!AppValidate.isMobileNumber(phone_no.getText().toString().trim()))
        {
            isValid = false;
            phone_no.requestFocus();
            Toast.makeText(EditBuisnessProfile.this, "Enter correct Mobile no.", Toast.LENGTH_SHORT).show();
        }
        else if(AppValidate.isEmpty(uploadedDoc))
        {
            isValid = false;
            Toast.makeText(EditBuisnessProfile.this, "Upload GST certificate", Toast.LENGTH_SHORT).show();
            //loginId.setBackgroundResource(R.drawable.edit_text_border_red);
        }


        return isValid;
    }

    @Override
    protected void onViewClick(View view)
    {
        switch(view.getId())
        {
            case R.id.submit_button:
                if(checkValidations())
                {
                    CallApi callApi = new CallApi(API.edit_company_profile);
                    callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
                    callApi.addReqParams("company_name", firm_name.getText().toString().trim());
                    callApi.addReqParams("firm_type", selected_seller_type);
                    callApi.addReqParams("mobile", phone_no.getText().toString().trim());
                    callApi.addReqParams("firm_address", firm_address.getText().toString().trim());
                    callApi.addReqParams("gst_no", gst_no.getText().toString().trim());
                    callApi.addReqParams("gst_certificate", uploadedDoc);
                    processCallApi(callApi);

                }
                break;

            case R.id.upload_gst_certificate:
                IMAGE_TYPE= "CERTIFICATE";
                selectImage();
                break;

            case R.id.certificate_gst:
                if(AppValidate.isNotEmpty(uploadedDoc))
                {
                    ImageCO imageCO = new ImageCO();
                    imageCO.url = uploadedDoc;
                    imageCO.type = "image";

                    if(imageCO.type.toLowerCase().contains("pdf"))
                    {
                        AppUtilities.showPdfFromUrl(EditBuisnessProfile.this, imageCO.url);
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

            case R.id.ll_select_user_profile:
                IMAGE_TYPE = "PROFILE";
                selectImage();
                break;

            case R.id.user_image:
                List<ImageCO> imageCOList = new ArrayList<>();
                ImageCO imageCO = new ImageCO();
                imageCO.url = UserCO.getUserCOFromDB().image;
                imageCO.type = "image";
                imageCOList.add(imageCO);

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("images", new ArrayList(imageCOList));
                bundle.putString("isPdfShow","no");
                bundle.putInt("position", 0);
                bundle.putInt("type",2);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                FragmentImageSlider newFragment = FragmentImageSlider.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
                break;


        }
    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {
        if(API.edit_company_profile.method.equals(callApi.networkActivity.method))
        {
            try
            {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");
                if (status.equalsIgnoreCase("Success"))
                {
                    JSONObject jsonObject1= action.getJSONObject("data");

                    AppUtilities.writeToPref(IReqParams.COMPANY_NAME,  jsonObject1.getString("company_name"));
                    AppUtilities.writeToPref(IReqParams.BUSINESS_MOBILE_NUMBER, jsonObject1.getString("business_mobile_no"));
                    AppUtilities.writeToPref(IReqParams.FIRM_TYPE, jsonObject1.getString("firm_type"));
                    AppUtilities.writeToPref(IReqParams.GST, jsonObject1.getString("gst_no"));
                    AppUtilities.writeToPref(IReqParams.FIRM_ADDRESS, jsonObject1.getString("firm_address"));
                    AppUtilities.writeToPref(IReqParams.GST_CERTIFICATE, jsonObject1.getString("gst_certificate"));

                    finish();
                }
                else {

                    Toast.makeText(this, nessage, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }



    private void selectImage()
    {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(EditBuisnessProfile.this);
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
        return ContextCompat.checkSelfPermission(EditBuisnessProfile.this,
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
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(EditBuisnessProfile.this, BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
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



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result code is RESULT_OK only if the user captures an Image

        if (resultCode == RESULT_OK)
        {
            if (requestCode == 1)
            {
                Uri selectedImage = Uri.parse(cameraFilePath);

                try {
                    File orignalFile = FileUtils.from(EditBuisnessProfile.this, selectedImage);
                    if(IMAGE_TYPE.equals("CERTIFICATE"))
                    {
                        uploadUsingOkHttpGstCertificate(orignalFile);
                    }
                    else
                    {
                        uploadUsingOkHttp(orignalFile);
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
                    File orignalFile = FileUtils.from(EditBuisnessProfile.this, selectedImage);
                   // uploadUsingOkHttp(orignalFile);
                    if(IMAGE_TYPE.equals("CERTIFICATE"))
                    {
                        uploadUsingOkHttpGstCertificate(orignalFile);
                    }
                    else
                    {
                        uploadUsingOkHttp(orignalFile);
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

    private void uploadUsingOkHttpGstCertificate(File orignalFile)
    {
        pDialog = new ProgressDialog(EditBuisnessProfile.this);
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
                        Headers.of("Content-Disposition", "form-data; name=\"gst_certificate\"; filename=\"" + orignalFile.getName() + "\""),
                        new OkHttp3CountingFileRequestBody(orignalFile, "image/*", new OkHttp3CountingFileRequestBody.ProgressListener() {
                            @Override
                            public void transferred(long num)
                            {
                                Float progress = (num / (float) totalSize) * 100;
                                pDialog.setProgress(progress.intValue());
                            }
                        })
                )
                .addPart(MultipartBody.Part.createFormData("user_id", UserCO.getUserCOFromDB().id))

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
                .url(ISysConfig.SERVER_IMAGE_UPLOAD_URL + "/add_gst_certificate")


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

                            if (status.equalsIgnoreCase("Success"))
                            {
                                JSONObject onbjnew = action.getJSONObject("data");
                                uploadedDoc = onbjnew.getString("gst_certificate");

                                AppUtilities.writeToPref(IReqParams.GST_CERTIFICATE, uploadedDoc);

                                certificate_gst.setVisibility(View.VISIBLE);
                                Glide.with(EditBuisnessProfile.this)
                                        .load(uploadedDoc)
                                        .apply(RequestOptions.circleCropTransform().placeholderOf(R.drawable.error_images).error(R.drawable.error_images))
                                        .dontAnimate()
                                        .into(certificate_gst);

                            }
                            else
                            {
                                Toast.makeText(EditBuisnessProfile.this, nessage, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
                    }
                });

            }

            @Override
            public void onFailure(okhttp3.Call call, IOException e)
            {
                try {
                    pDialog.dismiss();
                    Toast.makeText(EditBuisnessProfile.this, "Network error.", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    e.addSuppressed(ex);
                }

                //showUserMessage("Network error.");
            }
        });
    }


    private void uploadUsingOkHttp(File orignalFile)
    {
        pDialog = new ProgressDialog(EditBuisnessProfile.this);
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
                        Headers.of("Content-Disposition", "form-data; name=\"business_profile_image\"; filename=\"" + orignalFile.getName() + "\""),
                        new OkHttp3CountingFileRequestBody(orignalFile, "image/*", new OkHttp3CountingFileRequestBody.ProgressListener() {
                            @Override
                            public void transferred(long num)
                            {
                                Float progress = (num / (float) totalSize) * 100;
                                pDialog.setProgress(progress.intValue());
                            }
                        })
                )
                .addPart(MultipartBody.Part.createFormData("user_id", UserCO.getUserCOFromDB().id))

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
                .url(ISysConfig.SERVER_IMAGE_UPLOAD_URL + "/upload_business_profile_image")


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

                            if (status.equalsIgnoreCase("Success"))
                            {
                                JSONObject onbjnew = action.getJSONObject("data");
                                uploadedDoc = onbjnew.getString("business_profile_image");

                                AppUtilities.writeToPref(IReqParams.BUSINESS_PROFILE_IMAGE, uploadedDoc);
                                //certificate_gst.setVisibility(View.VISIBLE);

                                Glide.with(EditBuisnessProfile.this)
                                        .load(uploadedDoc)
                                        .apply(RequestOptions.circleCropTransform().placeholderOf(R.drawable.error_images).error(R.drawable.error_images))
                                        .dontAnimate()
                                        .into(user_image);

                            }
                            else
                            {
                                Toast.makeText(EditBuisnessProfile.this, nessage, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
                    }
                });

            }

            @Override
            public void onFailure(okhttp3.Call call, IOException e)
            {
                try {
                    pDialog.dismiss();
                    Toast.makeText(EditBuisnessProfile.this, "Network error.", Toast.LENGTH_SHORT).show();
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
        plantsList.add("Firm Type/Wholeseller");
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
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        // TODO Auto-generated method stub
    }


    TextView textCartItemCount;
    int mCartItemCount;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_option_menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        MenuItem  menuItem2 = menu.findItem(R.id.notification_home);
        menuItem2.setVisible(false);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        if(!AppValidate.isEmpty(AppUtilities.readFromPref(IReqParams.USER_CART_COUNT)))
        {
            mCartItemCount = Integer.parseInt(AppUtilities.readFromPref(IReqParams.USER_CART_COUNT));
            setupBadge();
        }

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

         if (item.getItemId()==R.id.action_search)
        {
            Intent i= new Intent(EditBuisnessProfile.this, SearchPage.class);
            startActivity(i);
        }

        else if (item.getItemId()==R.id.notification_home)
        {
            UserCO userCO =  UserCO.getUserCOFromDB();
            if(AppValidate.isNotEmpty(userCO.id))
            {
                Intent i =new Intent(EditBuisnessProfile.this,Notification.class);
                startActivityForResult(i, IConstants.INTENT_CART_COUNT_CODE);// Activity is started with requestCode 2
            }
            else
            {
                Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
                Intent ilogin = new Intent(EditBuisnessProfile.this,LoginOriginal.class);
                ilogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(ilogin);
                finish();
            }

        }
        else if (item.getItemId()==R.id.action_cart)
        {
            UserCO userCO =  UserCO.getUserCOFromDB();
            if(AppValidate.isNotEmpty(userCO.id))
            {
                Intent i =new Intent(EditBuisnessProfile.this,CartScreen.class);
                startActivityForResult(i, IConstants.INTENT_CART_COUNT_CODE);// Activity is started with requestCode 2
            }
            else
            {
                Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
                Intent ilogin = new Intent(EditBuisnessProfile.this,LoginOriginal.class);
                ilogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(ilogin);
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupBadge()
    {
        if (textCartItemCount != null)
        {
            if (mCartItemCount == 0)
            {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }



}