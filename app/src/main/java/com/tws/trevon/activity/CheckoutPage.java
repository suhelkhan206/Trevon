package com.tws.trevon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tws.trevon.R;
import com.tws.trevon.adapter.CartItemAdapter;
import com.tws.trevon.co.CheckoutCO;
import com.tws.trevon.co.MyAddressCO;
import com.tws.trevon.co.ProductCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.IConstants;

public class CheckoutPage extends AbstractActivity {

    LinearLayout edit_address;
    TextView address_person_name,user_address;
    RecyclerView product_recycler_view;
    TextView gst_amount,price_items, total_mrp, cart_discount, delivery_charges,subtotal,save_amount;

    CheckoutCO checkoutCO;
    MyAddressCO myAddressCO;

    CartItemAdapter cartItemAdapter;
    RelativeLayout palce_order_button;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_page);
        Toolbar toolbar = findViewById(R.id.checkout_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Checkout");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        edit_address = findViewById(R.id.edit_address);
        address_person_name = findViewById(R.id.address_person_name);
        user_address = findViewById(R.id.user_address);
        product_recycler_view = findViewById(R.id.product_recycler_view);
        gst_amount = findViewById(R.id.gst_amount);
        delivery_charges = findViewById(R.id.delivery_charges);
        price_items = findViewById(R.id.price_items);
        save_amount = findViewById(R.id.save_amount);
        subtotal = findViewById(R.id.subtotal);
        total_mrp = findViewById(R.id.total_mrp);
        cart_discount= findViewById(R.id.cart_discount);

        palce_order_button = findViewById(R.id.palce_order_button);
        palce_order_button.setOnClickListener(this);
        edit_address.setOnClickListener(this);

        Intent intent = getIntent();
        String productCOString = intent.getExtras().getString("productCO");
        String addressCOString = intent.getExtras().getString("addressCO");
        checkoutCO = AppController.gson.fromJson(productCOString, CheckoutCO.class);
        myAddressCO = AppController.gson.fromJson(addressCOString, MyAddressCO.class);


        user_address.setText( MyAddressCO.getFullAddress(
                myAddressCO.house_no, myAddressCO.area, myAddressCO.city, myAddressCO.state,
                myAddressCO.country, myAddressCO.phone_no, myAddressCO.pincode));


        address_person_name.setText(myAddressCO.name);

        cartItemAdapter = new CartItemAdapter(CheckoutPage.this,checkoutCO.cartProductCOList,true);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(CheckoutPage.this);
        product_recycler_view.setLayoutManager(mLayoutManager2);
        product_recycler_view.setItemAnimator(new DefaultItemAnimator());
        product_recycler_view.setAdapter(cartItemAdapter);
        cartItemAdapter.notifyDataSetChanged();

        delivery_charges.setText(IConstants.RUPEE_ICON+"0");
        price_items.setText("Price ("+checkoutCO.cartProductCOList.size()+" items)");
        total_mrp.setText(IConstants.RUPEE_ICON+checkoutCO.totalMrp);
        cart_discount.setText(IConstants.RUPEE_ICON+checkoutCO.discount);
        subtotal.setText(IConstants.RUPEE_ICON+ checkoutCO.totalPrice);
        save_amount.setText("You will save "+checkoutCO.discount+" on this order");
        gst_amount.setText(IConstants.RUPEE_ICON+checkoutCO.gst);
    }

    @Override
    protected void onViewClick(View view)
    {
        switch(view.getId())
        {
            case R.id.palce_order_button:
                String myAddressCOString =  AppController.gson.toJson(myAddressCO);
                String productCOString =  AppController.gson.toJson(checkoutCO);
                Intent iadd = new Intent(CheckoutPage.this, CheckoutPages.class);
                iadd.putExtra("productCO",productCOString);
                iadd.putExtra("addressCO",myAddressCOString);
                startActivity(iadd);
                break;

            case R.id.edit_address:
                finish();
                break;


        }
    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {

    }
}