package com.tws.trevon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tws.trevon.R;
import com.tws.trevon.co.ProductCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.API;
import com.tws.trevon.constants.IConstants;

import org.json.JSONObject;

import androidx.appcompat.widget.Toolbar;

public class Order extends AbstractActivity {
    TextView margine_your_earn,product_charges, shiping_charges,
            order_total_final, supplier, product_quantity, total_product_charges;
    ImageView product_image;
    TextView product_title, product_sale_price, product_original_price,
            product_size, quantity;

    EditText cash_to_colloect_customer;
    LinearLayout order_process;
    ProductCO productCO;
    TextView ordertotal;
    EditText enter_coupon_code;
    LinearLayout apply_coupon_code;
    TextView  coupon_discount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = findViewById(R.id.order_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        cash_to_colloect_customer = findViewById(R.id.cash_to_colloect_customer);
        margine_your_earn = findViewById(R.id.margine_your_earn);
        product_charges = findViewById(R.id.product_charges);
        shiping_charges = findViewById(R.id.shiping_charges);
        order_total_final = findViewById(R.id.order_total_final);
        supplier  = findViewById(R.id.supplier);
        product_image  = findViewById(R.id.product_image);
        product_title = findViewById(R.id.product_title);
        product_sale_price = findViewById(R.id.product_sale_price);
        product_original_price = findViewById(R.id.product_original_price);
        product_size = findViewById(R.id.product_size);
        quantity = findViewById(R.id.quantity);
        order_process = findViewById(R.id.order_process);
        product_quantity = findViewById(R.id.product_quantity);
        total_product_charges = findViewById(R.id.total_product_charges);
        order_process.setOnClickListener(this);
        ordertotal = findViewById(R.id.ordertotal);
        enter_coupon_code = findViewById(R.id.enter_coupon_code);
        apply_coupon_code = findViewById(R.id.apply_coupon_code);
        coupon_discount = findViewById(R.id.coupon_discount);
        apply_coupon_code.setOnClickListener(this);
        Intent intent = getIntent();
        String productCOString = intent.getExtras().getString("productCO");
        productCO = AppController.gson.fromJson(productCOString, ProductCO.class);

        getSupportActionBar().setTitle(productCO.title);
        setData(productCO);
        cash_to_colloect_customer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length() != 0)
                {
                    float pro_price = Float.parseFloat(productCO.sell_price);
                    float inpu_price = Float.parseFloat(cash_to_colloect_customer.getText().toString().trim());
                    float epsilon = Float.MIN_NORMAL;
                   // if(Float.compare(pro_price, inpu_price) < 0)
                    if(pro_price >= inpu_price)
                    {
                        margine_your_earn.setText(IConstants.RUPEE_ICON+"0");
                        order_total_final.setText(IConstants.RUPEE_ICON+"0");
                        cash_to_colloect_customer.requestFocus();
                    }
                    else
                    {
                        float margin_earns = (inpu_price-pro_price);
                        //Integer qty = Integer.parseInt(productCO.quantity);
                      //margin_earns = margin_earns*qty;
                        margine_your_earn.setText(String.valueOf(margin_earns));
                      //  productCO.finalCustomerPrice = String.valueOf(margin_earns);
                        calculateProduct_price();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

    }

    public void calculateProduct_price()
    {
        Integer couponAMount = 0;
        if(AppValidate.isNotEmpty(productCO.coupon_amount))
        {
            couponAMount = Integer.parseInt(productCO.coupon_amount);
            coupon_discount.setText("-"+IConstants.RUPEE_ICON+couponAMount);
        }
        else
        {
            coupon_discount.setText("-"+IConstants.RUPEE_ICON+couponAMount);
        }
       // float shippingCharge = Float.parseFloat(productCO.shipping_charge);
        //float f3=shippingCharge+productCO.getPriceQuantity();
        //float f4 = f3-couponAMount;
       // order_total_final.setText(IConstants.RUPEE_ICON+f4);

     //   float total_payble_amount = shippingCharge+Float.parseFloat(productCO.product_price);
      //  float fAmount= total_payble_amount-couponAMount;
      //  productCO.productFinalPrize =""+fAmount;


    }

    public void setData(ProductCO productCO)
    {
        calculateProduct_price();
        coupon_discount.setText("-"+IConstants.RUPEE_ICON+"0");
        product_charges.setText(IConstants.RUPEE_ICON+productCO.sell_price);
        product_quantity.setText(productCO.quantity);
        total_product_charges.setText(IConstants.RUPEE_ICON+productCO.getPriceQuantity());
        ordertotal.setText(IConstants.RUPEE_ICON+productCO.sell_price);
      //  shiping_charges.setText("+"+IConstants.RUPEE_ICON+productCO.shipping_charge);
      //  supplier.setText(productCO.merchant_name);
      //  product_title.setText(productCO.product_name);
      //  product_sale_price.setText("Price "+IConstants.RUPEE_ICON+productCO.product_price);
       // product_original_price.setText("MRP "+IConstants.RUPEE_ICON+productCO.mrp_price);
     //   product_size.setText("Size: "+productCO.selectedSize);
     //   quantity.setText("Qty: "+productCO.quantity);

        Glide.with(Order.this)
                .load(productCO.image)
                .into(product_image);
    }

    @Override
    protected void onViewClick(View view) {

        switch (view.getId()) {
            case R.id.order_process:

                if(cash_to_colloect_customer.getText().toString().trim().length()== 0)
                {
                    Toast.makeText(Order.this, "Enter Cash Amount", Toast.LENGTH_SHORT).show();
                }
                else
                {
                   // float pro_price = Float.parseFloat(productCO.product_price);
                   // float inpu_price = Float.parseFloat(cash_to_colloect_customer.getText().toString().trim());

                  //  if(pro_price >= inpu_price)
                 //   {
                 //       cash_to_colloect_customer.requestFocus();
                 //       Toast.makeText(Order.this, "Cash Amount is Low", Toast.LENGTH_SHORT).show();
                //    }
                //    else
                //    {
                       // productCO.finalCustomerPrice = cash_to_colloect_customer.getText().toString().trim();
                 //       String productCOString =  AppController.gson.toJson(productCO);
                 //       Intent i = new Intent(Order.this, MyAddress.class);
                 //       i.putExtra("productCO",productCOString);
                 //       startActivity(i);
                 //   }
                }
                 break;

            case R.id.apply_coupon_code:

                if (enter_coupon_code.getText().toString().trim().length() == 0) {
                    enter_coupon_code.requestFocus();
                    Toast.makeText(this, "Enter Coupon Code", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //api
                    CallApi callApipin = new CallApi(API.applyCoupon);
                    callApipin.addReqParams("product_id",productCO.id);
                    callApipin.addReqParams("coupon_code",enter_coupon_code.getText().toString().trim());
                    processCallApi(callApipin);
                }
                break;

        }

    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {

        if(API.applyCoupon.method.equals(callApi.networkActivity.method))
        {
            try
            {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");

                if (status.equalsIgnoreCase("success"))
                {
                    JSONObject data = action.getJSONObject("data");

                    productCO.coupon_amount = data.getString("coupon_amount");
                    productCO.coupon_id = data.getString("coupon_id");
                    calculateProduct_price();

                  //
                }
                else
                {
                    Toast.makeText(this, "somethig wrong", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}