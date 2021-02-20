package com.tws.trevon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tws.trevon.R;
import com.tws.trevon.co.MyAddressCO;
import com.tws.trevon.co.ProductCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.IConstants;


public class OrderSummary extends AbstractActivity {
    TextView total_sale_price, product_mrp, your_sale_price, margine, sale_price, coupon_discount,
            total_payble_amount, shipping_charges, supplier, product_quantity, quantity_sale_p, total_margin;

    ImageView product_image;
    TextView product_title, product_sale_price, product_original_price,
            product_size, quantity, address_holder_name,address;

    LinearLayout place_order;

    ProductCO productCO;
    MyAddressCO myAddressCO;
    ImageView edit_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        Toolbar toolbar = findViewById(R.id.order_summary);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Order Summary");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        edit_address = findViewById(R.id.edit_address);
        product_mrp=findViewById(R.id.product_mrp);
        your_sale_price=findViewById(R.id.your_sale_price);
        margine=findViewById(R.id.margine);
        sale_price=findViewById(R.id.sale_price);
        coupon_discount=findViewById(R.id.coupon_discount);
        shipping_charges=findViewById(R.id.shipping_charges);
        total_payble_amount=findViewById(R.id.total_payble_amount);
        supplier=findViewById(R.id.supplier);
        total_sale_price = findViewById(R.id.total_sale_price);
        product_image = findViewById(R.id.product_image);
        product_title = findViewById(R.id.product_title);
        product_sale_price= findViewById(R.id.product_sale_price);
        product_original_price = findViewById(R.id.product_original_price);
        product_size = findViewById(R.id.product_size);
        quantity = findViewById(R.id.quantity);
        quantity_sale_p = findViewById(R.id.quantity_sale_p);

        address_holder_name = findViewById(R.id.address_holder_name);
        address = findViewById(R.id.address);
        product_quantity = findViewById(R.id.product_quantity);
        total_margin = findViewById(R.id.total_margin);
        place_order=findViewById(R.id.place_order);
        place_order.setOnClickListener(this);
        edit_address.setOnClickListener(this);
              Intent intent = getIntent();
        String productCOString = intent.getExtras().getString("productCO");
        String addressCOString = intent.getExtras().getString("addressCO");
        productCO = AppController.gson.fromJson(productCOString, ProductCO.class);
        myAddressCO = AppController.gson.fromJson(addressCOString, MyAddressCO.class);

        setData(productCO, myAddressCO);

    }

    public void setData(ProductCO productCO, MyAddressCO myAddressCO)
    {
        //float pro_price = Float.parseFloat(productCO.product_price);
       // float inpu_price = Float.parseFloat(productCO.finalCustomerPrice);
        //float margin_earns = (inpu_price-pro_price);

        product_mrp.setText(IConstants.RUPEE_ICON+productCO.regular_price);
       // your_sale_price.setText(IConstants.RUPEE_ICON+productCO.finalCustomerPrice);

       // margine.setText(IConstants.RUPEE_ICON+margin_earns);
        product_quantity.setText(productCO.quantity);

        int qty = Integer.parseInt(productCO.quantity);
       // float totalmargin = margin_earns* qty;
      //  total_margin.setText(IConstants.RUPEE_ICON+totalmargin);
        sale_price.setText(IConstants.RUPEE_ICON+productCO.sell_price);
      //  float totalsale_price = pro_price* qty;
      //  total_sale_price.setText(IConstants.RUPEE_ICON+totalsale_price);
        quantity_sale_p.setText(productCO.quantity);
        coupon_discount.setText("-"+IConstants.RUPEE_ICON+productCO.coupon_amount);
       // shipping_charges.setText("+"+IConstants.RUPEE_ICON+productCO.shipping_charge);
        float productFinalPrize = Float.parseFloat(productCO.productFinalPrize);
        float finalPrize = productFinalPrize*qty;
        total_payble_amount.setText(IConstants.RUPEE_ICON+finalPrize);
        productCO.productFinalPrize = ""+finalPrize;
        //supplier.setText(productCO.merchant_name);

        Glide.with(OrderSummary.this)
                .load(productCO.image)
                .dontAnimate()
                .into(product_image);

        product_title.setText(productCO.title);
        product_sale_price.setText("Price "+IConstants.RUPEE_ICON+productCO.sell_price);
        product_original_price.setText("MRP "+IConstants.RUPEE_ICON+productCO.regular_price);
        product_size.setText("Size: "+productCO.selectedSize);
        quantity.setText("Qty: "+productCO.quantity);

        address_holder_name.setText(myAddressCO.name);
        address.setText( MyAddressCO.getFullAddress(
                myAddressCO.house_no, myAddressCO.area, myAddressCO.city, myAddressCO.state,
                myAddressCO.country, myAddressCO.phone_no, myAddressCO.pincode));

    }


    @Override
    protected void onViewClick(View view)
    {
        switch(view.getId())
        {
            case R.id.place_order:
                String myAddressCOString =  AppController.gson.toJson(myAddressCO);
                String productCOString =  AppController.gson.toJson(productCO);
                Intent iadd = new Intent(OrderSummary.this, CheckoutPages.class);
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