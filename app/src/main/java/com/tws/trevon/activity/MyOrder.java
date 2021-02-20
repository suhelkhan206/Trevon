package com.tws.trevon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.tws.trevon.R;
import com.tws.trevon.adapter.CartItemAdapter;
import com.tws.trevon.adapter.OrderProductDetailAdapter;
import com.tws.trevon.co.ProductCO;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.API;
import com.tws.trevon.constants.IConstants;
import com.tws.trevon.constants.IReqParams;
import com.tws.trevon.model.OrderCO;

import org.json.JSONArray;
import org.json.JSONObject;

public class MyOrder extends AbstractActivity {

    View return_divider, order_return_supporter, view_order_return, refund_divider,
            order_refund_supporter, view_order_refund,
            view_order_placed, view_order_confirmed,view_order_processed,
            view_order_pickup,con_divider,ready_divider,placed_divider;

    TextView return_date_final, textorder_return, refund_return_date, textorder_refund,
            textorderpickup,text_confirmed,textorderprocessed;

    ConstraintLayout order_track_view;

    ImageView orderreturn, orderrefund,img_orderconfirmed,orderprocessed,orderpickup;

    OrderCO orderCO;

    public TextView order_date,transection_id,
            payment_type,total_amount,order_id;

    public TextView price_items, total_mrp, cart_discount,
            delivery_charges,subtotal,save_amount;
    RecyclerView order_item_recyclerview;
    OrderProductDetailAdapter orderProductDetailAdapter;
    LinearLayout order_detail_full_view;
    LinearLayout cancel_order;
    TextView order_status;
    TextView gst;
    LinearLayout download_invoice;

    @Override
    public void onBackPressed()
    {
        String orderCoString = AppController.gson.toJson(orderCO);
        Intent intent=new Intent();
        intent.putExtra("MESSAGE",orderCoString);
        setResult(IConstants.ACTIVITY_ORDER,intent);
        finish();//finishing activity
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        Toolbar toolbar = findViewById(R.id.my_order_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
      //  getSupportActionBar().setTitle("My Order");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String orderCoString = AppController.gson.toJson(orderCO);
                Intent intent=new Intent();
                intent.putExtra("MESSAGE",orderCoString);
                setResult(IConstants.ACTIVITY_ORDER,intent);
                finish();//finishing activity

            }
        });
        download_invoice = findViewById(R.id.download_invoice);
        order_status = findViewById(R.id.order_status);
        cancel_order = findViewById(R.id.cancel_order);
        cancel_order.setOnClickListener(this);
        download_invoice.setOnClickListener(this);
        gst = findViewById(R.id.gst);
        view_order_placed=findViewById(R.id.view_order_placed);
        view_order_confirmed=findViewById(R.id.view_order_confirmed);
        view_order_processed=findViewById(R.id.view_order_processed);
        view_order_pickup=findViewById(R.id.view_order_pickup);
        placed_divider=findViewById(R.id.placed_divider);
        con_divider=findViewById(R.id.con_divider);
        ready_divider=findViewById(R.id.ready_divider);

        return_divider = findViewById(R.id.return_divider);
        order_return_supporter = findViewById(R.id.order_return_supporter);
        view_order_return = findViewById(R.id.view_order_return);
        refund_divider = findViewById(R.id.refund_divider);
        order_refund_supporter = findViewById(R.id.order_refund_supporter);
        view_order_refund = findViewById(R.id.view_order_refund);
        return_date_final = findViewById(R.id.return_date_final);
        textorder_return = findViewById(R.id.textorder_return);
        refund_return_date = findViewById(R.id.refund_return_date);
        textorder_refund = findViewById(R.id.textorder_refund);

        textorderpickup=findViewById(R.id.textorderpickup);
        text_confirmed=findViewById(R.id.text_confirmed);
        textorderprocessed=findViewById(R.id.textorderprocessed);
        order_track_view = findViewById(R.id.order_track_view);

        img_orderconfirmed=findViewById(R.id.img_orderconfirmed);
        orderprocessed=findViewById(R.id.orderprocessed);
        orderpickup=findViewById(R.id.orderpickup);

