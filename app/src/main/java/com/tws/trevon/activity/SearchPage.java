package com.tws.trevon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tws.trevon.R;
import com.tws.trevon.adapter.SearchProductAdapter;
import com.tws.trevon.co.ProductCO;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.API;
import com.tws.trevon.constants.IConstants;
import com.tws.trevon.constants.IPreferences;
import com.tws.trevon.constants.IReqParams;

import org.json.JSONArray;
import org.json.JSONObject;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import java.util.List;

import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchPage extends AbstractActivity
{
    EditText editText_search_page;
    LinearLayout search_go;
    RecyclerView search_recycler;
    SearchProductAdapter searchProductAdapter;
    public List<ProductCO> packageCOList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        Toolbar toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Search Product");

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        editText_search_page = findViewById(R.id.editText_search_page);
        search_go = findViewById(R.id.search_go);
        search_recycler = findViewById(R.id.search_recycler);
        search_go.setOnClickListener(this);


        RecyclerView.LayoutManager featyredlayoutManager = new LinearLayoutManager(SearchPage.this, LinearLayoutManager.VERTICAL, false);
        search_recycler.setLayoutManager(featyredlayoutManager);
        searchProductAdapter = new SearchProductAdapter(SearchPage.this, packageCOList, 0);
        search_recycler.setAdapter(searchProductAdapter);
        searchProductAdapter.notifyDataSetChanged();

        searchProductAdapter.setRVItemClickListener(new SearchProductAdapter.RVItemClickListener() {
            @Override
            public void onFavouriteProductClick(View view, int position, int pos) {
                ProductCO packageCO =  packageCOList.get(position);
                Intent i = new Intent(SearchPage.this, SingleProductScreen.class);
                i.putExtra("productId",packageCO.id);
                startActivity(i);

            }
        });
    }



    @Override
    protected void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.search_go:
                if (editText_search_page.getText().toString().trim().length() == 0) {
                    editText_search_page.requestFocus();
                    editText_search_page.setError("Enter text");
                }
                else
                {
                    if (editText_search_page.getText().toString().length() > 2)
                    {
                        CallApi callApi = new CallApi(API.search_product);
                        callApi.addReqParams("str", editText_search_page.getText().toString().trim());
                        callApi.addReqParams("city_id", AppUtilities.readFromPref(IPreferences.CITY_ID));
                        processCallApi(callApi);
                    }
                    else
                    {
                        Toast.makeText(this, "Please enter at least 3 characters ", Toast.LENGTH_SHORT).show();
                    }



                }

                break;
        }
    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {
        if (API.search_product.method.equals(callApi.networkActivity.method)) {
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");

                if (status.equalsIgnoreCase("Success")) {
                    packageCOList.clear();
                    JSONArray onbjnew = action.getJSONArray("data");

                    for (int i = 0; i < onbjnew.length(); i++)
                    {
                        JSONObject jsonObject1 = onbjnew.getJSONObject(i);
                        ProductCO productCO = AppController.gson.fromJson(jsonObject1.toString(), ProductCO.class);
                        packageCOList.add(productCO);
                    }
                    searchProductAdapter.featuredFoodCOList = packageCOList;
                    searchProductAdapter.notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(SearchPage.this, nessage, Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

    }



}