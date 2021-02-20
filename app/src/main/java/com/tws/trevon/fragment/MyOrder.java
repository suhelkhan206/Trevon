package com.tws.trevon.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tws.trevon.R;
import com.tws.trevon.activity.MyOrderList;
import com.tws.trevon.adapter.OrderListAdapter;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.API;
import com.tws.trevon.constants.IConstants;
import com.tws.trevon.model.OrderCO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyOrder extends AbstractFragment
{
    View view;
    RecyclerView my_orders_recyclerview;
    OrderListAdapter orderListAdapter;
    List<OrderCO> orderCOList = new ArrayList<>();
    LinearLayout no_order_found;
    SwipeRefreshLayout mySwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_my_order, container, false);
        my_orders_recyclerview = view.findViewById(R.id.my_orders_recyclerview);
        mySwipeRefreshLayout = view.findViewById(R.id.swipeContainer);
        no_order_found = view.findViewById(R.id.no_order_found);
        CallApi callApi = new CallApi(API.view_order_list);
        callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
        processCallApi(callApi);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        my_orders_recyclerview.setLayoutManager(layoutManager);
        orderListAdapter = new OrderListAdapter(getActivity(), orderCOList);
        my_orders_recyclerview.setAdapter(orderListAdapter);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh()
                    {
                        CallApi callApi = new CallApi(API.view_order_list);
                        callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
                        processCallApi(callApi);

                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

        orderListAdapter.setEnrolledClickListener(new OrderListAdapter.EnrolledClickListner() {

            @Override
            public void onCancelCLick(View view, int position) {
                OrderCO orderCO = orderCOList.get(position);

                if(orderCO.delivery_status.equals("0"))
                {
                    orderCO.delivery_status = "1";
                    orderListAdapter.notifyDataSetChanged();
                    CallApi callApi = new CallApi(API.order_status_change);
                    callApi.addReqParams("type", "cancel");
                    callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
                    callApi.addReqParams("order_id", orderCO.id);
                    processCallApi(callApi);
                }

            }

            @Override
            public void onOrderViewCLick(View view, int position)
            {
                OrderCO orderCO = orderCOList.get(position);
                orderCO.order_position = position;

                Intent intent=new Intent(getActivity(), com.tws.trevon.activity.MyOrder.class);
                intent.putExtra("orderid", orderCO.id);
                startActivityForResult(intent, IConstants.ACTIVITY_ORDER);// Activity is started with requestCode 2

            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode== IConstants.ACTIVITY_ORDER)
        {
            String message=data.getStringExtra("MESSAGE");
            OrderCO orderCO = AppController.gson.fromJson(message, OrderCO.class);
            orderCOList.set(orderCO.order_position, orderCO);
            orderListAdapter.notifyDataSetChanged();
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
                    Toast.makeText(getActivity(), nessage, Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(getActivity(), nessage, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }

    }
}