package com.tws.trevon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tws.trevon.R;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.API;
import com.tws.trevon.constants.IReqParams;

import org.json.JSONObject;

public class AllInOnePage extends AbstractActivity {

    TextView text;
    String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_in_one_page);
        Toolbar toolbar = findViewById(R.id.all_inone_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        Intent intent = getIntent();
        String type = intent.getExtras().getString("type");
        getSupportActionBar().setTitle(type);
        text = findViewById(R.id.text);

                if(type.equals("Support"))
                {
                    key="support";
                }
                else if(type.equals("Term of Use"))
                {
                    key="term_of_use";
                }
                else if(type.equals("Privacy Policy"))
                {
                    key="privacy_policy";
                }
                else if(type.equals("About"))
                {
                    key="about";
                }


        CallApi callApi = new CallApi(API.setting);
        callApi.addReqParams("option_key", key);
        processCallApi(callApi);
    }

    @Override
    protected void onViewClick(View view) {

    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {
        if (API.setting.method.equals(callApi.networkActivity.method))
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

                    String id =  jsonObject1.getString("id");
                    String option_key =  jsonObject1.getString("option_key");
                    String option_value =  jsonObject1.getString("option_value");

                    text.setText(  Html.fromHtml(option_value), TextView.BufferType.SPANNABLE);

                }
                else {

                    Toast.makeText(this, nessage, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

}