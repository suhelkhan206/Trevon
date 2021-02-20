package com.tws.trevon.activity.sp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tws.trevon.R;
import com.tws.trevon.activity.AbstractActivity;
import com.tws.trevon.co.ImageCO;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.API;
import com.tws.trevon.constants.IReqParams;
import com.tws.trevon.fragment.FragmentImageSlider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity2 extends AbstractActivity {

    CircleImageView user_image;
    TextView   user_name,user_phone_number, wallet_amount;
    EditText first_name,mobile_number,email_id;
    LinearLayout submit_button;
    LinearLayout ll_select_user_profile;

    ImageView crown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile2);
        Toolbar toolbar = findViewById(R.id.edit_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit Profile");

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
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

        ll_select_user_profile = findViewById(R.id.ll_select_user_profile);
        ll_select_user_profile.setVisibility(View.GONE);
        user_image = findViewById(R.id.user_image);
        user_name = findViewById(R.id.user_name);
        user_phone_number = findViewById(R.id.user_phone_number);
        wallet_amount = findViewById(R.id.wallet_amount);

        first_name = findViewById(R.id.first_name);

        mobile_number = findViewById(R.id.mobile_number);
        email_id = findViewById(R.id.email_id);

        submit_button = findViewById(R.id.submit_button);
        submit_button.setOnClickListener(this);

        first_name.setText(UserCO.getUserCOFromDB().username);
        mobile_number.setText(UserCO.getUserCOFromDB().mobile);
        email_id.setText(UserCO.getUserCOFromDB().email);

        user_name.setText(UserCO.getUserCOFromDB().username);
        user_phone_number.setText(UserCO.getUserCOFromDB().mobile);
        wallet_amount.setText(UserCO.getUserCOFromDB().wallet_amount);

        Glide.with(EditProfileActivity2.this)
                .load(UserCO.getUserCOFromDB().image)
                .apply(RequestOptions.circleCropTransform().placeholderOf(R.drawable.error_images).error(R.drawable.error_images))
                .dontAnimate()
                .into(user_image);

        user_image.setOnClickListener(this);

    }

    @Override
    protected void onViewClick(View view)
    {
        switch(view.getId())
        {
            case R.id.submit_button:
                if(checkValidations())
                {

                    CallApi callApi = new CallApi(API.edit_profile);
                    callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
                    callApi.addReqParams("first_name", first_name.getText().toString().trim());
                    //callApi.addReqParams("last_name", last_name.getText().toString().trim());
                    callApi.addReqParams("mobile", mobile_number.getText().toString().trim());
                    callApi.addReqParams("email", email_id.getText().toString().trim());
                    processCallApi(callApi);
                }


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

    private Boolean checkValidations()
    {
        Boolean isValid = true;

        if(first_name.getText().toString().trim().length() == 0)
        {
            isValid = false;
            first_name.requestFocus();
            Toast.makeText(EditProfileActivity2.this, "Enter first name", Toast.LENGTH_SHORT).show();
            //loginId.setBackgroundResource(R.drawable.edit_text_border_red);
        }

        else if(mobile_number.getText().toString().trim().length() == 0)
        {
            isValid = false;
            mobile_number.requestFocus();
            Toast.makeText(EditProfileActivity2.this, "Enter mobile no.", Toast.LENGTH_SHORT).show();
            //loginId.setBackgroundResource(R.drawable.edit_text_border_red);
        }
        else if(!AppValidate.isMobileNumber(mobile_number.getText().toString().trim()))
        {
            isValid = false;
            mobile_number.requestFocus();
            Toast.makeText(EditProfileActivity2.this, "Enter correct Mobile no.", Toast.LENGTH_SHORT).show();
        }
        else if(email_id.getText().toString().trim().length() == 0)
        {
            isValid = false;
            email_id.requestFocus();
            Toast.makeText(EditProfileActivity2.this, "Enter email id", Toast.LENGTH_SHORT).show();
            //loginId.setBackgroundResource(R.drawable.edit_text_border_red);
        }
        else if(!AppValidate.isEmail(email_id.getText().toString().trim()))
        {
            isValid = false;
            email_id.requestFocus();
            Toast.makeText(EditProfileActivity2.this, "Enter correct email id", Toast.LENGTH_SHORT).show();
        }
        return isValid;
    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {
        if(API.edit_profile.method.equals(callApi.networkActivity.method))
        {
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");
                if (status.equalsIgnoreCase("Success"))
                {
                    JSONObject jsonObject1= action.getJSONObject("data");
                    AppUtilities.writeToPref(IReqParams.USER_NAME,  jsonObject1.getString("first_name"));
                    AppUtilities.writeToPref(IReqParams.LOGGED_IN_USER_MOBILE, jsonObject1.getString("mobile"));
                    AppUtilities.writeToPref(IReqParams.LOGGED_IN_USER_EMAIL, jsonObject1.getString("email"));

                    finish();
                } else {

                    Toast.makeText(this, nessage, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

}