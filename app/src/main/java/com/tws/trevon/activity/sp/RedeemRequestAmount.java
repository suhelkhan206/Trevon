package com.tws.trevon.activity.sp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import de.hdodenhof.circleimageview.CircleImageView;

import android.graphics.Color;
import android.os.Bundle;
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
import com.tws.trevon.R;
import com.tws.trevon.activity.AbstractActivity;
import com.tws.trevon.co.ImageCO;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.API;
import com.tws.trevon.fragment.FragmentImageSlider;

import java.util.ArrayList;
import java.util.List;

public class RedeemRequestAmount extends AbstractActivity implements AdapterView.OnItemSelectedListener {

    CircleImageView user_image;
    TextView user_name,user_phone_number, wallet_amount;
    ImageView crown;
    ArrayList<String> doc_type_list = new ArrayList<>();
    String redeem_type;
     LinearLayout redeem_amount_button;
     EditText amount;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_request_amount);
        Toolbar toolbar = findViewById(R.id.redeem_request_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Redeem Request");

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        user_image =findViewById(R.id.user_image);
        user_name = findViewById(R.id.user_name);
        user_phone_number = findViewById(R.id.user_phone_number);
        wallet_amount = findViewById(R.id.wallet_amount);
        user_image.setOnClickListener(this);
        crown = findViewById(R.id.crown);
        redeem_amount_button = findViewById(R.id.redeem_amount_button);
        amount = findViewById(R.id.amount);
        redeem_amount_button.setOnClickListener(this);
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

        Glide.with(RedeemRequestAmount.this)
                .load(UserCO.getUserCOFromDB().image)
                .apply(RequestOptions.circleCropTransform().placeholderOf(R.drawable.error_images).error(R.drawable.error_images))
                .dontAnimate()
                .into(user_image);

        //doc_type_list.add("Bank Account");
        doc_type_list.add("Google Pay");
        doc_type_list.add("Phonepe");
        doc_type_list.add("Paytm");
        doc_type_list.add("Amazon Pay");

        populateSpinner(doc_type_list);
    }

    @Override
    protected void onViewClick(View view)
    {
        switch(view.getId())
        {
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

            case R.id.redeem_amount_button:
                 if(chekAddrValidation())
                 {
                     CallApi callApi = new CallApi(API.redeem_request);
                     callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
                     callApi.addReqParams("request_method", redeem_type);
                     callApi.addReqParams("request_amount", amount.getText().toString().trim());
                     processCallApi(callApi);
                 }
                break;

        }
    }

    public boolean chekAddrValidation()
    {
        Boolean isValid = true;

        if (amount.getText().toString().trim().length() == 0) {
            isValid = false;
            amount.requestFocus();
            Toast.makeText(RedeemRequestAmount.this, "Enter Amount", Toast.LENGTH_SHORT).show();
        }
        else if (AppValidate.isEmpty(redeem_type)) {
            isValid = false;
            Toast.makeText(RedeemRequestAmount.this, "Please Select Redeem Type", Toast.LENGTH_SHORT).show();
        }


        return isValid;
    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {

        if(API.redeem_request.method.equals(callApi.networkActivity.method))
        {
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void populateSpinner(ArrayList<String> tenureCOList)
    {
        // Get reference of widgets from XML layout
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);

        final List<String> plantsList = new ArrayList<>();
        plantsList.add("Select Redeem Type");
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
            redeem_type = (String) parent.getItemAtPosition(position);

          /*  if(redeem_type.equals("Bank Account"))
            {
                redeem_type = "";
            }*/
            if(redeem_type.equals("Google Pay"))
            {
                redeem_type = "google_pay";
            }
            else if(redeem_type.equals("Phonepe"))
            {
                redeem_type = "phonepe";
            }
            else if(redeem_type.equals("Paytm"))
            {
                redeem_type = "paytm";
            }
            else if(redeem_type.equals("Amazon Pay"))
            {
                redeem_type = "amazon_pay";
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        // TODO Auto-generated method stub
    }

}