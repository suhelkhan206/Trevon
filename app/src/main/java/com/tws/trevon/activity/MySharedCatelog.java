package com.tws.trevon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tws.trevon.R;
import com.tws.trevon.adapter.FeatureProductAdapter;
import com.tws.trevon.adapter.FeatureProductAdapterHorizontal;
import com.tws.trevon.co.ProductCO;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.common.ItemOffsetDecoration;
import com.tws.trevon.constants.API;
import com.tws.trevon.constants.IConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MySharedCatelog extends AbstractActivity {

    RecyclerView my_shared_catelog_recycler_view;
    List<ProductCO> productCOList = new ArrayList<>();
    FeatureProductAdapterHorizontal featureProductAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shared_catelog);

        Toolbar toolbar = findViewById(R.id.sahred_catalog_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Shared Catalogs");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        my_shared_catelog_recycler_view = findViewById(R.id.my_shared_catelog_recycler_view);

        my_shared_catelog_recycler_view.setHasFixedSize(true);
        my_shared_catelog_recycler_view.setLayoutManager(new GridLayoutManager(this, 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        my_shared_catelog_recycler_view.addItemDecoration(itemDecoration);

        CallApi callApi = new CallApi(API.userSharedCatalog);
        callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
        processCallApi(callApi);
    }
    @Override
    protected void onViewClick(View view) {

    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {
        if(API.userSharedCatalog.method.equals(callApi.networkActivity.method))
        { // new
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");
                if (status.equalsIgnoreCase("Success"))
                {
                    productCOList.clear();
                    JSONArray onbjnew = action.getJSONArray("data");
                    for(int i = 0 ; i<onbjnew.length();i++)
                    {
                        JSONObject jsonObject1 = onbjnew.getJSONObject(i);
                        ProductCO productCO = AppController.gson.fromJson(jsonObject1.toString(), ProductCO.class);
                        productCOList.add(productCO);
                    }

                    featureProductAdapter = new FeatureProductAdapterHorizontal(this, productCOList);
                    my_shared_catelog_recycler_view.setAdapter(featureProductAdapter);
                    featureProductAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(this, nessage, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.dashboard_option_menu, menu);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.dashboard_option_menu,menu);
        MenuItem  menuItem2 = menu.findItem(R.id.notification_home);
        menuItem2.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId()==R.id.action_search)
        {
            Intent i= new Intent(MySharedCatelog.this, SearchPage.class);
            startActivity(i);
        }

        if (item.getItemId()==R.id.notification_home)
        {
            UserCO userCO =  UserCO.getUserCOFromDB();
            if(AppValidate.isNotEmpty(userCO.id))
            {
                Intent i =new Intent(MySharedCatelog.this,Notification.class);
                startActivityForResult(i, IConstants.INTENT_CART_COUNT_CODE);// Activity is started with requestCode 2
            }
            else
            {
                Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
                Intent ilogin = new Intent(MySharedCatelog.this,LoginOriginal.class);
                ilogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(ilogin);
                finish();
            }

        }
        return super.onOptionsItemSelected(item);
    }
}