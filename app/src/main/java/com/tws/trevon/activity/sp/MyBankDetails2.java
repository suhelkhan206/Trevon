package com.tws.trevon.activity.sp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.MenuItemCompat;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tws.trevon.BuildConfig;
import com.tws.trevon.R;
import com.tws.trevon.activity.AbstractActivity;
import com.tws.trevon.co.BankDetailCO;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppController;
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

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyBankDetails2 extends AbstractActivity {
    EditText acc_no,cacc_no, acc_hname, ifsc, bank_name;
    ImageView mybankdetail;
    LinearLayout submit_button;
    EditText phone_pay_number,google_pay_number,paytm_number, amazone_pay_number;

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 1;
    private String cameraFilePath;
    ProgressDialog pDialog;
    public String uploadedDoc="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bank_details2);
        Toolbar toolbar = findViewById(R.id.my_bank_details);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Bank Details");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        acc_no=(EditText)findViewById(R.id.account_number);
        cacc_no=(EditText)findViewById(R.id.confirm_account_number);
        acc_hname=(EditText)findViewById(R.id.account_holders_name);
        bank_name=(EditText)findViewById(R.id.bank_name);
        ifsc=(EditText)findViewById(R.id.ifsc_code);
        mybankdetail=(ImageView)findViewById(R.id.mybankdetail);
        submit_button = findViewById(R.id.submit_button);

        phone_pay_number = findViewById(R.id.phone_pay_number);
        google_pay_number = findViewById(R.id.google_pay_number);
        paytm_number = findViewById(R.id.paytm_number);
        amazone_pay_number = findViewById(R.id.amazone_pay_number);

        submit_button.setOnClickListener(this);
        mybankdetail.setOnClickListener(this);

        CallApi callApi = new CallApi(API.get_bank_details);
        callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
        processCallApi(callApi);
    }

    public boolean chekAddrValidation()
    {
        Boolean isValid = true;

        if (acc_no.getText().toString().trim().length() == 0) {
            isValid = false;
            acc_no.requestFocus();
            Toast.makeText(MyBankDetails2.this, "Enter Account Number", Toast.LENGTH_SHORT).show();
        }
        else if (cacc_no.getText().toString().trim().length() == 0) {
            isValid = false;
            cacc_no.requestFocus();
            Toast.makeText(MyBankDetails2.this, "Enter Confirm Account Number", Toast.LENGTH_SHORT).show();
        }
        else if(!acc_no.getText().toString().trim().equals(cacc_no.getText().toString().trim()))
        {
            isValid = false;
            cacc_no.requestFocus();
            Toast.makeText(MyBankDetails2.this, "Account and Confirm Account Number not match", Toast.LENGTH_SHORT).show();
        }
        else if (acc_hname.getText().toString().trim().length() == 0) {

            isValid = false;
            acc_hname.requestFocus();
            Toast.makeText(MyBankDetails2.this, "Enter Account holder name", Toast.LENGTH_SHORT).show();
        }
        else if (bank_name.getText().toString().trim().length() == 0) {

            isValid = false;
            bank_name.requestFocus();
            Toast.makeText(MyBankDetails2.this, "Enter bank name", Toast.LENGTH_SHORT).show();
        }
        else if (ifsc.getText().toString().trim().length() == 0) {

            isValid = false;
            ifsc.requestFocus();
            Toast.makeText(MyBankDetails2.this, "Enter IFSC code", Toast.LENGTH_SHORT).show();
        }


        if(phone_pay_number.getText().toString().trim().length() > 0)
        {
            if(!AppValidate.isMobileNumber(phone_pay_number.getText().toString()))
            {
                isValid = false;
                phone_pay_number.requestFocus();
                Toast.makeText(MyBankDetails2.this, "Enter Correct Phonepay Number", Toast.LENGTH_SHORT).show();
            }
        }
         if(google_pay_number.getText().toString().trim().length() > 0)
        {
            if(!AppValidate.isMobileNumber(google_pay_number.getText().toString()))
            {
                isValid = false;
                google_pay_number.requestFocus();
                Toast.makeText(MyBankDetails2.this, "Enter Correct GooglePay Number", Toast.LENGTH_SHORT).show();
            }
        }
         if(paytm_number.getText().toString().trim().length() > 0)
        {
            if(!AppValidate.isMobileNumber(paytm_number.getText().toString()))
            {
                isValid = false;
                paytm_number.requestFocus();
                Toast.makeText(MyBankDetails2.this, "Enter Correct Paytm Number", Toast.LENGTH_SHORT).show();
            }
        }
         if(amazone_pay_number.getText().toString().trim().length() > 0)
        {
            if(!AppValidate.isMobileNumber(amazone_pay_number.getText().toString()))
            {
                isValid = false;
                amazone_pay_number.requestFocus();
                Toast.makeText(MyBankDetails2.this, "Enter Correct AmazonPay Number", Toast.LENGTH_SHORT).show();
            }
        }


        return isValid;
    }


    @Override
    protected void onViewClick(View view) {
        switch(view.getId()) {
            case R.id.submit_button:

                if (chekAddrValidation())
                {
                    CallApi callApi = new CallApi(API.edit_bank_details);
                    callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
                    callApi.addReqParams("account_no", acc_no.getText().toString().trim());
                    // callApi.addReqParams("confirm_account_no", cacc_no.getText().toString().trim());
                    callApi.addReqParams("account_holder_name", acc_hname.getText().toString().trim());
                    callApi.addReqParams("bank_name", bank_name.getText().toString().trim());
                    callApi.addReqParams("ifsc_code", ifsc.getText().toString().trim());
                    callApi.addReqParams("phonePe",phone_pay_number.getText().toString().trim());
                    callApi.addReqParams("google_pay", google_pay_number.getText().toString().trim());
                    callApi.addReqParams("paytm", paytm_number.getText().toString().trim());
                    callApi.addReqParams("amazone_pay", amazone_pay_number.getText().toString().trim());

                    processCallApi(callApi);

                }
                break;

            case R.id.mybankdetail:
                selectImage();
                break;
        }

    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {

        if(API.edit_bank_details.method.equals(callApi.networkActivity.method))
        {
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            finish();
        }

        if(API.get_bank_details.method.equals(callApi.networkActivity.method))
        {
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");

                if (status.equalsIgnoreCase("Success"))
                {
                    JSONObject jsonObject1= action.getJSONObject("data");
                    BankDetailCO bankDetailCO = AppController.gson.fromJson(jsonObject1.toString(), BankDetailCO.class);
                    acc_no.setText(bankDetailCO.account_no);
                    cacc_no.setText(bankDetailCO.account_no);
                    acc_hname.setText(bankDetailCO.account_holder_name);
                    ifsc.setText(bankDetailCO.ifsc_code);
                    bank_name.setText(bankDetailCO.bank_name);
                    phone_pay_number.setText(bankDetailCO.phonepe);
                    google_pay_number.setText(bankDetailCO.google_pay);
                    paytm_number.setText(bankDetailCO.paytm);
                    amazone_pay_number.setText(bankDetailCO.amazon_pay);
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



    private void selectImage()
    {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(MyBankDetails2.this);
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
        return ContextCompat.checkSelfPermission(MyBankDetails2.this,
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
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(MyBankDetails2.this, BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
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
                    File orignalFile = FileUtils.from(MyBankDetails2.this, selectedImage);
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
                    File orignalFile = FileUtils.from(MyBankDetails2.this, selectedImage);
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


    private void uploadUsingOkHttp(File orignalFile)
    {
        pDialog = new ProgressDialog(MyBankDetails2.this);
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
                        Headers.of("Content-Disposition", "form-data; name=\"passbook_image\"; filename=\"" + orignalFile.getName() + "\""),
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
                .url(ISysConfig.SERVER_IMAGE_UPLOAD_URL + "/upload_user_passbook_image")


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
                                uploadedDoc = onbjnew.getString("passbook_image");

                                //AppUtilities.writeToPref(IReqParams.LOGGED_IN_USER_PROFILE_IMAGE, path);

                                Glide.with(MyBankDetails2.this)
                                        .load(uploadedDoc)
                                        .apply(RequestOptions.circleCropTransform().placeholderOf(R.drawable.error_images).error(R.drawable.error_images))
                                        .dontAnimate()
                                        .into(mybankdetail);
                            } else {
                                Toast.makeText(MyBankDetails2.this, nessage, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MyBankDetails2.this, "Network error.", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    e.addSuppressed(ex);
                }

                //showUserMessage("Network error.");
            }
        });
    }



}