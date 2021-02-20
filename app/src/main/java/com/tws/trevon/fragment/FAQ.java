package com.tws.trevon.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tws.trevon.R;
import com.tws.trevon.activity.FaqSinglePage;
import com.tws.trevon.adapter.FaqAdapter;
import com.tws.trevon.co.FaqCOO;
import com.tws.trevon.co.SliderCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.API;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FAQ extends AbstractFragment {

  RecyclerView faq_list_recycler_view;
  View view;
  FaqAdapter faqAdapter;
  List<FaqCOO> FaqCOOList = new ArrayList<>();


    public FAQ() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_f_a_q, container, false);
        faq_list_recycler_view = view.findViewById(R.id.faq_list_recycler_view);


        CallApi callApi = new CallApi(API.Faqs);
        processCallApi(callApi);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        faq_list_recycler_view.setLayoutManager(layoutManager);
        faqAdapter = new FaqAdapter(getActivity(), FaqCOOList);
        faq_list_recycler_view.setAdapter(faqAdapter);

        adapterClicks();

        return view;
    }

    private void adapterClicks()
    {
        faqAdapter.setEnrolledClickListener(new FaqAdapter.EnrolledClickListner() {
            @Override
            public void onParentViewClick(View view, int positionParent) {
                FaqCOO faqCOO = FaqCOOList.get(positionParent);
                String faqCOOString  = AppController.gson.toJson(faqCOO);
                Intent i = new Intent(getActivity(), FaqSinglePage.class);
                i.putExtra("faq", faqCOOString);
                startActivity(i);
            }
        });
    }


    @Override
    protected void onViewClick(View view) {

    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {
     if(API.Faqs.method.equals(callApi.networkActivity.method))
        {
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");
                if (status.equalsIgnoreCase("Success"))
                {
                    FaqCOOList.clear();
                    JSONArray onbjnew = action.getJSONArray("data");
                    for(int i = 0 ; i<onbjnew.length();i++)
                    {
                        JSONObject jsonObject1 = onbjnew.getJSONObject(i);
                        FaqCOO faqCOO = AppController.gson.fromJson(jsonObject1.toString(), FaqCOO.class);
                        FaqCOOList.add(faqCOO);
                    }

                    faqAdapter.faqCOOListList = FaqCOOList;
                    faqAdapter.notifyDataSetChanged();



                } else {
                    Toast.makeText(getActivity(), nessage, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}