package com.tws.trevon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tws.trevon.R;
import com.tws.trevon.adapter.NotificationAdapter;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.API;
import com.tws.trevon.constants.IConstants;
import com.tws.trevon.model.NotificationCO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Notification extends AbstractActivity {

    RecyclerView notification_recycler;
    NotificationAdapter notificationAdapter;
    List<NotificationCO> notificationCOList =new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = findViewById(R.id.notification_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Notification");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        notification_recycler = findViewById(R.id.notification_recycler);


        notificationAdapter = new NotificationAdapter(this,notificationCOList);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this);
        notification_recycler.setLayoutManager(mLayoutManager2);
        notification_recycler.setItemAnimator(new DefaultItemAnimator());
        notification_recycler.setAdapter(notificationAdapter);


        if(AppValidate.isEmpty(notificationCOList))
        {
            CallApi callApi = new CallApi(API.get_notifications);
            callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);//
            processCallApi(callApi);
        }
    }

    @Override
    protected void onViewClick(View view) {

    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {
        if(API.get_notifications.method.equals(callApi.networkActivity.method))
        {
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");

                if (status.equalsIgnoreCase("Success"))
                {
                    notificationCOList.clear();
                    JSONArray onbjnew = action.getJSONArray("data");

                    for(int i=0;i<onbjnew.length(); i++)
                    {
                        JSONObject jsonObject1= onbjnew.getJSONObject(i);
                        NotificationCO notificationCO = AppController.gson.fromJson(jsonObject1.toString(), NotificationCO.class);
                        notificationCOList.add(notificationCO);
                    }
                    notificationAdapter.notificationCOList = notificationCOList;
                    notificationAdapter.notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(this, nessage, Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.dashboard_option_menu, menu);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.dashboard_option_menu,menu);
        MenuItem  menuItem = menu.findItem(R.id.notification_home);
        menuItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId()==R.id.action_search)
        {
            Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
        }

        if (item.getItemId()==R.id.notification_home)
        {
            UserCO userCO =  UserCO.getUserCOFromDB();
            if(AppValidate.isNotEmpty(userCO.user_id))
            {
                Intent i =new Intent(Notification.this,Notification.class);
                startActivityForResult(i, IConstants.INTENT_CART_COUNT_CODE);// Activity is started with requestCode 2
            }
            else
            {
                Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
                Intent ilogin = new Intent(Notification.this,LoginOriginal.class);
                ilogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(ilogin);
                finish();
            }

        }
        return super.onOptionsItemSelected(item);
    }*/

}