//////
        order_date = findViewById(R.id.order_date);
        order_id = findViewById(R.id.order_id);
        transection_id = findViewById(R.id.transection_id);
        payment_type = findViewById(R.id.payment_type);
        total_amount = findViewById(R.id.total_amount);


/////
        delivery_charges = findViewById(R.id.delivery_charges);
        price_items = findViewById(R.id.price_items);
        save_amount = findViewById(R.id.save_amount);
        subtotal = findViewById(R.id.subtotal);
        total_mrp = findViewById(R.id.total_mrp);
        cart_discount= findViewById(R.id.cart_discount);

        order_item_recyclerview = findViewById(R.id.order_item_recyclerview);
        order_detail_full_view = findViewById(R.id.order_detail_full_view);

        Intent intent = getIntent();
        String orderId = intent.getExtras().getString("orderid");
        getSupportActionBar().setTitle("#"+orderId);
        CallApi callApi = new CallApi(API.my_order_detail);
        callApi.addReqParams("order_id", orderId);
        processCallApi(callApi);
    }

    public void setData()
    {
        button_update();
        order_detail_full_view.setVisibility(View.VISIBLE);
        order_date.setText(orderCO.created_date);
        order_id.setText("Order ID: #"+orderCO.id);
        transection_id.setText(orderCO.transaction_id);
        payment_type.setText(orderCO.payment_mode.toUpperCase());
        total_amount.setText(IConstants.RUPEE_ICON+ orderCO.total_paid);

        delivery_charges.setText(IConstants.RUPEE_ICON+ "0");
        price_items.setText("Price ("+orderCO.order_detail.size()+" items)");

       /* Integer intAmount = Integer.parseInt(orderCO.total_amount);
        Integer intgst = Integer.parseInt(orderCO.gst_amount);
        int amount = intAmount - intgst;*/

        gst.setText(IConstants.RUPEE_ICON+orderCO.gst_amount+"Tax");
        save_amount.setText("You will save "+orderCO.total_discount+" on this order");
        subtotal.setText(IConstants.RUPEE_ICON+ orderCO.total_paid);
        total_mrp.setText(IConstants.RUPEE_ICON+ orderCO.total_amount);
        cart_discount.setText(IConstants.RUPEE_ICON+ orderCO.total_discount);

        getOrderStatus(orderCO.delivery_status);

        orderProductDetailAdapter = new OrderProductDetailAdapter(MyOrder.this,orderCO.order_detail,true);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(MyOrder.this);
        order_item_recyclerview.setLayoutManager(mLayoutManager2);
        order_item_recyclerview.setItemAnimator(new DefaultItemAnimator());
        order_item_recyclerview.setAdapter(orderProductDetailAdapter);
        orderProductDetailAdapter.notifyDataSetChanged();

      /*  orderProductDetailAdapter.setClickListener(new OrderProductDetailAdapter.ClickListner() {
            @Override
            public void onDownloadInvoiceClick(View view, int position) {
                orderCO.order_detail.get(position);

                try{
                    AppUtilities.downloadFile(Uri.parse(orderCO.invoice_url), "Invoice-"+orderCO.created_date, MyOrder.this);
                    Toast.makeText(MyOrder.this, "Downloading...", Toast.LENGTH_SHORT).show();
                }
                catch(Exception e)
                {
                    Toast.makeText(MyOrder.this, "Try after sometime...", Toast.LENGTH_SHORT).show();
                }

            }
        });
*/
    }

    public void button_update()
    {
        if(orderCO.delivery_status.equals("0"))
        {
            order_status.setText("Cancel");

        }
        else if(orderCO.delivery_status.equals("1"))
        {
            order_status.setText("Cancel");
        }
        else if(orderCO.delivery_status.equals("2"))
        {
            order_status.setText("Return");

        }
        else if(orderCO.delivery_status.equals("3"))
        {
            order_status.setText("Cancelled");

        }
        else if(orderCO.delivery_status.equals("4"))
        {
            order_status.setText("Refund Requested");

        }
        else if(orderCO.delivery_status.equals("5"))
        {
            order_status.setText("Refunded");

        }
        else if(orderCO.delivery_status.equals("6"))
        {
            order_status.setText("Cancel");

        }
    }

    @Override
    protected void onViewClick(View view) {
        switch (view.getId())
        {
           case R.id.cancel_order:
               if(orderCO.delivery_status.equals("0") || orderCO.delivery_status.equals("1") || orderCO.delivery_status.equals("6"))
               {
                   orderCO.delivery_status = "3";
                   CallApi callApi = new CallApi(API.order_status_change);
                   callApi.addReqParams("type", "cancel");
                   callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
                   callApi.addReqParams("order_id", orderCO.id);
                   processCallApi(callApi);
                   button_update();
               }
               else if(orderCO.delivery_status.equals("2"))
               {
                   orderCO.delivery_status = "4";
                   CallApi callApi = new CallApi(API.order_status_change);
                   callApi.addReqParams("type", "return");
                   callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
                   callApi.addReqParams("order_id", orderCO.id);
                   processCallApi(callApi);
                   button_update();
               }
               else if(orderCO.delivery_status.equals("4"))
               {
                   orderCO.delivery_status = "5";
                   CallApi callApi = new CallApi(API.order_status_change);
                   callApi.addReqParams("type", "refund");
                   callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
                   callApi.addReqParams("order_id", orderCO.id);
                   processCallApi(callApi);
                   button_update();
               }

               getOrderStatus(orderCO.delivery_status);


               break;

            case R.id.download_invoice:
                try
                {
                    AppUtilities.downloadFile(Uri.parse(orderCO.invoice_url), "Invoice-"+orderCO.created_date, MyOrder.this);
                    Toast.makeText(MyOrder.this, "Downloading...", Toast.LENGTH_SHORT).show();
                }
                catch(Exception e)
                {
                    Toast.makeText(MyOrder.this, "Try after sometime...", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {
        if(API.my_order_detail.method.equals(callApi.networkActivity.method))
        {
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");
                if (status.equalsIgnoreCase("Success"))
                {
                    JSONObject data = action.getJSONObject("data");
                    orderCO = AppController.gson.fromJson(data.toString(), OrderCO.class);
                    setData();

                } else {

                    Toast.makeText(this, nessage, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }


    private void getOrderStatus(String orderStatus)
    {
        ///////////////////////////////
        if (orderStatus.equals("0")){
            float alfa= (float) 0.5;
            setStatus(alfa);
            order_track_view.setVisibility(View.VISIBLE);

        }else if (orderStatus.equals("1")){
            float alfa= (float) 0.5;
            setStatus1(alfa);
            order_track_view.setVisibility(View.VISIBLE);


        }else if (orderStatus.equals("2")){
            float alfa= (float) 0.5;
            setStatus2(alfa);
            order_track_view.setVisibility(View.VISIBLE);


        }else if (orderStatus.equals("3")){
            float alfa= (float) 0.5;
            setStatus3(alfa);
            order_track_view.setVisibility(View.VISIBLE);
        }
        else if (orderStatus.equals("4")){
            float alfa= (float) 0.5;
            setStatus4(alfa);
            order_track_view.setVisibility(View.VISIBLE);
        }
        else if (orderStatus.equals("5")){
            float alfa= (float)0.5;
            setStatus5(alfa);
            order_track_view.setVisibility(View.VISIBLE);
        }
        else if (orderStatus.equals("6")){
            float alfa= (float)0.5;
            setStatus6(alfa);
            order_track_view.setVisibility(View.VISIBLE);
        }

    }


    private void setStatus(float alfa)
    {
        float myf= (float) 0.5;
        view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));

        //
        placed_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_black));
        placed_divider.setAlpha(alfa);
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        view_order_confirmed.setAlpha(alfa);
        text_confirmed.setAlpha(alfa);

        //
        con_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_black));
        con_divider.setAlpha(alfa);
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        view_order_processed.setAlpha(alfa);
        textorderprocessed.setAlpha(alfa);

        //
        ready_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_black));
        ready_divider.setAlpha(alfa);
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        view_order_pickup.setAlpha(alfa);
        textorderpickup.setAlpha(alfa);

        //
        refund_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_black));
        refund_divider.setAlpha(alfa);
        view_order_refund.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        view_order_refund.setAlpha(alfa);
        textorder_refund.setAlpha(alfa);

        //
        return_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_black));
        return_divider.setAlpha(alfa);
        view_order_return.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        view_order_return.setAlpha(alfa);
        textorder_return.setAlpha(alfa);
    }

    private void setStatus1(float alfa)
    {
        float myf= (float) 0.5;
        view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));

        //
        placed_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_yellow));
       // placed_divider.setAlpha(alfa);
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
       // view_order_confirmed.setAlpha(alfa);
       // text_confirmed.setAlpha(alfa);

        //
        con_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_black));
        con_divider.setAlpha(alfa);
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        view_order_processed.setAlpha(alfa);
        textorderprocessed.setAlpha(alfa);

        //
        ready_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_black));
        ready_divider.setAlpha(alfa);
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        view_order_pickup.setAlpha(alfa);
        textorderpickup.setAlpha(alfa);

        //
        refund_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_black));
        refund_divider.setAlpha(alfa);
        view_order_refund.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        view_order_refund.setAlpha(alfa);
        textorder_refund.setAlpha(alfa);

        //
        return_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_black));
        return_divider.setAlpha(alfa);
        view_order_return.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        view_order_return.setAlpha(alfa);
        textorder_return.setAlpha(alfa);
    }

    private void setStatus6(float alfa)
    {
        float myf= (float) 0.5;
        view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));

        //
        placed_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_yellow));
        // placed_divider.setAlpha(alfa);
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        // view_order_confirmed.setAlpha(alfa);
        // text_confirmed.setAlpha(alfa);

        //
        con_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_yellow));
       // con_divider.setAlpha(alfa);
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
       // view_order_processed.setAlpha(alfa);
       // textorderprocessed.setAlpha(alfa);

        //
        ready_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_black));
        ready_divider.setAlpha(alfa);
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        view_order_pickup.setAlpha(alfa);
        textorderpickup.setAlpha(alfa);

        //
        refund_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_black));
        refund_divider.setAlpha(alfa);
        view_order_refund.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        view_order_refund.setAlpha(alfa);
        textorder_refund.setAlpha(alfa);

        //
        return_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_black));
        return_divider.setAlpha(alfa);
        view_order_return.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        view_order_return.setAlpha(alfa);
        textorder_return.setAlpha(alfa);
    }

    private void setStatus3(float alfa)
    {
        float myf= (float) 0.5;
        view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        view_order_placed.setAlpha(alfa);
        //
        placed_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_black));
        placed_divider.setAlpha(alfa);
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        view_order_confirmed.setAlpha(alfa);
        text_confirmed.setAlpha(alfa);

        //
        con_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_black));
        con_divider.setAlpha(alfa);
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        view_order_processed.setAlpha(alfa);
        textorderprocessed.setAlpha(alfa);

        //
        ready_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_black));
        ready_divider.setAlpha(alfa);
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        view_order_pickup.setAlpha(alfa);
        textorderpickup.setAlpha(alfa);

        //
        refund_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_black));
        refund_divider.setAlpha(alfa);
        view_order_refund.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        view_order_refund.setAlpha(alfa);
        textorder_refund.setAlpha(alfa);

        //
        return_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_black));
        return_divider.setAlpha(alfa);
        view_order_return.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        view_order_return.setAlpha(alfa);
        textorder_return.setAlpha(alfa);
    }

    private void setStatus2(float alfa)
    {
        float myf= (float) 0.5;
        view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));

        //
        placed_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_yellow));
        // placed_divider.setAlpha(alfa);
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        // view_order_confirmed.setAlpha(alfa);
        // text_confirmed.setAlpha(alfa);

        //
        con_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_yellow));
        // con_divider.setAlpha(alfa);
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        // view_order_processed.setAlpha(alfa);
        // textorderprocessed.setAlpha(alfa);

        //
        ready_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_yellow));
        ready_divider.setAlpha(alfa);
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        view_order_pickup.setAlpha(alfa);
        textorderpickup.setAlpha(alfa);

        //
        refund_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_black));
        refund_divider.setAlpha(alfa);
        view_order_refund.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        view_order_refund.setAlpha(alfa);
        textorder_refund.setAlpha(alfa);

        //
        return_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_black));
        return_divider.setAlpha(alfa);
        view_order_return.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        view_order_return.setAlpha(alfa);
        textorder_return.setAlpha(alfa);
    }

    private void setStatus4(float alfa)
    {
        float myf= (float) 0.5;
        view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));

        //
        placed_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_yellow));
        // placed_divider.setAlpha(alfa);
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        // view_order_confirmed.setAlpha(alfa);
        // text_confirmed.setAlpha(alfa);

        //
        con_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_yellow));
        // con_divider.setAlpha(alfa);
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        // view_order_processed.setAlpha(alfa);
        // textorderprocessed.setAlpha(alfa);

        //
        ready_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_yellow));
        ready_divider.setAlpha(alfa);
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        view_order_pickup.setAlpha(alfa);
        textorderpickup.setAlpha(alfa);

        //
        refund_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_yellow));
        refund_divider.setAlpha(alfa);
        view_order_refund.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        view_order_refund.setAlpha(alfa);
        textorder_refund.setAlpha(alfa);

        //
        return_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_black));
        return_divider.setAlpha(alfa);
        view_order_return.setBackground(getResources().getDrawable(R.drawable.shape_status_current));
        view_order_return.setAlpha(alfa);
        textorder_return.setAlpha(alfa);
    }

    private void setStatus5(float alfa)
    {
        float myf= (float) 0.5;
        view_order_placed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));

        //
        placed_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_yellow));
        // placed_divider.setAlpha(alfa);
        view_order_confirmed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        // view_order_confirmed.setAlpha(alfa);
        // text_confirmed.setAlpha(alfa);

        //
        con_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_yellow));
        // con_divider.setAlpha(alfa);
        view_order_processed.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        // view_order_processed.setAlpha(alfa);
        // textorderprocessed.setAlpha(alfa);

        //
        ready_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_yellow));
        ready_divider.setAlpha(alfa);
        view_order_pickup.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        view_order_pickup.setAlpha(alfa);
        textorderpickup.setAlpha(alfa);

        //
        refund_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_yellow));
        refund_divider.setAlpha(alfa);
        view_order_refund.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        view_order_refund.setAlpha(alfa);
        textorder_refund.setAlpha(alfa);

        //
        return_divider.setBackground(getResources().getDrawable(R.drawable.line_shape_yellow));
        return_divider.setAlpha(alfa);
        view_order_return.setBackground(getResources().getDrawable(R.drawable.shape_status_completed));
        view_order_return.setAlpha(alfa);
        textorder_return.setAlpha(alfa);
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
            Intent i= new Intent(MyOrder.this, SearchPage.class);
            startActivity(i);
        }

        else if (item.getItemId()==R.id.notification_home)
        {
            UserCO userCO =  UserCO.getUserCOFromDB();
            if(AppValidate.isNotEmpty(userCO.id))
            {
                Intent i =new Intent(MyOrder.this,Notification.class);
                startActivityForResult(i, IConstants.INTENT_CART_COUNT_CODE);// Activity is started with requestCode 2
            }
            else
            {
                Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
                Intent ilogin = new Intent(MyOrder.this,LoginOriginal.class);
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
                Intent i =new Intent(MyOrder.this,CartScreen.class);
                startActivityForResult(i, IConstants.INTENT_CART_COUNT_CODE);// Activity is started with requestCode 2
            }
            else
            {
                Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
                Intent ilogin = new Intent(MyOrder.this,LoginOriginal.class);
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

}