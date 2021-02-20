package com.tws.trevon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.tws.trevon.R;
import com.tws.trevon.adapter.MyAddressAdapter;
import com.tws.trevon.co.CheckoutCO;
import com.tws.trevon.co.MyAddressCO;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.API;
import com.tws.trevon.constants.IConstants;
import com.tws.trevon.constants.IPreferences;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyAddress extends AbstractActivity
{
    View view;
    RecyclerView my_address_recyclerview;
    MyAddressAdapter myAddressAdapter;
    List<MyAddressCO> myAddressCOList =new ArrayList<>();
    LinearLayout add_new_address, proceed;
    CheckoutCO checkoutCO;
    MyAddressCO myAddressCO;
    String is_checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);

        Toolbar toolbar = findViewById(R.id.my_addr_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Shipping Address");
        myAddressCO = new MyAddressCO();
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        Intent intent = getIntent();
        if(intent.getExtras() != null)
        {
             is_checkout = intent.getExtras().getString("is_checkout");
            if(is_checkout.equals("Y"))
            {
                String productCOString = intent.getExtras().getString("checkoutCO");
                checkoutCO = AppController.gson.fromJson(productCOString, CheckoutCO.class);
            }

        }

        loadData();
    }

    private void loadData()
    {
        my_address_recyclerview = findViewById(R.id.my_address_recyclerview);
        add_new_address = findViewById(R.id.add_new_address);
        proceed = findViewById(R.id.proceed);
        add_new_address.setOnClickListener(this);
        proceed.setOnClickListener(this);
        myAddressAdapter = new MyAddressAdapter(MyAddress.this,myAddressCOList, true);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(MyAddress.this);
        my_address_recyclerview.setLayoutManager(mLayoutManager2);
        my_address_recyclerview.setItemAnimator(new DefaultItemAnimator());
        my_address_recyclerview.setAdapter(myAddressAdapter);

        if(is_checkout.equals("Y"))
        {
            proceed.setVisibility(View.VISIBLE);
        }
        else
        {
            proceed.setVisibility(View.GONE);
        }


        if(AppValidate.isEmpty(myAddressCOList))
        {
            CallApi callApi = new CallApi(API.my_address_list);
            callApi.addReqParams("user_id",UserCO.getUserCOFromDB().id);// UserCO.getUserCOFromDB().id
            callApi.addReqParams("city_id", AppUtilities.readFromPref(IPreferences.CITY_ID));
            processCallApi(callApi);
        }

        adapterClick();

    }

    private void adapterClick()
    {
        myAddressAdapter.setAddressClickListener(new MyAddressAdapter.AddressClickListner()
        {
            @Override
            public void onAddressCLick(View view, int position)
            {
                MyAddressCO myAddressCO = myAddressCOList.get(position);
                String myAddressString = AppController.gson.toJson(myAddressCO);

                Intent i = new Intent(MyAddress.this, AddNewAddress.class);
                i.putExtra("myAddressObject",myAddressString);

                startActivityForResult(i, IConstants.ACTIVITY_CHECKOUT_CODE);
            }

            @Override
            public void onAddressRootCLick(View view, int position)
            {
                 myAddressCO = myAddressCOList.get(position);
                 for(int i=0 ;i<myAddressCOList.size();i++)
                 {
                     myAddressCOList.get(i).is_default="0";
                 }
                myAddressCO.is_default ="1";
                myAddressAdapter.notifyDataSetChanged();
            }

            @Override
            public void ondeleteAddressCLick(View view, int position)
            {
                MyAddressCO myAddressCO1 = myAddressCOList.get(position);
                if(AppValidate.isNotEmpty(myAddressCO.id))
                {
                    if(myAddressCO.id.equals(myAddressCO1.id))
                    {
                        myAddressCO = new MyAddressCO();
                    }
                }

                CallApi callApi = new CallApi(API.delete_address);
                callApi.addReqParams("address_id", myAddressCO1.id);
                processCallApi(callApi);
                myAddressCOList.remove(position);
                myAddressAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case IConstants.ACTIVITY_CHECKOUT_CODE:
                if(data != null)
                {
                    String message=data.getStringExtra("MESSAGE");
                    MyAddressCO  myAddressCO = AppController.gson.fromJson(message, MyAddressCO.class);
                   // address_order.setText(myAddressCO.address_1);
                    String update = "0";
                    int pos = 0;
                    for(int i=0; i<myAddressCOList.size(); i++)
                    {
                        if(myAddressCOList.get(i).id.equals(myAddressCO.id))
                        {
                            pos = i;
                            update = "1";
                            break;
                        }
                        else
                        {
                            update = "0";
                        }
                    }
                    if(update.equals("1"))
                    {
                        myAddressCOList.set(pos, myAddressCO);
                       // myAddressCOList.add(pos, myAddressCO);
                    }
                    else
                    {
                        myAddressCOList.add(myAddressCO);
                    }

                    myAddressAdapter.notifyDataSetChanged();
                }
                break;
        }
    }


    @Override
    protected void onViewClick(View view)
    {
        switch(view.getId())
        {
            case R.id.add_new_address:
                Intent i = new Intent(MyAddress.this, AddNewAddress.class);
                startActivityForResult(i, IConstants.ACTIVITY_CHECKOUT_CODE);
                break;

            case R.id.proceed:
                if(AppValidate.isNotEmpty(myAddressCO.id))
                {
                    String myAddressCOString =  AppController.gson.toJson(myAddressCO);
                    String productCOString =  AppController.gson.toJson(checkoutCO);
                    Intent iadd = new Intent(MyAddress.this, CheckoutPage.class);
                    iadd.putExtra("productCO",productCOString);
                    iadd.putExtra("addressCO",myAddressCOString);
                    startActivity(iadd);
                }
                else
                {
                    Toast.makeText(this, "Please select address", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi)
    {
        if(API.my_address_list.method.equals(callApi.networkActivity.method))
        {
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");

                if (status.equalsIgnoreCase("Success"))
                {
                    myAddressCOList.clear();
                    JSONArray onbjnew = action.getJSONArray("data");

                    for(int i=0;i<onbjnew.length(); i++)
                    {
                        JSONObject jsonObject1= onbjnew.getJSONObject(i);
                        MyAddressCO myAddressCO = AppController.gson.fromJson(jsonObject1.toString(), MyAddressCO.class);
                        myAddressCOList.add(myAddressCO);
                    }
                    myAddressAdapter.notifyDataSetChanged();
                    myAddressAdapter.myAddressCOList = myAddressCOList;
                }
                else
                {
                    Toast.makeText(MyAddress.this, nessage, Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            /* UserCO userCO = AppController.gson.fromJson(userCOJsonString, UserCO.class);
            userCO.saveInDB();

            Toast.makeText(this, UserCO.getUserCOFromDB()+"", Toast.LENGTH_SHORT).show();
            UserCO.getUserCOFromDB();*/
        }
    }

}
