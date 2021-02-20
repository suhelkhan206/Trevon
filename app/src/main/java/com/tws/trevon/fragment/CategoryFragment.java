package com.tws.trevon.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tws.trevon.R;
import com.tws.trevon.adapter.CategroyParentAdapter;
import com.tws.trevon.co.CategoryCO;
import com.tws.trevon.co.ImageCO;
import com.tws.trevon.co.SliderCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.API;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class CategoryFragment extends AbstractFragment
{
    View view;
    RecyclerView category_page_recycler_view;
    SwipeRefreshLayout mySwipeRefreshLayout;
    List<CategoryCO> categoryCOList = new ArrayList<>();
    CategroyParentAdapter categroyParentAdapter;
    ImageView banner_category;
    List<SliderCO> bannerCOList = new ArrayList<>();

    public CategoryFragment()
    {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_category, container, false);
        category_page_recycler_view = view.findViewById(R.id.category_page_recycler_view);
        banner_category = view.findViewById(R.id.banner_category);
        mySwipeRefreshLayout = view.findViewById(R.id.swipeContainer);
        if(categoryCOList.size() == 0)
        {
            CallApi callApi = new CallApi(API.all_level_category_list);
            //  callApi.processInBackground = true;
            processCallApi(callApi);
        }

        CallApi callApi = new CallApi(API.category_baner);
        callApi.processInBackground = true;
        processCallApi(callApi);
        banner_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                List<ImageCO> imageCOList = new ArrayList<>();
                for(int i = 0; i<bannerCOList.size(); i++)
                {
                    ImageCO imageCO = new ImageCO();
                    imageCO.url = bannerCOList.get(i).image;
                    imageCO.type = "image";
                    imageCOList.add(imageCO);
                }


                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("images", new ArrayList(imageCOList));
                bundle.putString("isPdfShow","no");
                bundle.putInt("position", 0);
                bundle.putInt("type",2);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentImageSlider newFragment = FragmentImageSlider.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }
        });

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh()
                    {
                        CallApi callApi = new CallApi(API.category_baner);
                        callApi.processInBackground = true;
                        processCallApi(callApi);

                        CallApi callApi1 = new CallApi(API.all_level_category_list);
                        callApi.processInBackground = true;
                        processCallApi(callApi1);

                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );


        return view;

    }
    private void adapterCLick()
    {
        categroyParentAdapter.setEnrolledClickListener(new CategroyParentAdapter.EnrolledClickListner() {
            @Override
            public void onParentViewClick(View view, int position) {
                CategoryCO subCategoryCO = categoryCOList.get(position);
                if(subCategoryCO.subcats.size()>0)
                {
                    if(subCategoryCO.isVisible)
                    {
                        subCategoryCO.isVisible = false;
                    }
                    else
                    {
                        subCategoryCO.isVisible = true;
                    }
                }

                categroyParentAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onViewClick(View view)
    {

    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {
   if(API.all_level_category_list.method.equals(callApi.networkActivity.method))
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
                    JSONArray jsonObject1= action.getJSONArray("data");
                    for(int i = 0 ; i<jsonObject1.length();i++)
                    {
                        JSONObject d = jsonObject1.getJSONObject(i);
                        CategoryCO categoryCO = AppController.gson.fromJson(d.toString(), CategoryCO.class);
                        categoryCOList.add(categoryCO);
                    }


                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    category_page_recycler_view.setLayoutManager(layoutManager);
                    categroyParentAdapter = new CategroyParentAdapter(getActivity(), categoryCOList);
                    category_page_recycler_view.setAdapter(categroyParentAdapter);
                    categroyParentAdapter.categoryCOList = categoryCOList;
                    categroyParentAdapter.notifyDataSetChanged();
                    adapterCLick();
                } else {

                    Toast.makeText(getActivity(), nessage, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }

   else if(API.category_baner.method.equals(callApi.networkActivity.method))
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
                       .dontAnimate()
                       .into(banner_category);


           } else {
               Toast.makeText(getActivity(), nessage, Toast.LENGTH_SHORT).show();
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
    }
}