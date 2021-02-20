package com.tws.trevon.activity.sp;
 import android.content.Intent;
 import android.os.Bundle;
 import android.view.Menu;
 import android.view.MenuItem;
 import android.view.View;
 import android.widget.LinearLayout;
 import android.widget.TextView;
 import android.widget.Toast;

 import com.tws.trevon.R;
 import com.tws.trevon.activity.AbstractActivity;
 import com.tws.trevon.activity.CartScreen;
 import com.tws.trevon.activity.LoginOriginal;
 import com.tws.trevon.activity.MyOrder;
 import com.tws.trevon.activity.Notification;
 import com.tws.trevon.activity.SearchPage;
 import com.tws.trevon.adapter.OrderListAdapter;
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

 import java.util.ArrayList;
 import java.util.List;

 import androidx.appcompat.widget.Toolbar;
 import androidx.core.view.MenuItemCompat;
 import androidx.recyclerview.widget.LinearLayoutManager;
 import androidx.recyclerview.widget.RecyclerView;

public class MyOrderListSp extends AbstractActivity
{
    RecyclerView my_orders_recyclerview;
    OrderListAdapterSp orderListAdapter;
    List<OrderCO> orderCOList = new ArrayList<>();
    LinearLayout no_order_found;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_list_sp);
        Toolbar toolbar = findViewById(R.id.my_orders_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Orders");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        no_order_found = findViewById(R.id.no_order_found);
        my_orders_recyclerview = findViewById(R.id.my_orders_recyclerview);

        String user_id = getIntent().getStringExtra("user_id");

        CallApi callApi = new CallApi(API.view_order_list);
        callApi.addReqParams("user_id", user_id);
        processCallApi(callApi);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        my_orders_recyclerview.setLayoutManager(layoutManager);
        orderListAdapter = new OrderListAdapterSp(this, orderCOList);
        my_orders_recyclerview.setAdapter(orderListAdapter);

        orderListAdapter.setEnrolledClickListener(new OrderListAdapterSp.EnrolledClickListner() {
            @Override
            public void onCancelCLick(View view, int position)
            {

            }

            @Override
            public void onOrderViewCLick(View view, int position)
            {
                OrderCO orderCO = orderCOList.get(position);

                Intent intent=new Intent(MyOrderListSp.this, MyOrderSP.class);
                intent.putExtra("orderid", orderCO.id);
                startActivityForResult(intent, IConstants.ACTIVITY_ORDER);// Activity is started with requestCode 2

            }

        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode== IConstants.ACTIVITY_ORDER)
        {
            // String message=data.getStringExtra("MESSAGE");
            //OrderCO orderCO = AppController.gson.fromJson(message, OrderCO.class);
            //  orderCOList.set(orderCO.order_position, orderCO);
            //orderListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onViewClick(View view) {

    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {
        if(API.view_order_list.method.equals(callApi.networkActivity.method))
        {
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");
                if (status.equalsIgnoreCase("Success"))
                {
                    orderCOList.clear();
                    JSONArray onbjnew = action.getJSONArray("data");
                    for(int i = 0 ; i<onbjnew.length();i++)
                    {
                        JSONObject jsonObject1 = onbjnew.getJSONObject(i);
                        OrderCO orderCO = AppController.gson.fromJson(jsonObject1.toString(), OrderCO.class);
                        orderCOList.add(orderCO);
                    }

                    if(orderCOList.size() > 0)
                    {
                        no_order_found.setVisibility(View.GONE);
                    }
                    else
                    {
                        no_order_found.setVisibility(View.VISIBLE);
                    }
                    orderListAdapter.orderCOList = orderCOList;
                    orderListAdapter.notifyDataSetChanged();


                } else {
                    no_order_found.setVisibility(View.VISIBLE);
                    Toast.makeText(this, nessage, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                no_order_found.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
        }

        else if(API.order_status_change.method.equals(callApi.networkActivity.method))
        {
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");
                if (status.equalsIgnoreCase("Success"))
                {
                    JSONArray onbjnew = action.getJSONArray("data");


                } else {

                    Toast.makeText(this, nessage, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }
}

