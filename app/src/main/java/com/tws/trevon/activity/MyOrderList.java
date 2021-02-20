package com.tws.trevon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tws.trevon.R;
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

public class MyOrderList extends AbstractActivity
{
    RecyclerView my_orders_recyclerview;
    OrderListAdapter orderListAdapter;
    List<OrderCO> orderCOList = new ArrayList<>();
    LinearLayout no_order_found;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_list);
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

        CallApi callApi = new CallApi(API.view_order_list);
        callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
        processCallApi(callApi);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        my_orders_recyclerview.setLayoutManager(layoutManager);
        orderListAdapter = new OrderListAdapter(this, orderCOList);
        my_orders_recyclerview.setAdapter(orderListAdapter);

        orderListAdapter.setEnrolledClickListener(new OrderListAdapter.EnrolledClickListner() {
            @Override
            public void onCancelCLick(View view, int position)
            {
                OrderCO orderCO = orderCOList.get(position);

                if(orderCO.delivery_status.equals("0"))
                {
                    orderCO.delivery_status = "3";
                    orderListAdapter.notifyDataSetChanged();
                    CallApi callApi = new CallApi(API.order_status_change);
                    callApi.addReqParams("type", "cancel");
                    callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
                    callApi.addReqParams("order_id", orderCO.id);
                    processCallApi(callApi);
                }
                else if(orderCO.delivery_status.equals("2"))
                {
                    orderCO.delivery_status = "4";
                    orderListAdapter.notifyDataSetChanged();
                    CallApi callApi = new CallApi(API.order_status_change);
                    callApi.addReqParams("type", "return");
                    callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
                    callApi.addReqParams("order_id", orderCO.id);
                    processCallApi(callApi);
                }
                else if(orderCO.delivery_status.equals("4"))
                {
                    orderCO.delivery_status = "5";
                    orderListAdapter.notifyDataSetChanged();
                    CallApi callApi = new CallApi(API.order_status_change);
                    callApi.addReqParams("type", "refund");
                    callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
                    callApi.addReqParams("order_id", orderCO.id);
                    processCallApi(callApi);
                }

            }

            @Override
            public void onOrderViewCLick(View view, int position)
            {
                OrderCO orderCO = orderCOList.get(position);

                Intent intent=new Intent(MyOrderList.this,MyOrder.class);
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
            Intent i= new Intent(MyOrderList.this, SearchPage.class);
            startActivity(i);
        }

        else if (item.getItemId()==R.id.notification_home)
        {
            UserCO userCO =  UserCO.getUserCOFromDB();
            if(AppValidate.isNotEmpty(userCO.id))
            {
                Intent i =new Intent(MyOrderList.this,Notification.class);
                startActivityForResult(i, IConstants.INTENT_CART_COUNT_CODE);// Activity is started with requestCode 2
            }
            else
            {
                Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
                Intent ilogin = new Intent(MyOrderList.this,LoginOriginal.class);
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
                Intent i =new Intent(MyOrderList.this,CartScreen.class);
                startActivityForResult(i, IConstants.INTENT_CART_COUNT_CODE);// Activity is started with requestCode 2
            }
            else
            {
                Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
                Intent ilogin = new Intent(MyOrderList.this,LoginOriginal.class);
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