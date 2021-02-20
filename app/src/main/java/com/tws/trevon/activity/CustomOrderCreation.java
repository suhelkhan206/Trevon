package com.tws.trevon.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tws.trevon.BuildConfig;
import com.tws.trevon.R;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.common.FileUtils;
import com.tws.trevon.common.OkHttp3CountingFileRequestBody;
import com.tws.trevon.constants.API;
import com.tws.trevon.constants.IReqParams;
import com.tws.trevon.constants.ISysCodes;
import com.tws.trevon.constants.ISysConfig;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomOrderCreation extends AbstractActivity {

    LinearLayout upload_image;
    EditText message, mobile_number;
    LinearLayout submit_data;

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 1;
    private String cameraFilePath;
    ProgressDialog pDialog;
    public String uploadedDoc="";

    TextView image_url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_order_creation);
        Toolbar toolbar = findViewById(R.id.order_your_design_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Order Your Design");

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        upload_image = findViewById(R.id.upload_image);
        message = findViewById(R.id.message);
        mobile_number = findViewById(R.id.mobile_number);
        submit_data = findViewById(R.id.submit_data);
        image_url = findViewById(R.id.image_url);
        submit_data.setOnClickListener(this);
        upload_image.setOnClickListener(this);
        mobile_number.setText(UserCO.getUserCOFromDB().mobile);
    }

    public boolean chekAddrValidation()
    {
        Boolean isValid = true;

        if (AppValidate.isEmpty(uploadedDoc)) {
            isValid = false;
            Toast.makeText(CustomOrderCreation.this, "Please Select Image...", Toast.LENGTH_SHORT).show();
        } else if (mobile_number.getText().toString().trim().length() == 0) {
            isValid = false;
            mobile_number.requestFocus();
            Toast.makeText(CustomOrderCreation.this, "Enter Mobile Number...", Toast.LENGTH_SHORT).show();
        } else if (message.getText().toString().trim().length() == 0) {
            isValid = false;
            message.requestFocus();
            Toast.makeText(CustomOrderCreation.this, "Enter Message...", Toast.LENGTH_SHORT).show();
        }


        return isValid;
    }

    @Override
    protected void onViewClick(View view) {
        switch(view.getId()) {
            case R.id.upload_image:
                selectImage();
                break;
            case R.id.submit_data:
                if (chekAddrValidation())
                {
                    CallApi callApi = new CallApi(API.add_custom);
                    callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
                    callApi.addReqParams("message", message.getText().toString().trim());
                    callApi.addReqParams("mobile", mobile_number.getText().toString().trim());
                    callApi.addReqParams("image", uploadedDoc);
                    processCallApi(callApi);
                }
                break;
        }
    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {
        if(API.add_custom.method.equals(callApi.networkActivity.method))
        {
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");
                if (status.equalsIgnoreCase("Success"))
                {
                    finish();
                }
                else
                {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomOrderCreation.this);
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
        return ContextCompat.checkSelfPermission(CustomOrderCreation.this,
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
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(CustomOrderCreation.this, BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
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

        if (resultCode == RESULT_OK)
        {
            if (requestCode == 1)
            {
                Uri selectedImage = Uri.parse(cameraFilePath);

                try {
                    File orignalFile = FileUtils.from(CustomOrderCreation.this, selectedImage);

                    uploadUsingOkHttpCreatePRO(orignalFile);

                } catch (IOException e)
                {
                    e.printStackTrace();
                }

            } else
            if (requestCode == 2)
            {
                Uri selectedImage = data.getData();
                try
                {
                    File orignalFile = FileUtils.from(CustomOrderCreation.this, selectedImage);
                    uploadUsingOkHttpCreatePRO(orignalFile);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }



    private void uploadUsingOkHttpCreatePRO(File orignalFile)
    {
        pDialog = new ProgressDialog(CustomOrderCreation.this);
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
                        Headers.of("Content-Disposition", "form-data; name=\"image\"; filename=\"" + orignalFile.getName() + "\""),
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
                .url(ISysConfig.SERVER_IMAGE_UPLOAD_URL + "/custom_order")
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
                                 uploadedDoc = onbjnew.getString("image");
                                image_url.setText("Uploaded Image: "+uploadedDoc);
                               /* AppUtilities.writeToPref(IReqParams.LOGGED_IN_USER_PROFILE_IMAGE, uploadedDoc);

                                Glide.with(getActivity())
                                        .load(uploadedDoc)
                                        .apply(RequestOptions.circleCropTransform().placeholderOf(R.drawable.error_images).error(R.drawable.error_images))
                                        .dontAnimate()
                                        .into(user_image);*/
                            } else {
                                Toast.makeText(CustomOrderCreation.this, nessage, Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch (Exception e)
                        {
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
                    Toast.makeText(CustomOrderCreation.this, "Network error.", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    e.addSuppressed(ex);
                }

                //showUserMessage("Network error.");
            }
        });
    }
}