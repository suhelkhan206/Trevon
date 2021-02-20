package com.tws.trevon.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.tws.trevon.R;
import com.tws.trevon.activity.CustomOrderCreation;
import com.tws.trevon.activity.MainActivity;
import com.tws.trevon.activity.ProductViewAll;
import com.tws.trevon.adapter.FeatureProductAdapter;
import com.tws.trevon.adapter.FeatureProductAdapter2;
import com.tws.trevon.adapter.ShopByCategoryAdapter;
import com.tws.trevon.adapter.SliderAdapterCustom;
import com.tws.trevon.co.CategoryCO;
import com.tws.trevon.co.ProductCO;
import com.tws.trevon.co.SliderCO;
import com.tws.trevon.co.SubminiCategoryCO;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.common.ConstantVariable;
import com.tws.trevon.constants.API;
import com.tws.trevon.customlayout.EnchantedViewPager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;

public class Home extends AbstractFragment
{
    SliderView mViewPager;
    SliderAdapterCustom mAdapter;
    List<SliderCO> sliderCOList = new ArrayList<>();
    List<SliderCO> bannerCOList1 = new ArrayList<>();
    List<SliderCO> bannerCOList2 = new ArrayList<>();
    CircleIndicator circleIndicator;
    Button most_selling_product_button,view_all_deals_button,view_all_shop_by_category, view_all_featured_button;
    int currentCount = 0;
    RecyclerView most_selling_product_recycler_view,home_bottom_product_recycler_view,shop_by_category_recycler_view,featured_product_recycler_view;
    RelativeLayout most_selling_product_layout,popular_product_layout,shop_by_cat_layout, featured_product_layout;
    ImageView banner_1;
    View rootView;
    List<CategoryCO> categoryCOList =new ArrayList<>();
    List<SubminiCategoryCO> subminiCategoryCOListFeatureCat = new ArrayList<>();
    List<ProductCO> productCOList = new ArrayList<>();
    List<ProductCO> popuarProductCOList = new ArrayList<>();
    ShopByCategoryAdapter shopByCategoryAdapter;
    FeatureProductAdapter popularProductAdapter;
    FeatureProductAdapter featureProductAdapter;
    FeatureProductAdapter2 featureProductAdapter2;
    List<ProductCO> mostSellingProductCOList = new ArrayList<>();
    ImageView banner_first;
    SwipeRefreshLayout mySwipeRefreshLayout;
    ImageView order_your_design_banner;

    public Home()
    {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed())
        { // fragment is visible and have created
            loadData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        if(getUserVisibleHint())
        {
            // fragment is visible
            loadData();
        }

        return rootView;
    }

