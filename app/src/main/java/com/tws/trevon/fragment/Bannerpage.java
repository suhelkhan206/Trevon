package com.tws.trevon.fragment;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tws.trevon.R;
import com.tws.trevon.adapter.FeatureProductAdapter;
import com.tws.trevon.adapter.FeatureProductAdapterHorizontal;
import com.tws.trevon.co.ProductCO;
import com.tws.trevon.co.SliderCO;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.common.ItemOffsetDecoration;
import com.tws.trevon.constants.API;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Bannerpage extends AbstractFragment {

    RecyclerView deal_of_the_day_recycler_view;
    View view;
    Button remain_time_deals_of_the_day;
    List<ProductCO> productCOList = new ArrayList<>();
    FeatureProductAdapterHorizontal featureProductAdapter;
    List<SliderCO> bannerCOList = new ArrayList<>();
    ImageView banner_offer;
    NestedScrollView deals_scroll;
    LinearLayout no_deals_layout;

    public Bannerpage()
    {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.banner_page, container, false);
        no_deals_layout = view.findViewById(R.id.no_deals_layout);
        deals_scroll = view.findViewById(R.id.deals_scroll);
        deal_of_the_day_recycler_view = view.findViewById(R.id.deal_of_the_day_recycler_view);
        remain_time_deals_of_the_day = view.findViewById(R.id.remain_time_deals_of_the_day);
        banner_offer = view.findViewById(R.id.banner_offer);
        deal_of_the_day_recycler_view.setHasFixedSize(true);
        deal_of_the_day_recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        deal_of_the_day_recycler_view.addItemDecoration(itemDecoration);

        CallApi callApi = new CallApi(API.dealsOfTheDay);
        callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
        processCallApi(callApi);


        CallApi callApib = new CallApi(API.banner);
        callApib.processInBackground = true;
        processCallApi(callApib);



        return view;
    }


    @Override
    protected void onViewClick(View view) {

    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {
        if(API.dealsOfTheDay.method.equals(callApi.networkActivity.method))
        { // new
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");
                if (status.equalsIgnoreCase("Success"))
                {
                    productCOList.clear();
                    JSONArray onbjnew = action.getJSONArray("data");
                    for(int i = 0 ; i<onbjnew.length();i++)
                    {
                        JSONObject jsonObject1 = onbjnew.getJSONObject(i);
                        ProductCO productCO = AppController.gson.fromJson(jsonObject1.toString(), ProductCO.class);
                        productCOList.add(productCO);
                    }
                    if(productCOList.size() > 0)
                    {
                        no_deals_layout.setVisibility(View.GONE);
                        deals_scroll.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        no_deals_layout.setVisibility(View.VISIBLE);
                        deals_scroll.setVisibility(View.GONE);
                    }

                    featureProductAdapter = new FeatureProductAdapterHorizontal(getActivity(), productCOList);
                    deal_of_the_day_recycler_view.setAdapter(featureProductAdapter);
                    featureProductAdapter.notifyDataSetChanged();
                    adapterClick();
                } else {
                    no_deals_layout.setVisibility(View.VISIBLE);
                    deals_scroll.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), nessage, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                no_deals_layout.setVisibility(View.VISIBLE);
                deals_scroll.setVisibility(View.GONE);
            }
        }

        else if(API.banner.method.equals(callApi.networkActivity.method))
        {
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");
                if (status.equalsIgnoreCase("Success"))
                {
                    bannerCOList.clear();
                    JSONArray onbjnew = action.getJSONArray("data");
                    for(int i = 0 ; i<onbjnew.length();i++)
                    {
                        JSONObject jsonObject1 = onbjnew.getJSONObject(i);
                        SliderCO sliderCO = AppController.gson.fromJson(jsonObject1.toString(), SliderCO.class);
                        bannerCOList.add(sliderCO);
                    }

                    Glide.with(AppController.getInstance().getApplicationContext())
                            .load(bannerCOList.get(0).image)
                            .into(banner_offer);


                } else {
                    Toast.makeText(getActivity(), nessage, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void adapterClick() {
        featureProductAdapter.setEnrolledClickListener(new FeatureProductAdapter.EnrolledClickListner() {
            @Override
            public void onViewClick(View view, int positionParent)
            {

                CallApi callApi = new CallApi(API.add_to_wishList);
                callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
                callApi.addReqParams("product_id", productCOList.get(positionParent).id);
                callApi.processInBackground= true;
                processCallApi(callApi);
            }
        });
    }

}