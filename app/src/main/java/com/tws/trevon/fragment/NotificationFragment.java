package com.tws.trevon.fragment;

import android.os.Bundle;

        import androidx.fragment.app.Fragment;
        import androidx.recyclerview.widget.DefaultItemAnimator;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.LinearLayout;
        import android.widget.Toast;

        import com.tws.trevon.R;
        import com.tws.trevon.adapter.NotificationAdapter;
        import com.tws.trevon.co.UserCO;
        import com.tws.trevon.common.AppController;
        import com.tws.trevon.common.AppValidate;
        import com.tws.trevon.common.CallApi;
        import com.tws.trevon.constants.API;
        import com.tws.trevon.model.NotificationCO;

        import org.json.JSONArray;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.List;


public class NotificationFragment extends AbstractFragment {

    View view;
    RecyclerView notification_recycler;
    NotificationAdapter notificationAdapter;
    List<NotificationCO> notificationCOList = new ArrayList<>();
    LinearLayout no_notification_found;
    SwipeRefreshLayout mySwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notification, container, false);

        notification_recycler = view.findViewById(R.id.notification_recycler);
        no_notification_found = view.findViewById(R.id.no_notification_found);
        mySwipeRefreshLayout = view.findViewById(R.id.swipeContainer);

        notificationAdapter = new NotificationAdapter(getActivity(),notificationCOList);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
        notification_recycler.setLayoutManager(mLayoutManager2);
        notification_recycler.setItemAnimator(new DefaultItemAnimator());
        notification_recycler.setAdapter(notificationAdapter);


        if(AppValidate.isEmpty(notificationCOList))
        {
            CallApi callApi = new CallApi(API.get_notifications);
            callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);//
            processCallApi(callApi);
        }

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh()
                    {
                        CallApi callApi = new CallApi(API.get_notifications);
                        callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);//
                        processCallApi(callApi);

                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

        return view;
    }
    @Override
    protected void onViewClick(View view) {

    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {
        if(API.get_notifications.method.equals(callApi.networkActivity.method))
        {
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");

                if (status.equalsIgnoreCase("Success"))
                {
                    notificationCOList.clear();
                    JSONArray onbjnew = action.getJSONArray("data");

                    for(int i=0;i<onbjnew.length(); i++)
                    {
                        JSONObject jsonObject1= onbjnew.getJSONObject(i);
                        NotificationCO notificationCO = AppController.gson.fromJson(jsonObject1.toString(), NotificationCO.class);
                        notificationCOList.add(notificationCO);
                    }
                    if(notificationCOList.size()>0)
                    {
                        no_notification_found.setVisibility(View.GONE);
                    }else
                    {
                        no_notification_found.setVisibility(View.VISIBLE);
                    }
                    notificationAdapter.notificationCOList = notificationCOList;
                    notificationAdapter.notifyDataSetChanged();
                }
                else
                { no_notification_found.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), nessage, Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                no_notification_found.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
        }
    }
}