    public void loadData()
    {
        // Hashmap for ListView
        mViewPager = rootView.findViewById(R.id.viewPager);
        circleIndicator = rootView.findViewById(R.id.indicator_unselected_background);
        view_all_shop_by_category = rootView.findViewById(R.id.view_all_shop_by_category);
        banner_first = rootView.findViewById(R.id.banner_first);
        view_all_featured_button = rootView.findViewById(R.id.view_all_featured_button);
        view_all_deals_button = rootView.findViewById(R.id.view_all_deals_button);
        shop_by_category_recycler_view = (RecyclerView) rootView.findViewById(R.id.shop_by_category_recycler_view);
        most_selling_product_recycler_view = rootView.findViewById(R.id.most_selling_product_recycler_view);
        most_selling_product_layout = rootView.findViewById(R.id.most_selling_product_layout);
        featured_product_recycler_view = (RecyclerView) rootView.findViewById(R.id.featured_product_recycler_view);
        home_bottom_product_recycler_view = (RecyclerView) rootView.findViewById(R.id.home_bottom_product_recycler_view);
        banner_1 = rootView.findViewById(R.id.banner_1);
        shop_by_cat_layout= rootView.findViewById(R.id.shop_by_cat_layout);
        featured_product_layout= rootView.findViewById(R.id.featured_product_layout);
        popular_product_layout= rootView.findViewById(R.id.popular_product_layout);
        most_selling_product_button = rootView.findViewById(R.id.most_selling_product_button);
        mySwipeRefreshLayout = rootView.findViewById(R.id.swipeContainer);
        order_your_design_banner = rootView.findViewById(R.id.order_your_design_banner);
        // mViewPager.useScale();
      //  mViewPager.removeAlpha();
        order_your_design_banner.setOnClickListener(this);
        view_all_featured_button.setOnClickListener(this);
        view_all_deals_button.setOnClickListener(this);
        view_all_shop_by_category.setOnClickListener(this);
        most_selling_product_button.setOnClickListener(this);

        if(sliderCOList.size() == 0)
        {
            CallApi callApi = new CallApi(API.home_page_sliders);
            //callApi.addReqParams("type","SLIDER");
            callApi.processInBackground = true;
            processCallApi(callApi);
        }

        if(categoryCOList.size() == 0)
        {
            CallApi callApi = new CallApi(API.main_category_list);
            //  callApi.processInBackground = true;
            processCallApi(callApi);
        }

        CallApi callApiBanner = new CallApi(API.home_page_baner_1);
        //  callApi.processInBackground = true;
        processCallApi(callApiBanner);


        if(productCOList.size() == 0)
        {
            CallApi callApi = new CallApi(API.featured_product_list);
            callApi.addReqParams("user_id",UserCO.getUserCOFromDB().id);
            //callApi.processInBackground = true;
            processCallApi(callApi);
        }

        CallApi callApiBanner2 = new CallApi(API.home_page_baner_2);
        //  callApi.processInBackground = true;
        processCallApi(callApiBanner2);


        if(popuarProductCOList.size() == 0)
        {
            CallApi callApi = new CallApi(API.deal_of_the_day_products);
            callApi.addReqParams("user_id",UserCO.getUserCOFromDB().id);
            callApi.processInBackground = true;
            processCallApi(callApi);
        }

        if(mostSellingProductCOList.size() == 0)
        {
            CallApi callApi = new CallApi(API.most_selling_product_list);
            callApi.addReqParams("user_id",UserCO.getUserCOFromDB().id);
            callApi.processInBackground = true;
            processCallApi(callApi);
        }



        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh()
                    {
                        callApi();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

       // mViewPager.useScale();
       // mViewPager.removeAlpha();
    }

    private void callApi() {

        CallApi callApi = new CallApi(API.home_page_sliders);
        //callApi.addReqParams("type","SLIDER");
        callApi.processInBackground = true;
        processCallApi(callApi);


        CallApi callApi1 = new CallApi(API.main_category_list);
        callApi.processInBackground = true;
        processCallApi(callApi1);


        CallApi callApiBanner = new CallApi(API.home_page_baner_1);
        callApi.processInBackground = true;
        processCallApi(callApiBanner);


        CallApi callApi2 = new CallApi(API.featured_product_list);
        callApi2.addReqParams("user_id", UserCO.getUserCOFromDB().id);
        //callApi.processInBackground = true;
        processCallApi(callApi2);


        CallApi callApiBanner2 = new CallApi(API.home_page_baner_2);
        callApi.processInBackground = true;
        processCallApi(callApiBanner2);


        CallApi callApi3 = new CallApi(API.deal_of_the_day_products);
        callApi3.addReqParams("user_id", UserCO.getUserCOFromDB().id);
        callApi3.processInBackground = true;
        processCallApi(callApi3);

        CallApi callApi4 = new CallApi(API.most_selling_product_list);
        callApi4.addReqParams("user_id", UserCO.getUserCOFromDB().id);
        callApi4.processInBackground = true;
        processCallApi(callApi4);

    }

    @Override
    protected void onViewClick(View view)
    {
        switch (view.getId()) {
            case R.id.view_all_featured_button:
               /* Intent i =new Intent(getActivity(), ProductViewAll.class);
                i.putExtra("is_cat", "N");
                startActivity(i);*/
                break;

            case R.id.view_all_deals_button:
               /* Intent i1 =new Intent(getActivity(), ProductViewAll.class);
                i1.putExtra("is_cat", "N");
                startActivity(i1);*/
                break;
            case R.id.view_all_shop_by_category:
                ((MainActivity) requireActivity()).highLightNavigationBottom();
                CategoryFragment categoryFragment = new CategoryFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(Home.this);
                fragmentTransaction.add(R.id.fragment_container, categoryFragment, "category");
                fragmentTransaction.commit();
                break;

            case R.id.most_selling_product_button:
                ConstantVariable.CATEGORY_TITLE = "Most Selling";
                Intent im =new Intent(getActivity(), ProductViewAll.class);
                im.putExtra("category_id", "");
                im.putExtra("type", "most_selling_product_list");
                startActivity(im);
                break;

            case R.id.order_your_design_banner:
                Intent io =new Intent(getActivity(), CustomOrderCreation.class);
                startActivity(io);
                break;




        }


    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi)
    {
        if(API.home_page_sliders.method.equals(callApi.networkActivity.method))
        {
            try
            {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");
                if (status.equalsIgnoreCase("Success"))
                {
                    sliderCOList.clear();
                    JSONArray onbjnew = action.getJSONArray("data");
                    for(int i = 0 ; i<onbjnew.length();i++)
                    {
                        JSONObject jsonObject1 = onbjnew.getJSONObject(i);
                        SliderCO sliderCO = AppController.gson.fromJson(jsonObject1.toString(), SliderCO.class);
                        sliderCOList.add(sliderCO);
                    }
                    setHomeVideo();
                }
                else
                {
                    Toast.makeText(getActivity(), nessage, Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if(API.main_category_list.method.equals(callApi.networkActivity.method))
        {
            try
            {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");
                if (status.equalsIgnoreCase("Success"))
                {
                    categoryCOList.clear();
                    JSONArray onbjnew = action.getJSONArray("data");
                    for(int i = 0 ; i<onbjnew.length();i++)
                    {
                        JSONObject jsonObject1 = onbjnew.getJSONObject(i);
                        CategoryCO categoryCO = AppController.gson.fromJson(jsonObject1.toString(), CategoryCO.class);
                        categoryCOList.add(categoryCO);
                    }
                    if (getActivity() != null)
                    {
                        shop_by_category_recycler_view.setHasFixedSize(true);
                        shop_by_category_recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 4));

                        shopByCategoryAdapter = new ShopByCategoryAdapter(getActivity(), categoryCOList);
                        shop_by_category_recycler_view.setAdapter(shopByCategoryAdapter);

                      /*  RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        shop_by_category_recycler_view.setLayoutManager(layoutManager);
                        shopByCategoryAdapter = new ShopByCategoryAdapter(getActivity(), categoryCOList);
                        shop_by_category_recycler_view.setAdapter(shopByCategoryAdapter);*/
                        shopByCategoryAdapter.categoryCOList = categoryCOList;
                        shopByCategoryAdapter.notifyDataSetChanged();
                        if(categoryCOList.size()> 0)
                        {
                          //  shop_by_cat_layout.setVisibility(View.VISIBLE);
                        }else
                        {
                          //  shop_by_cat_layout.setVisibility(View.GONE);
                        }

                        shopByCategoryAdapter.setAddressClickListener(new ShopByCategoryAdapter.AddressClickListner() {
                            @Override
                            public void onrootCLick(View view, int position) {
                                ConstantVariable.CATEGORY_TITLE = categoryCOList.get(position).title;
                                CategoryCO categoryCO = categoryCOList.get(position);
                                Intent i1 =new Intent(getActivity(), ProductViewAll.class);
                                i1.putExtra("category_id", categoryCO.id);
                                i1.putExtra("type", "main_category_product_list");
                                startActivity(i1);

                            }

                            @Override
                            public void onAddressRootCLick(View view, int position) {

                            }
                        });

                    }
                } else {
                    if(categoryCOList.size()> 0)
                    {//
                      //  shop_by_cat_layout.setVisibility(View.VISIBLE);
                    }else
                    {
                     //   shop_by_cat_layout.setVisibility(View.GONE);
                    }
                    Toast.makeText(getActivity(), nessage, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                if(categoryCOList.size()> 0)
                {
                   // shop_by_cat_layout.setVisibility(View.VISIBLE);
                }else
                {
                  //  shop_by_cat_layout.setVisibility(View.GONE);
                }
                e.printStackTrace();
            }
        }
        else if(API.home_page_baner_1.method.equals(callApi.networkActivity.method))
        {
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");
                if (status.equalsIgnoreCase("Success"))
                {
                    bannerCOList1.clear();
                    JSONArray onbjnew = action.getJSONArray("data");
                    for(int i = 0 ; i<onbjnew.length();i++)
                    {
                        JSONObject jsonObject1 = onbjnew.getJSONObject(i);
                        SliderCO sliderCO = AppController.gson.fromJson(jsonObject1.toString(), SliderCO.class);
                        bannerCOList1.add(sliderCO);
                    }

                    Glide.with(AppController.getInstance().getApplicationContext())
                            .load(bannerCOList1.get(0).image)
                            .dontAnimate()
                            .into(banner_first);

                    banner_first.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            ConstantVariable.CATEGORY_TITLE = bannerCOList1.get(0).title;
                            SliderCO sliderCO = bannerCOList1.get(0);
                            Intent i1 =new Intent(getActivity(), ProductViewAll.class);
                            i1.putExtra("category_id", sliderCO.category_id);
                            i1.putExtra("type", "home_page_baner_1");
                            startActivity(i1);
                        }
                    });



                } else {
                    Toast.makeText(getActivity(), nessage, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else if(API.home_page_baner_2.method.equals(callApi.networkActivity.method))
        {
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");
                if (status.equalsIgnoreCase("Success"))
                {
                    bannerCOList2.clear();
                    JSONArray onbjnew = action.getJSONArray("data");
                    for(int i = 0 ; i<onbjnew.length();i++)
                    {
                        JSONObject jsonObject1 = onbjnew.getJSONObject(i);
                        SliderCO sliderCO = AppController.gson.fromJson(jsonObject1.toString(), SliderCO.class);
                        bannerCOList2.add(sliderCO);
                    }

                    Glide.with(AppController.getInstance().getApplicationContext())
                            .load(bannerCOList2.get(0).image)
                            .dontAnimate()
                            .into(banner_1);

                    banner_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            ConstantVariable.CATEGORY_TITLE = bannerCOList2.get(0).title;
                            SliderCO sliderCO = bannerCOList2.get(0);
                            Intent i1 =new Intent(getActivity(), ProductViewAll.class);
                            i1.putExtra("category_id", sliderCO.category_id);
                            i1.putExtra("type", "home_page_baner_2");
                            startActivity(i1);
                        }
                    });


                } else {
                    Toast.makeText(getActivity(), nessage, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(API.featured_product_list.method.equals(callApi.networkActivity.method))
        {
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
                    if (getActivity() != null)
                    {

                        featureProductAdapter = new FeatureProductAdapter(getActivity(), productCOList);
                        GridLayoutManager topLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false);
                        featured_product_recycler_view.setHasFixedSize(true);
                        featured_product_recycler_view.setLayoutManager(topLayoutManager);
                        featured_product_recycler_view.setItemAnimator(new DefaultItemAnimator());
                        featured_product_recycler_view.setAdapter(featureProductAdapter);
                          adapterClick();
                        if(productCOList.size()> 0)
                        {
                            featured_product_layout.setVisibility(View.VISIBLE);
                        }else
                        {
                            featured_product_layout.setVisibility(View.GONE);
                        }

                    }
                } else {
                    if(productCOList.size()> 0)
                    {
                        featured_product_layout.setVisibility(View.VISIBLE);
                    }else
                    {
                        featured_product_layout.setVisibility(View.GONE);
                    }
                    Toast.makeText(getActivity(), nessage, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                if(productCOList.size()> 0)
                {
                    featured_product_layout.setVisibility(View.VISIBLE);
                }else
                {
                    featured_product_layout.setVisibility(View.GONE);
                }
                e.printStackTrace();
            }
        }

        else if(API.most_selling_product_list.method.equals(callApi.networkActivity.method))
        {
            try
            {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");
                if (status.equalsIgnoreCase("Success"))
                {
                    mostSellingProductCOList.clear();
                    JSONArray onbjnew = action.getJSONArray("data");
                    for(int i = 0 ; i<onbjnew.length();i++)
                    {
                        JSONObject jsonObject1 = onbjnew.getJSONObject(i);
                        ProductCO productCO = AppController.gson.fromJson(jsonObject1.toString(), ProductCO.class);
                        mostSellingProductCOList.add(productCO);
                    }
                    if (getActivity() != null)
                    {
                        featureProductAdapter2 = new FeatureProductAdapter2(getActivity(), mostSellingProductCOList);
                        GridLayoutManager topLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false);
                        most_selling_product_recycler_view.setHasFixedSize(true);
                        most_selling_product_recycler_view.setLayoutManager(topLayoutManager);
                        most_selling_product_recycler_view.setItemAnimator(new DefaultItemAnimator());
                        most_selling_product_recycler_view.setAdapter(featureProductAdapter2);
                        featureProductAdapter2.setEnrolledClickListener(new FeatureProductAdapter2.EnrolledClickListner() {
                            @Override
                            public void onViewClick(View view, int positionParent)
                            {


                                if(mostSellingProductCOList.get(positionParent).is_wished.equals("0"))
                                {
                                    mostSellingProductCOList.get(positionParent).is_wished = "1";
                                    CallApi callApi = new CallApi(API.add_to_wishList);
                                    callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
                                    callApi.addReqParams("product_id", mostSellingProductCOList.get(positionParent).id);
                                    callApi.processInBackground= true;
                                    processCallApi(callApi);
                                }
                                else
                                {
                                    mostSellingProductCOList.get(positionParent).is_wished = "0";
                                    CallApi callApi = new CallApi(API.remove_wishList);
                                    callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
                                    callApi.addReqParams("product_id", mostSellingProductCOList.get(positionParent).id);
                                    callApi.processInBackground= true;
                                    processCallApi(callApi);
                                }

                                featureProductAdapter2.notifyDataSetChanged();

                            }
                        });

                        if(mostSellingProductCOList.size()> 0)
                        {
                            most_selling_product_layout.setVisibility(View.VISIBLE);
                        }else
                        {
                            most_selling_product_layout.setVisibility(View.GONE);
                        }

                    }
                } else {
                    if(mostSellingProductCOList.size()> 0)
                    {
                        most_selling_product_layout.setVisibility(View.VISIBLE);
                    }else
                    {
                        most_selling_product_layout.setVisibility(View.GONE);
                    }
                    Toast.makeText(getActivity(), nessage, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                if(mostSellingProductCOList.size()> 0)
                {
                    most_selling_product_layout.setVisibility(View.VISIBLE);
                }else
                {
                    most_selling_product_layout.setVisibility(View.GONE);
                }
                e.printStackTrace();
            }
        }

        else if(API.deal_of_the_day_products.method.equals(callApi.networkActivity.method))
        {
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");
                if (status.equalsIgnoreCase("Success"))
                {
                    popuarProductCOList.clear();
                    JSONArray onbjnew = action.getJSONArray("data");
                    for(int i = 0 ; i<onbjnew.length();i++)
                    {
                        JSONObject jsonObject1 = onbjnew.getJSONObject(i);
                        ProductCO productCO = AppController.gson.fromJson(jsonObject1.toString(), ProductCO.class);
                        popuarProductCOList.add(productCO);
                    }
                    if (getActivity() != null)
                    {
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        home_bottom_product_recycler_view.setLayoutManager(layoutManager);
                        popularProductAdapter = new FeatureProductAdapter(getActivity(), popuarProductCOList);
                        home_bottom_product_recycler_view.setAdapter(popularProductAdapter);
                        popularProductAdapter.productCOList = popuarProductCOList;
                        popularProductAdapter.notifyDataSetChanged();

                        popularProductAdapter.setEnrolledClickListener(new FeatureProductAdapter.EnrolledClickListner() {
                            @Override
                            public void onViewClick(View view, int positionParent)
                            {


                                if(popuarProductCOList.get(positionParent).is_wished.equals("0"))
                                {
                                    popuarProductCOList.get(positionParent).is_wished = "1";
                                    CallApi callApi = new CallApi(API.add_to_wishList);
                                    callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
                                    callApi.addReqParams("product_id", popuarProductCOList.get(positionParent).id);
                                    callApi.processInBackground= true;
                                    processCallApi(callApi);
                                }
                                else
                                {
                                    popuarProductCOList.get(positionParent).is_wished = "0";
                                    CallApi callApi = new CallApi(API.remove_wishList);
                                    callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
                                    callApi.addReqParams("product_id", popuarProductCOList.get(positionParent).id);
                                    callApi.processInBackground= true;
                                    processCallApi(callApi);
                                }

                                popularProductAdapter.notifyDataSetChanged();

                            }
                        });



                        if(popuarProductCOList.size()> 0)
                        {
                            popular_product_layout.setVisibility(View.VISIBLE);
                        }else
                        {
                            popular_product_layout.setVisibility(View.GONE);
                        }

                    }
                } else {
                    if(popuarProductCOList.size()> 0)
                    {
                        popular_product_layout.setVisibility(View.VISIBLE);
                    }else
                    {
                        popular_product_layout.setVisibility(View.GONE);
                    }
                    Toast.makeText(getActivity(), nessage, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                if(popuarProductCOList.size()> 0)
                {
                    popular_product_layout.setVisibility(View.VISIBLE);
                }else
                {
                    popular_product_layout.setVisibility(View.GONE);
                }
                e.printStackTrace();
            }
        }
    }

    private void adapterClick()
    {
        featureProductAdapter.setEnrolledClickListener(new FeatureProductAdapter.EnrolledClickListner() {
            @Override
            public void onViewClick(View view, int positionParent)
            {


                if(productCOList.get(positionParent).is_wished.equals("0"))
                {
                    productCOList.get(positionParent).is_wished = "1";
                    CallApi callApi = new CallApi(API.add_to_wishList);
                    callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
                    callApi.addReqParams("product_id", productCOList.get(positionParent).id);
                    callApi.processInBackground= true;
                    processCallApi(callApi);
                }
                else
                {
                    productCOList.get(positionParent).is_wished = "0";
                    CallApi callApi = new CallApi(API.remove_wishList);
                    callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
                    callApi.addReqParams("product_id", productCOList.get(positionParent).id);
                    callApi.processInBackground= true;
                    processCallApi(callApi);
                }

                featureProductAdapter.notifyDataSetChanged();

            }
        });
    }

   /* private void autoPlay(final ViewPager viewPager) {

        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mAdapter != null && viewPager.getAdapter().getCount() > 0) {
                        int position = currentCount % mAdapter.getCount();
                        currentCount++;
                        viewPager.setCurrentItem(position);
                        autoPlay(viewPager);
                    }
                } catch (Exception e) {
                    Log.e("TAG", "auto scroll pager error.", e);
                }
            }
        }, 10000);
    }*/

    private void setHomeVideo()
    {
        if (getActivity() != null) {
            if (!sliderCOList.isEmpty())
            {
                mAdapter = new SliderAdapterCustom(getActivity(),sliderCOList);
                mViewPager.setSliderAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

                mViewPager.setClipToPadding(false);
                mViewPager.setPadding(30, 0, 30, 0);
             //   mViewPager.setPageMargin(-16);
                mViewPager.setOffscreenPageLimit(mAdapter.getCount());

                mViewPager.setIndicatorAnimation(IndicatorAnimationType.WORM);
                mViewPager.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                mViewPager.startAutoCycle();

               // circleIndicator.setViewPager(mViewPager);
               // autoPlay(mViewPager);
            }
            if (sliderCOList.size() == 0)
            {
                mViewPager.setVisibility(View.GONE);
            }
            else
            {
                mViewPager.setVisibility(View.VISIBLE);
            }


          //  txt_latest_video_no.setText(String.valueOf(sliderCOList.size()) + "\u0020" + getResources().getString(R.string.total_video));
          //  txt_all_video_no.setText(String.valueOf(sliderCOList.size()) + "\u0020" + getResources().getString(R.string.total_video));
          //  txt_cat_video_no.setText(String.valueOf(sliderCOList.size()) + "\u0020" + getResources().getString(R.string.total_category));

            /*if (getActivity() != null)
            {
                homeLatestAdapter = new HomeLatestAdapter(getActivity(), sliderCOList);
                recyclerViewLatestVideo.setAdapter(homeLatestAdapter);
            }*/

        }

    }

    /*private class CustomViewPagerAdapter extends PagerAdapter {
        private LayoutInflater inflater;

        private CustomViewPagerAdapter()
        {
            // TODO Auto-generated constructor stub
            inflater = requireActivity().getLayoutInflater();
        }

        @Override
        public int getCount()
        {
            return sliderCOList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View imageLayout = inflater.inflate(R.layout.row_slider_item, container, false);
            assert imageLayout != null;

            ImageView image = imageLayout.findViewById(R.id.image);

            Glide.with((MainActivity) requireActivity())
                    .load(sliderCOList.get(position).image)
                    .placeholder(R.drawable.error_images)
                    // .centerCrop().crossFade()
                    //.diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .dontAnimate()
                    .into(image);


            imageLayout.setTag(EnchantedViewPager.ENCHANTED_VIEWPAGER_POSITION + position);
            image.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    SliderCO sliderCO=  sliderCOList.get(position);
                    ConstantVariable.CATEGORY_TITLE = sliderCO.title;
                    Intent i1 =new Intent(getActivity(), ProductViewAll.class);
                    i1.putExtra("category_id", sliderCO.category_id);
                    i1.putExtra("type", "home_page_sliders");
                    startActivity(i1);
                   // List<ImageCO> imageCOList = new ArrayList<>();
                   *//* for(int i = 0; i<sliderCOList.size(); i++)
                    {
                        ImageCO imageCO = new ImageCO();
                        imageCO.url = sliderCOList.get(i).image;
                        imageCO.type = "image";
                        imageCOList.add(imageCO);
                    }

                       *//**//* if(imageCO.type.toLowerCase().contains("pdf"))
                        {
                            //AppUtilities.showPdfFromUrl(EditBuisnessProfile.this, imageCO.url);
                        }
                        else if(imageCO.type.toLowerCase().contains("image"))
                        {*//**//*

                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("images", new ArrayList(imageCOList));
                            bundle.putString("isPdfShow","no");
                            bundle.putInt("position", 0);
                            bundle.putInt("type",2);

                            FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                            FragmentImageSlider newFragment = FragmentImageSlider.newInstance();
                            newFragment.setArguments(bundle);
                            newFragment.show(ft, "slideshow");*//*


                }
            });

            container.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            (container).removeView((View) object);
        }
    }*/

}