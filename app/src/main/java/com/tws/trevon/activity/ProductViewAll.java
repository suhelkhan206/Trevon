package com.tws.trevon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tws.trevon.R;
import com.tws.trevon.adapter.FeatureProductAdapter;
import com.tws.trevon.adapter.FeatureProductAdapterHorizontal;
import com.tws.trevon.co.BrandsCO;
import com.tws.trevon.co.ColourCO;
import com.tws.trevon.co.FilterCO;
import com.tws.trevon.co.ProductCO;
import com.tws.trevon.co.SizeCO;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.common.ConstantVariable;
import com.tws.trevon.common.ItemOffsetDecoration;
import com.tws.trevon.constants.API;
import com.tws.trevon.constants.IConstants;
import com.tws.trevon.constants.IReqParams;
import com.tws.trevon.fragment.BottomFilter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductViewAll extends AbstractActivity
{
    RecyclerView product_view_all_recycler;
    List<ProductCO> productCOList = new ArrayList<>();
    FeatureProductAdapterHorizontal featureProductAdapter;
    LinearLayout sort_product,filter_product;
    BottomFilter postShare;
    String category_id,type;

    List<BrandsCO>  brandCOList = new ArrayList<>();
    List<SizeCO>  sizeCOList = new ArrayList<>();
    List<ColourCO>  colourCOList = new ArrayList<>();
    List<FilterCO> filterCOList = new ArrayList<>();
    LinearLayout no_wishlist_found;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view_all);
        Toolbar toolbar = findViewById(R.id.produc_view_all_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(ConstantVariable.CATEGORY_TITLE);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        Intent intent = getIntent();
        if(intent.getExtras() != null)
        {
            category_id = intent.getExtras().getString("category_id");
            type = intent.getExtras().getString("type");

        }
        no_wishlist_found = findViewById(R.id.no_wishlist_found);
        product_view_all_recycler = findViewById(R.id.product_view_all_recycler);
        sort_product = findViewById(R.id.sort_product);
        filter_product = findViewById(R.id.filter_product);
        sort_product.setOnClickListener(this);
        filter_product.setOnClickListener(this);


        product_view_all_recycler.setHasFixedSize(true);
        product_view_all_recycler.setLayoutManager(new GridLayoutManager(this, 2));


        CallApi callApi = new CallApi(API.new_product_list);
        callApi.addReqParams("user_id",UserCO.getUserCOFromDB().id);
        callApi.addReqParams("category_id",category_id);
        callApi.addReqParams("type",type);
        processCallApi(callApi);




/*
        CallApi callApiallFilters = new CallApi(API.allFilters);
        callApiallFilters.addReqParams("subminiCat_id",subminiCat_id);
        //callApi.addReqParams("sort","newest");
        processCallApi(callApiallFilters);*/



    }

    @Override
    protected void onViewClick(View view)
    {
        switch (view.getId())
        {
            case R.id.sort_product:

                break;

            case R.id.filter_product:

                break;
        }
    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {
        if(API.new_product_list.method.equals(callApi.networkActivity.method))
        { // new
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");
                if (status.equalsIgnoreCase("Success")) {
                    productCOList.clear();
                    JSONArray onbjnew = action.getJSONArray("data");
                    for (int i = 0; i < onbjnew.length(); i++) {
                        JSONObject jsonObject1 = onbjnew.getJSONObject(i);
                        ProductCO productCO = AppController.gson.fromJson(jsonObject1.toString(), ProductCO.class);
                        productCOList.add(productCO);
                    }
                    if (productCOList.size() > 0) {
                        no_wishlist_found.setVisibility(View.GONE);
                    } else
                    {
                        no_wishlist_found.setVisibility(View.VISIBLE);
                    }

                    featureProductAdapter = new FeatureProductAdapterHorizontal(this, productCOList);
                    product_view_all_recycler.setAdapter(featureProductAdapter);
                    featureProductAdapter.notifyDataSetChanged();
                    adapterClick();
                } else {
                    no_wishlist_found.setVisibility(View.VISIBLE);
                    Toast.makeText(this, nessage, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                no_wishlist_found.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
        }
        if(API.allFilters.method.equals(callApi.networkActivity.method))
        {
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");
                if (status.equalsIgnoreCase("Success"))
                {
                    productCOList.clear();
                    JSONObject onbjnew = action.getJSONObject("data");
                    JSONArray brands = onbjnew.getJSONArray("brands");
                    JSONArray size = onbjnew.getJSONArray("size");
                    JSONArray colour = onbjnew.getJSONArray("colour");
                    for(int i = 0 ; i<brands.length();i++)
                    {
                        JSONObject jsonObject1 = brands.getJSONObject(i);
                        BrandsCO brandsCO = AppController.gson.fromJson(jsonObject1.toString(), BrandsCO.class);
                        brandCOList.add(brandsCO);
                    }
                    FilterCO filterCO=new FilterCO();
                    filterCO.filter_title = "Brands";
                    filterCO.FilterValueList.addAll(brandCOList);
                    filterCOList.add(filterCO);

                    for(int i = 0 ; i<size.length();i++)
                    {
                        JSONObject jsonObject1 = size.getJSONObject(i);
                        SizeCO sizeCO = AppController.gson.fromJson(jsonObject1.toString(), SizeCO.class);
                        sizeCOList.add(sizeCO);
                    }

                    FilterCO filterCOSize=new FilterCO();
                    filterCOSize.filter_title = "Size";
                    filterCOSize.FilterValueList.addAll(brandCOList);
                    filterCOList.add(filterCOSize);

                    for(int i = 0 ; i<colour.length();i++)
                    {
                        JSONObject jsonObject1 = colour.getJSONObject(i);
                        ColourCO colourCO = AppController.gson.fromJson(jsonObject1.toString(), ColourCO.class);
                        colourCOList.add(colourCO);
                    }

                    FilterCO filterCOColour=new FilterCO();
                    filterCOColour.filter_title = "Colour";
                    filterCOColour.FilterValueList.addAll(brandCOList);
                    filterCOList.add(filterCOColour);

                } else {
                    Toast.makeText(this, nessage, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    TextView textCartItemCount;
    int mCartItemCount;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_option_menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        MenuItem  menuItem2 = menu.findItem(R.id.notification_home);
        menuItem2.setVisible(false);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        if(!AppValidate.isEmpty(AppUtilities.readFromPref(IReqParams.USER_CART_COUNT)))
        {
            mCartItemCount = Integer.parseInt(AppUtilities.readFromPref(IReqParams.USER_CART_COUNT));
            setupBadge();
        }

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        if (item.getItemId()==R.id.action_search)
        {
            Intent i= new Intent(ProductViewAll.this, SearchPage.class);
            startActivity(i);
        }

        else if (item.getItemId()==R.id.notification_home)
        {
            UserCO userCO =  UserCO.getUserCOFromDB();
            if(AppValidate.isNotEmpty(userCO.id))
            {
                Intent i =new Intent(ProductViewAll.this,Notification.class);
                startActivityForResult(i, IConstants.INTENT_CART_COUNT_CODE);// Activity is started with requestCode 2
            }
            else
            {
                Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
                Intent ilogin = new Intent(ProductViewAll.this,LoginOriginal.class);
                ilogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(ilogin);
                finish();
            }

        }
        else if (item.getItemId()==R.id.action_cart)
        {
            UserCO userCO =  UserCO.getUserCOFromDB();
            if(AppValidate.isNotEmpty(userCO.id))
            {
                Intent i =new Intent(ProductViewAll.this,CartScreen.class);
                startActivityForResult(i, IConstants.INTENT_CART_COUNT_CODE);// Activity is started with requestCode 2
            }
            else
            {
                Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
                Intent ilogin = new Intent(ProductViewAll.this,LoginOriginal.class);
                ilogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(ilogin);
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupBadge()
    {
        if (textCartItemCount != null)
        {
            if (mCartItemCount == 0)
            {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void adapterClick()
    {
        featureProductAdapter.setEnrolledClickListener(new FeatureProductAdapter.EnrolledClickListner() {
            @Override
            public void onViewClick(View view, int positionParent)
            {


                if(productCOList.get(positionParent).is_wished.equals("1"))
                {
                    productCOList.get(positionParent).is_wished = "0";
                    CallApi callApi = new CallApi(API.add_to_wishList);
                    callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
                    callApi.addReqParams("product_id", productCOList.get(positionParent).id);
                    callApi.processInBackground= true;
                    processCallApi(callApi);
                }
                else
                {
                    productCOList.get(positionParent).is_wished = "1";
                    CallApi callApi = new CallApi(API.remove_wishList);
                    callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
                    callApi.addReqParams("product_id", productCOList.get(positionParent).id);
                    callApi.processInBackground= true;
                    processCallApi(callApi);
                }

                featureProductAdapter.notifyDataSetChanged();

            }
        });
    }
}