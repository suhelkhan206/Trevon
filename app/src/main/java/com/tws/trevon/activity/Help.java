package com.tws.trevon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tws.trevon.R;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.CallApi;

public class Help extends AbstractActivity {

    CircleImageView user_image;
    TextView user_name,user_phone_number, wallet_amount;
    ImageView crown;

    LinearLayout gmail_click;
    LinearLayout open_call_dailer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Toolbar toolbar = findViewById(R.id.edit_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Help");

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        user_image = findViewById(R.id.user_image);
        user_name = findViewById(R.id.user_name);
        user_phone_number = findViewById(R.id.user_phone_number);
        wallet_amount = findViewById(R.id.wallet_amount);

        crown = findViewById(R.id.crown);
        if(UserCO.getUserCOFromDB().is_premium.equals("1"))
        {
            crown.setVisibility(View.VISIBLE);
        }
        else
        {
            crown.setVisibility(View.GONE);
        }

        user_name.setText(UserCO.getUserCOFromDB().username);
        user_phone_number.setText(UserCO.getUserCOFromDB().mobile);
        wallet_amount.setText(UserCO.getUserCOFromDB().wallet_amount);

        Glide.with(Help.this)
                .load(UserCO.getUserCOFromDB().image)
                .apply(RequestOptions.circleCropTransform().placeholderOf(R.drawable.error_images).error(R.drawable.error_images))
                .dontAnimate()
                .into(user_image);

        gmail_click = findViewById(R.id.gmail_click);
        open_call_dailer =findViewById(R.id.open_call_dailer);
        gmail_click.setOnClickListener(this);
        open_call_dailer.setOnClickListener(this);

    }

    @Override
    protected void onViewClick(View view) {
        switch(view.getId()) {
            case R.id.gmail_click:
                openGmail();
                break;
            case R.id.open_call_dailer:
                openCallDaileer();
                break;
        }
    }

       public void openGmail()
    {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"help@trevon.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "App feedback");
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Help.this, "There are no email client installed on your device.", Toast.LENGTH_SHORT).show();
        }
    }

    public void openCallDaileer()
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:9517536542"));
        startActivity(intent);
    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {

    }
}