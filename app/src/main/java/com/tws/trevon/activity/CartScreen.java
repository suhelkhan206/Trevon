package com.tws.trevon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tws.trevon.R;
import com.tws.trevon.adapter.CartItemAdapter;
import com.tws.trevon.co.CartProductCO;
import com.tws.trevon.co.CheckoutCO;
import com.tws.trevon.co.ProductCO;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.API;
import com.tws.trevon.constants.IConstants;
import com.tws.trevon.constants.IReqParams;
import com.tws.trevon.fragment.PopupBottomDailogFragment2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartScreen extends AbstractActivity implements PopupBottomDailogFragment2.ItemClickListener {

    float allOrderPrice, allOrderOriginalPrice;
    TextView order_price;
    RecyclerView order_item_recycler;
    RelativeLayout palce_order_button;
    CartItemAdapter cartItemAdapter;
    ArrayList<CartProductCO> cartProductCOList =new ArrayList<>();
    LinearLayout no_cart_found_view, all_item_calculation;
    LinearLayout start_shopping_btn;
    TextView subtotal,total_mrp, cart_discount;
    TextView delivery_charges, price_items,save_amount, gst_amount;
    CheckoutCO checkoutCO;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_screen);

        Toolbar toolbar = findViewById(R.id.cart_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Cart");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        checkoutCO = new CheckoutCO();
        no_cart_found_view = findViewById(R.id.no_cart_found_view);
        all_item_calculation = findViewById(R.id.all_item_calculation);

        order_item_recycler = findViewById(R.id.order_item_recycler);
        palce_order_button = findViewById(R.id.palce_order_button);
        start_shopping_btn = findViewById(R.id.start_shopping_btn);
        delivery_charges = findViewById(R.id.delivery_charges);
        price_items = findViewById(R.id.price_items);
        save_amount = findViewById(R.id.save_amount);
        subtotal = findViewById(R.id.subtotal);
        total_mrp = findViewById(R.id.total_mrp);
        cart_discount= findViewById(R.id.cart_discount);
        start_shopping_btn.setOnClickListener(this);
        palce_order_button.setOnClickListener(this);
        order_price = findViewById(R.id.order_price);
        gst_amount = findViewById(R.id.gst_amount);
        cartItemAdapter = new CartItemAdapter(CartScreen.this,cartProductCOList,false);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(CartScreen.this);
        order_item_recycler.setLayoutManager(mLayoutManager2);
        order_item_recycler.setItemAnimator(new DefaultItemAnimator());
        order_item_recycler.setAdapter(cartItemAdapter);

        CallApi callApi = new CallApi(API.view_cart);
        callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
        processCallApi(callApi);
        adapterClickListner();

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        cartProductCOList.clear();
        cartItemAdapter.notifyDataSetChanged();
        CallApi callApi = new CallApi(API.view_cart);
        callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
        processCallApi(callApi);
        /// this.onCreate(null);
    }

    @Override
    public void onBackPressed()
    {

        Intent intent=new Intent();
        setResult(IConstants.INTENT_CART_COUNT_CODE,intent);
        finish();//finishing activity
        super.onBackPressed();
    }

    private void adapterClickListner()
    {
        cartItemAdapter.setClickListener(new CartItemAdapter.ClickListner()
        {
            @Override
            public void onIncreaseClick(View view, int position)
            {
               /* CartProductCO cartProductCO = cartProductCOList.get(position);
                Integer quantity =  Integer.parseInt(cartProductCO.total_quantity);

                if(quantity >= Integer.valueOf(cartProductCO.product.get(0).stock_quantity))
                {
                    Toast.makeText(CartScreen.this, "Out of Stock", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    quantity= quantity + 1;
                    cartProductCO.total_quantity = quantity+"";
                    cartItemAdapter.notifyDataSetChanged();
                    priceCalculation();
                }
*/


            }

            @Override
            public void onDecreaseClick(View view, int position)
            {
                /*CartProductCO cartProductCO = cartProductCOList.get(position);
                Integer quantity =  Integer.parseInt(cartProductCO.total_quantity);
                if(quantity >= 1)
                {
                    quantity= quantity - 1;
                }
                if(quantity == 0)
                {
                    cartProductCOList.remove(position);
                    CallApi callApi = new CallApi(API.RemoveToCart);

                    callApi.addReqParams("product_item_id", cartProductCO.id);//
                    callApi.addReqParams("product_id", cartProductCO.product_id);//
                    callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);//
                    processCallApi(callApi);

                 *//*   {
                        "user_id":"1",
                            "product_id":"25",
                            "quantity":"1",
                            "product_price_id":"52",
                            "product_price_city_id":"3310",
                            "method_name":"RemoveToCart"


                    }*//*


                }
                AppUtilities.writeToPref(IReqParams.USER_CART_COUNT, String.valueOf(cartProductCOList.size()));
                cartProductCO.total_quantity = quantity+"";
                cartItemAdapter.notifyDataSetChanged();
                priceCalculation();*/
            }

            @Override
            public void onitemClick(View view, int position)
            {
                CartProductCO cartProductCO = cartProductCOList.get(position);
                Intent i= new Intent(CartScreen.this, SingleProductScreen.class);
                i.putExtra("productId",cartProductCO.product_id);
                startActivity(i);
            }

            @Override
            public void onitemRemoveClick(View view, int position)
            {
                CartProductCO cartProductCO = cartProductCOList.get(position);

                CallApi callApi = new CallApi(API.RemoveToCart);
                callApi.addReqParams("product_item_id", cartProductCO.id);//
                callApi.addReqParams("product_id", cartProductCO.product_id);
                callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
                processCallApi(callApi);

                cartProductCOList.remove(position);
                cartItemAdapter.notifyDataSetChanged();
                AppUtilities.writeToPref(IReqParams.USER_CART_COUNT, String.valueOf(cartProductCOList.size()));
               // priceCalculation();
            }

            @Override
            public void onitemDetailClick(View view, int position)
            {
                CartProductCO cartProductCO = cartProductCOList.get(position);
                showBottomSheet(view, cartProductCO.product, cartProductCO.total_quantity,position);
            }
        });
    }

    public void showBottomSheet(View view, ProductCO productCO, String total_quantity,int position)
    {
        productCO.position = position;
        String productCOString = AppController.gson.toJson(productCO);
        PopupBottomDailogFragment2 addPhotoBottomDialogFragment = PopupBottomDailogFragment2.newInstance(productCOString, total_quantity);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                PopupBottomDailogFragment2.TAG);
    }
    @Override public void onItemClick(String item, String pos)
    {
        ProductCO productCO= AppController.gson.fromJson(pos, ProductCO.class);
        cartProductCOList.get(productCO.position).total_quantity = item;
        cartProductCOList.get(productCO.position).product = productCO;
        calculateTotalAmount();
    }


    @Override
    protected void onViewClick(View view)
    {
        switch(view.getId())
        {
            case R.id.palce_order_button:
                if(cartProductCOList.size() > 0)
                {
                    String checkoutCOstr = AppController.gson.toJson(checkoutCO);
                    Intent i =new Intent(CartScreen.this, MyAddress.class);
                    i.putExtra("checkoutCO",checkoutCOstr);
                    i.putExtra("is_checkout","Y");
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(CartScreen.this, "Cart Empty", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.start_shopping_btn:
                finish();
                break;

        }
    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi)
    {
        if(API.view_cart.method.equals(callApi.networkActivity.method))
        {
            try
            {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");

                if (status.equalsIgnoreCase("Success"))
                {
                    cartProductCOList.clear();
                    JSONArray onbjnew = action.getJSONArray("data");

                    for(int i=0;i<onbjnew.length(); i++)
                    {
                        JSONObject jsonObject1= onbjnew.getJSONObject(i);
                        CartProductCO cartProductCO = AppController.gson.fromJson(jsonObject1.toString(), CartProductCO.class);
                        cartProductCOList.add(cartProductCO);
                        //myAddressCOList.add(myAddressCO);
                    }
                    calculateTotalAmount();
                    cartItemAdapter.cartProductCOList = cartProductCOList;
                    cartItemAdapter.notifyDataSetChanged();
                    if(cartProductCOList.size() > 0)
                    {
                        no_cart_found_view.setVisibility(View.GONE);
                        all_item_calculation.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        no_cart_found_view.setVisibility(View.VISIBLE);
                        all_item_calculation.setVisibility(View.GONE);
                    }
                }
                else
                {
                    Toast.makeText(CartScreen.this, nessage, Toast.LENGTH_SHORT).show();
                    if(cartProductCOList.size() > 0)
                    {
                        no_cart_found_view.setVisibility(View.GONE);
                        all_item_calculation.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        no_cart_found_view.setVisibility(View.VISIBLE);
                        all_item_calculation.setVisibility(View.GONE);
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if(API.RemoveToCart.method.equals(callApi.networkActivity.method))
        {

        }
    }

    private void calculateTotalAmount()
    {
        int finalPrice = 0;
        int totalMrp = 0;
        for(int i=0;i<cartProductCOList.size();i++)
        {
            int all_ver_price = priceCalculation(cartProductCOList.get(i).product, cartProductCOList.get(i).total_quantity);
            finalPrice = finalPrice + all_ver_price;

            int all_mrp_price =mrpCalculation(cartProductCOList.get(i).product, cartProductCOList.get(i).total_quantity);
            totalMrp = totalMrp + all_mrp_price;
        }

        price_items.setText("Price ("+cartProductCOList.size()+" items)");
        total_mrp.setText(IConstants.RUPEE_ICON+totalMrp);
        int discount = totalMrp - finalPrice;
        cart_discount.setText(IConstants.RUPEE_ICON+discount);
        subtotal.setText(IConstants.RUPEE_ICON+ finalPrice);
        save_amount.setText("You will save "+discount+" on this order");
        delivery_charges.setText(IConstants.RUPEE_ICON+"0");
        ////GST calculation
        float  percent = 0.95f;
        float  gamount = finalPrice*percent;
        float  GST = finalPrice - gamount;
        float  excludeGstPrice = finalPrice  - GST;
        gst_amount.setText(IConstants.RUPEE_ICON+excludeGstPrice+ " + "+IConstants.RUPEE_ICON+ GST+"Tax");

        checkoutCO.gst = ""+GST;
        checkoutCO.totalMrp = ""+totalMrp;
        checkoutCO.totalPrice = ""+finalPrice;
        checkoutCO.discount = ""+discount;
        checkoutCO.deliveryCharge = "";
        checkoutCO.cartProductCOList =  cartProductCOList;



    }


       public int priceCalculation(ProductCO productCO,String totalQuantity)
       {
         int all_ver_price = 0;
         int totalPrice;

        Integer retailer_max_purchase_qty = Integer.parseInt( productCO.retailer_max_purchase_qty);
           Integer totalQuantityint = Integer.parseInt(totalQuantity);

        if(totalQuantityint > retailer_max_purchase_qty)
        {
            Integer w_price = Integer.parseInt(productCO.wholesale_price);
            Integer piece_in_set = Integer.parseInt(productCO.piece_in_set);
            Integer overAllPrice = piece_in_set*w_price;
            totalPrice = totalQuantityint*overAllPrice;
            productCO.product_purchase_type = "Wholeseller";

        }
        else
        {
            Integer r_price = Integer.parseInt(productCO.retail_price);
            Integer piece_in_set = Integer.parseInt(productCO.piece_in_set);
            Integer overAllPrice = piece_in_set*r_price;
            totalPrice = totalQuantityint*overAllPrice;
            productCO.product_purchase_type = "Retailer";
        }

        all_ver_price = all_ver_price+totalPrice;

        return all_ver_price;
    }


    public int mrpCalculation(ProductCO productCO,String totalQuantity)
    {
        int all_ver_price = 0;

        Integer totalQuantityint = Integer.parseInt(totalQuantity);
        int totalmrp;
        Integer mrp = Integer.parseInt(productCO.mrp);
        Integer set_of_piece = Integer.parseInt(productCO.piece_in_set);
        totalmrp = totalQuantityint*mrp*set_of_piece;
        productCO.product_purchase_type = "Retailer";


        all_ver_price = all_ver_price+totalmrp;

        return all_ver_price;
    }


}