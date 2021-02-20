package com.tws.trevon.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tws.trevon.R;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.API;
import com.tws.trevon.constants.IConstants;
import com.tws.trevon.constants.IErrorCodes;


public class APITesting extends AbstractActivity
{
    private static final String TAG = APITesting.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        onCreateExtra(R.layout.a_projects);

        //processCallApi(new CallApi(API.getProjects));
        //processCallApi(new CallApi(API.getAboutUs));
        //processCallApi(new CallApi(API.getMileStones));
        //saveQuery();
        //processCallApi(new CallApi(API.getMyQueries));
        //processCallApi(new CallApi(API.getMyPaymentDetails));
        //processCallApi(new CallApi(API.getUserObject)); //to get profile details

        processCallApi(new CallApi(API.login_with_otp));//login for admin

        if (getIntent() != null) {
            if (getIntent().getBooleanExtra(IConstants.IS_NOTIF_CLICK, false))
            {
                switch (getIntent().getIntExtra(IConstants.FORWARD_TO, IConstants.NOTIFICATIONS))
                {
                    //startActivity(Notifications);
                    //finish();
                }
            }
        }
    }

    private void saveQuery()
    {
        /*QueryCO queryCO = new QueryCO();
        queryCO.title = "Complain about behaviour";
        queryCO.description = "Staff did not treat properly";
        queryCO.type = "Complain";

        CallApi callApi = new CallApi(API.saveQuery);
        callApi.addReqParams("queryCO", AppController.gson.toJson(queryCO));
        processCallApi(callApi);*/
    }

    @Override
    protected void onViewClick(View view)
    {
        switch(view.getId())
        {

        }
    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi)
    {
       /* if(API.getProjects.method.equals(callApi.networkActivity.method))
        {
            List<Map> projectList = (List<Map>)((Map)responseValues).get("projectList");

            for(Map projectMap : projectList)
            {
                String jsonString = AppController.gson.toJson(projectMap);
            }
        }
        else if(API.getAboutUs.method.equals(callApi.networkActivity.method))
        {
            List<Map> aboutUsList = (List<Map>)((Map)responseValues).get("aboutUsList");
            List<PreferenceCO> aboutUsPreferenceCOList = new ArrayList<>();

            for(Map aboutUsMap : aboutUsList)
            {
                String jsonString = AppController.gson.toJson(aboutUsMap);
                PreferenceCO preferenceCO = AppController.gson.fromJson(jsonString, PreferenceCO.class);
                aboutUsPreferenceCOList.add(preferenceCO);
            }
        }
        else if(API.getMileStones.method.equals(callApi.networkActivity.method))
        {
            List<Map> milestoneList = (List<Map>)((Map)responseValues).get("milestoneList");
*//*            List<PreferenceCO> milestonePreferenceCOList = new ArrayList<>();

            for(Map milestoneMap : milestoneList)
            {
                String jsonString = AppController.gson.toJson(milestoneMap);
                PreferenceCO preferenceCO = AppController.gson.fromJson(jsonString, PreferenceCO.class);
                milestonePreferenceCOList.add(preferenceCO);
            }*//*
        }
        else if(API.saveQuery.method.equals(callApi.networkActivity.method))
        {
            Map queryMap = (Map)((Map)responseValues).get("queryCO");
            String jsonString = AppController.gson.toJson(queryMap);
            QueryCO queryCO = AppController.gson.fromJson(jsonString, QueryCO.class);
        }
        else if(API.getMyPaymentDetails.method.equals(callApi.networkActivity.method))
        {
            List<Map> userPaymentList = (List<Map>)((Map)responseValues).get("userPaymentDetailList");
            List<UserPaymentDetailCO> userPaymentDetailCOList = new ArrayList<>();

            for(Map userPaymentMap : userPaymentList)
            {
                String jsonString = AppController.gson.toJson(userPaymentMap);
                UserPaymentDetailCO userPaymentDetailCO = AppController.gson.fromJson(jsonString, UserPaymentDetailCO.class);
                userPaymentDetailCOList.add(userPaymentDetailCO);
            }
        }
        else if(API.getMyQueries.method.equals(callApi.networkActivity.method))
        {
            List<Map> queryList = (List<Map>)((Map)responseValues).get("queryList");
            List<QueryCO> queryCOList = new ArrayList<>();

            for(Map queryMap : queryList)
            {
                String jsonString = AppController.gson.toJson(queryMap);
                QueryCO queryCO = AppController.gson.fromJson(jsonString, QueryCO.class);
                queryCOList.add(queryCO);
            }
        }
        else if(API.admin_login.method.equals(callApi.networkActivity.method))
        {
            String jsonString = AppController.gson.toJson(((Map)responseValues).get("USER"));
            User user = AppController.gson.fromJson(jsonString, User.class);

            List<String> roleList = (List<String>)((Map)responseValues).get("roleList");
        }
        else if(API.getUserObject.method.equals(callApi.networkActivity.method))
        {
            String jsonString = AppController.gson.toJson(((Map)responseValues).get("USER"));
            User user = AppController.gson.fromJson(jsonString, User.class);
        }*/
    }

    @Override
    public void onApiCallError(CallApi callApi, final String errorCode)
    {
        if(IErrorCodes.INTERNET_NOT_AVAILABLE.equals(errorCode))
        {
            Toast.makeText(this, errorCode, Toast.LENGTH_SHORT).show();

        }
    }
}
