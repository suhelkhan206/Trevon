package com.tws.trevon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tws.trevon.R;
import com.tws.trevon.co.MyAddressCO;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.API;
import com.tws.trevon.constants.IConstants;
import com.tws.trevon.constants.IPreferences;

import org.json.JSONObject;


public class AddNewAddress extends AbstractActivity
{
    EditText customername, house_number,street_area, city, country, state, pin;
    TextView t1;
    LinearLayout add_new_address;
    MyAddressCO myAddressCO;
    String is_Edit,position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);

        Toolbar toolbar = findViewById(R.id.address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add New Address");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        customername=(EditText)findViewById(R.id.customer_name);
        house_number=(EditText)findViewById(R.id.house_number);
        street_area=(EditText)findViewById(R.id.street_area);
        city=(EditText)findViewById(R.id.city);
        country=(EditText)findViewById(R.id.country);
        state=(EditText)findViewById(R.id.state);
        pin=(EditText)findViewById(R.id.pin);
        t1=findViewById(R.id.save_button);
        add_new_address = findViewById(R.id.add_new_address);
        add_new_address.setOnClickListener(this);

        Intent intent = getIntent();
        if(intent.getExtras() != null)
        {
            String myAddressCOString = intent.getStringExtra("myAddressObject");
            myAddressCO = AppController.gson.fromJson(myAddressCOString,MyAddressCO.class);

            //Toast.makeText(this, ""+myAddressCO, Toast.LENGTH_SHORT).show();
            customername.setText(myAddressCO.name);
            house_number.setText(myAddressCO.house_no);
            street_area.setText(myAddressCO.area);
            city.setText(myAddressCO.city);
            country.setText(myAddressCO.country);
            state.setText(myAddressCO.state);
            pin.setText(myAddressCO.pincode);

            /*if(myAddressCO.is_default.equalsIgnoreCase("1"))
            {
                addr_check_box_default.setChecked(true);
            }
            else
            {
                addr_check_box_default.setChecked(false);
            }*/
        }

    }

    public boolean chekAddrValidation()
    {
        Boolean isValid = true;

        if (customername.getText().toString().trim().length() == 0) {
            isValid = false;
            customername.requestFocus();
            Toast.makeText(AddNewAddress.this, "Enter First Name", Toast.LENGTH_SHORT).show();
        }


        else if (house_number.getText().toString().trim().length() == 0) {
            isValid = false;
            house_number.requestFocus();
            Toast.makeText(AddNewAddress.this, "Enter House number", Toast.LENGTH_SHORT).show();
        }

        else if (street_area.getText().toString().trim().length() == 0) {

            isValid = false;
            street_area.requestFocus();
            Toast.makeText(AddNewAddress.this, "Enter Street / Area", Toast.LENGTH_SHORT).show();
        }
        else if (city.getText().toString().trim().length() == 0) {

            isValid = false;
            city.requestFocus();
            Toast.makeText(AddNewAddress.this, "Enter city name", Toast.LENGTH_SHORT).show();
        }
        else if (country.getText().toString().trim().length() == 0) {

            isValid = false;
            country.requestFocus();
            Toast.makeText(AddNewAddress.this, "Enter Country", Toast.LENGTH_SHORT).show();
        }
        else if (state.getText().toString().trim().length() == 0) {

            isValid = false;
            state.requestFocus();
            Toast.makeText(AddNewAddress.this, "Enter state", Toast.LENGTH_SHORT).show();
        }
        else if (pin.getText().toString().trim().length() == 0) {

            isValid = false;
            pin.requestFocus();
            Toast.makeText(AddNewAddress.this, "Enter Pincode", Toast.LENGTH_SHORT).show();
        }




        return isValid;
    }

    @Override
    protected void onViewClick(View view) {

        switch(view.getId())
        {
            case R.id.add_new_address:
                if (chekAddrValidation())
                {
                    CallApi callApi = new CallApi(API.add_new_address);

                    if(myAddressCO != null)
                    {
                        callApi.addReqParams("id", myAddressCO.id);
                    }
                    callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
                    callApi.addReqParams("firstname", customername.getText().toString().trim());
                    callApi.addReqParams("house_no", house_number.getText().toString().trim());
                    //callApi.addReqParams("phone_no", mobile_number.getText().toString().trim());
                    callApi.addReqParams("area", street_area.getText().toString().trim());
                    // callApi.addReqParams("last_name", last_name.getText().toString().trim());
                    callApi.addReqParams("country", country.getText().toString().trim());
                    callApi.addReqParams("state", state.getText().toString().trim());
                    callApi.addReqParams("city", city.getText().toString().trim());
                    callApi.addReqParams("pincode", pin.getText().toString().trim());
                    processCallApi(callApi);
                }
                break;
        }
    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {
        if (API.add_new_address.method.equals(callApi.networkActivity.method))
        {
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");

                if (status.equalsIgnoreCase("Success"))
                {
                    JSONObject onbjnew = action.getJSONObject("data");

                    myAddressCO = AppController.gson.fromJson(onbjnew.toString(), MyAddressCO.class);
                    String address = AppController.gson.toJson(myAddressCO);

                    Intent intent=new Intent();
                    intent.putExtra("MESSAGE",address);
                    setResult(IConstants.ACTIVITY_CHECKOUT_CODE,intent);
                    finish();//finishing activity

                }
                else
                {
                    Toast.makeText(this, nessage, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }



}