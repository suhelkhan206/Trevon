package com.tws.trevon.fragment;

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

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.tws.trevon.BuildConfig;
import com.tws.trevon.R;
import com.tws.trevon.activity.ChangePassword;
import com.tws.trevon.activity.CustomOrderCreation;
import com.tws.trevon.activity.EditBuisnessProfile;
import com.tws.trevon.activity.EditProfileActivity;
import com.tws.trevon.activity.EliteAuthorActivity;
import com.tws.trevon.activity.LoginOriginal;
import com.tws.trevon.activity.MainActivity;
import com.tws.trevon.activity.MyAddress;
import com.tws.trevon.activity.MyBankDetails;
import com.tws.trevon.activity.MyConnectionActivity;
import com.tws.trevon.activity.MyEarnings;
import com.tws.trevon.activity.MyOrderList;
import com.tws.trevon.activity.MySharedCatelog;
import com.tws.trevon.activity.MyWishList;
import com.tws.trevon.activity.Notification;
import com.tws.trevon.co.ImageCO;
import com.tws.trevon.co.User;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.common.FileUtils;
import com.tws.trevon.common.OkHttp3CountingFileRequestBody;
import com.tws.trevon.constants.IConstants;
import com.tws.trevon.constants.IReqParams;
import com.tws.trevon.constants.ISysCodes;
import com.tws.trevon.constants.ISysConfig;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MyAccount extends AbstractFragment
{
    Uri mInvitationUrl;
    View view;
    LinearLayout create_your_profile,my_address,my_connection,become_elite_author,profile,my_wish_list, my_order, my_earning, invite_friend
            ,my_bank_detail,logout,change_password;


    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 1;
    private String cameraFilePath;
    ProgressDialog pDialog;
    public String uploadedDoc="";

    CircleImageView user_image;
    TextView   user_name,user_phone_number, wallet_amount;
    LinearLayout ll_select_user_profile;
    ImageView crown;

    public MyAccount() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_account, container, false);

        user_image = view.findViewById(R.id.user_image);
        user_name = view.findViewById(R.id.user_name);
        user_phone_number = view.findViewById(R.id.user_phone_number);
        wallet_amount = view.findViewById(R.id.wallet_amount);
        create_your_profile = view.findViewById(R.id.create_your_profile);
        become_elite_author = view.findViewById(R.id.become_elite_author);
        my_connection = view.findViewById(R.id.my_connection);
        my_address = view.findViewById(R.id.my_address);
        profile = view.findViewById(R.id.profile);
        my_wish_list = view.findViewById(R.id.my_wish_list);
        my_order = view.findViewById(R.id.my_order);
        my_earning = view.findViewById(R.id.my_earning);
        invite_friend = view.findViewById(R.id.invite_friend);
        my_bank_detail = view.findViewById(R.id.my_bank_detail);
        logout = view.findViewById(R.id.logout);
        ll_select_user_profile = view.findViewById(R.id.ll_select_user_profile);
        ll_select_user_profile.setOnClickListener(this);
        change_password = view.findViewById(R.id.change_password);
        my_connection.setOnClickListener(this);
        become_elite_author.setOnClickListener(this);
        change_password.setOnClickListener(this);
        my_address.setOnClickListener(this);
        profile.setOnClickListener(this);
        my_wish_list.setOnClickListener(this);
        my_order.setOnClickListener(this);
        my_earning.setOnClickListener(this);
        invite_friend.setOnClickListener(this);
        my_bank_detail.setOnClickListener(this);
        logout.setOnClickListener(this);
        create_your_profile.setOnClickListener(this);
        user_image.setOnClickListener(this);
        crown = view.findViewById(R.id.crown);
        updateUser();


        return view;
    }

    private void updateUser() {
        if(UserCO.getUserCOFromDB().is_premium.equals("1"))
        {
            crown.setVisibility(View.VISIBLE);
        }
        else
        {
            crown.setVisibility(View.GONE);
        }

        wallet_amount.setText(UserCO.getUserCOFromDB().wallet_amount);
        user_phone_number.setText(UserCO.getUserCOFromDB().mobile);
        user_name.setText(UserCO.getUserCOFromDB().username);

        Glide.with(getActivity())
                .load(UserCO.getUserCOFromDB().image)
                .apply(RequestOptions.circleCropTransform().placeholderOf(R.drawable.error_images).error(R.drawable.error_images))
                .dontAnimate()
                .into(user_image);
    }

    @Override
    public void onResume()
    {
        updateUser();
        super.onResume();
    }


    @Override
    protected void onViewClick(View view) {
        switch(view.getId()) {
            case R.id.profile:
                Intent iMySharedCatelog = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(iMySharedCatelog);
                break;

            case R.id.become_elite_author:
                Intent ielite = new Intent(getActivity(), EliteAuthorActivity.class);
                startActivity(ielite);
                break;

            case R.id.my_wish_list:
                Intent iMyWishList = new Intent(getActivity(), MyWishList.class);
                startActivity(iMyWishList);
                break;
            case R.id.my_order:
                Intent iMyOrderList = new Intent(getActivity(), MyOrderList.class);
                startActivity(iMyOrderList);
                break;
            case R.id.my_connection:
                Intent iconnection = new Intent(getActivity(), MyConnectionActivity.class);
                startActivity(iconnection);
                break;
            case R.id.my_earning:
                Intent iMyEarnings = new Intent(getActivity(), MyEarnings.class);
                startActivity(iMyEarnings);

                break;
            case R.id.invite_friend:
                createLink();
                break;
            case R.id.my_bank_detail:
                Intent i = new Intent(getActivity(), MyBankDetails.class);
                startActivity(i);
                break;
            case R.id.logout:
                UserCO userCO = new UserCO();
                userCO.deleteFromDB();
                Toast.makeText(getActivity(), "Logout", Toast.LENGTH_SHORT).show();
                Intent il = new Intent(getActivity(), LoginOriginal.class);
                il.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(il);
                getActivity().finish();
                break;

            case R.id.ll_select_user_profile:

                selectImage();
                break;

            case R.id.create_your_profile:
                Intent  ic = new Intent(getActivity(), CustomOrderCreation.class);
                startActivity(ic);
                break;


            case R.id.change_password:
                Intent ichange= new Intent(getActivity(), ChangePassword.class);
                startActivity(ichange);
                break;


            case R.id.my_address:
                Intent iad =new Intent(getActivity(), MyAddress.class);
                iad.putExtra("is_checkout","N");
                startActivity(iad);
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

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentImageSlider newFragment = FragmentImageSlider.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");

                break;
        }
    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {

    }

    private void selectImage()
    {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        return ContextCompat.checkSelfPermission(getActivity(),
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
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private File createImageFile() throws IOException {
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
                    File orignalFile = FileUtils.from(getActivity(), selectedImage);

                    uploadUsingOkHttp(orignalFile);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else
            if (requestCode == 2)
            {
                Uri selectedImage = data.getData();
                try {
                    File orignalFile = FileUtils.from(getActivity(), selectedImage);
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
        pDialog = new ProgressDialog(getActivity());
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
                        Headers.of("Content-Disposition", "form-data; name=\"user_image\"; filename=\"" + orignalFile.getName() + "\""),
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


        Request request = new Request.Builder()
                .tag("anyName")
                .addHeader("source", ISysCodes.REQUEST_SOURCE_ANDROID)
                // .url(ISysConfig.SERVER_HIT_URL)
                .url(ISysConfig.SERVER_IMAGE_UPLOAD_URL + "/adduser_image")
                .post(requestBody)
                .build();

        okhttp3.Call call = (new OkHttpClient()).newCall(request);

        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) throws IOException {

              getActivity().runOnUiThread(new Runnable() {
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

                                AppUtilities.writeToPref(IReqParams.LOGGED_IN_USER_PROFILE_IMAGE, uploadedDoc);

                                Glide.with(getActivity())
                                        .load(uploadedDoc)
                                        .apply(RequestOptions.circleCropTransform().placeholderOf(R.drawable.error_images).error(R.drawable.error_images))
                                        .dontAnimate()
                                        .into(user_image);
                            } else {
                                Toast.makeText(getActivity(), nessage, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), "Network error.", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    e.addSuppressed(ex);
                }

                //showUserMessage("Network error.");
            }
        });
    }



    private void createLink() {

        //creating dynamic link
        String uid = UserCO.getUserCOFromDB().id;

        Log.d("TAG", "EEEEE: " + uid);
        String link = "https://trevon.page.link/?invitedby=" + uid;
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(link))
                .setDomainUriPrefix("https://trevon.page.link")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.trevon")
                                .setMinimumVersion((int)3.0)
                                .build())
//                .setIosParameters(
//                        new DynamicLink.IosParameters.Builder("com.example.ios")
//                                .setAppStoreId("123456789")
//                                .setMinimumVersion("1.0.1")
//                                .build())
                .buildShortDynamicLink()
                .addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
                    @Override
                    public void onSuccess(ShortDynamicLink shortDynamicLink) {

                        mInvitationUrl = shortDynamicLink.getShortLink();

                        Log.d("TAG", "TTTTTTTTT ::: PATH " +  shortDynamicLink.getPreviewLink());

                        Log.d("TAG", "TTTTTTTTT " + mInvitationUrl.toString());
                        Log.d("TAG", "TTTTTTTTT " + shortDynamicLink.toString());
                        sendDynamicLink(mInvitationUrl);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "EEEEEE " + e.getMessage());
                    }
                });


    }

    //Sharing the dynamic Link
    private void sendDynamicLink(Uri mInvitationUrl) {

        String referrerName = UserCO.getUserCOFromDB().username;
        String subject = String.format("%s wants you to Download The Trevon!", referrerName);
        String invitationLink = mInvitationUrl.toString();
        String msg = "Download the Trevon and earn some money! Use my referrer link: "
                + invitationLink;
        String msgHtml = String.format("<p>Download the Trevon and earn some money! Use my "
                + "<a href=\"%s\">referrer link</a>!</p>", invitationLink);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
        sendIntent.putExtra(Intent.EXTRA_HTML_TEXT, msgHtml);
        sendIntent.setType("text/plain");

        if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(sendIntent);
        }
    }
}