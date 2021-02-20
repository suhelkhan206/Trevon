package com.tws.trevon.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tws.trevon.R;
import com.tws.trevon.co.FilterCO;
import com.tws.trevon.co.ServerFillterCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.IConstants;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.annotation.Nullable;

public class BottomFilter extends AbstractBottomSheetFragment
{
    LinearLayout filter_types_list, filter_type_values_list,clear_filter, apply_filter,close_dialog;
    View view;
    OnMyDialogResult mDialogResult;
    ArrayList<FilterCO> filterCOList =new ArrayList<>();
    ArrayList<ServerFillterCO> serverFillterCOList = new ArrayList<>();

    // FilterFirstAdapter filterFirstAdapter;
    // RecyclerView filter_types_list_recycler;//filter_value_list_recycler
    // FilterValueAdapter filterValueAdapter;
    // ArrayList<FilterValueCO> filterValueList =new ArrayList<>();

    @Override
    protected String getTAG()
    {
        return null;
    }

    @Override
    protected void onViewClick(View view)
    {
        switch (view.getId())
        {
            case R.id.apply_filter:
                if( mDialogResult != null )
                {
                    mDialogResult.finish(serverFillterCOList, filterCOList);
                }
                serverFillterCOList.clear();
                // filterCOList.clear();
              /*  for(int i=0;i<filterCOList.size(); i++)
                {
                    for(int j = 0; j<filterCOList.get(i).FilterValueList.size();j++)
                    {
                        filterCOList.get(i).FilterValueList.get(j).isChecked = false;
                    }
                }*/
                String filterCOListString1 = AppController.gson.toJson(filterCOList);
                AppUtilities.writeToPref(IConstants.FILTER_LIST,filterCOListString1);
                //  filterFirstAdapter.filterCOList = filterCOList;
                // filterFirstAdapter.notifyDataSetChanged();
                BottomFilter.this.dismiss();
                break;

            case R.id.close_dialog:
                String filterCOListString = AppController.gson.toJson(filterCOList);
                AppUtilities.writeToPref(IConstants.FILTER_LIST,filterCOListString);
                BottomFilter.this.dismiss();
                break;


            case R.id.clear_filter:

                serverFillterCOList.clear();
                // filterCOList.clear();
                for(int i=0;i<filterCOList.size(); i++)
                {
                    for(int j = 0; j<filterCOList.get(i).FilterValueList.size();j++)
                    {
                        //filterCOList.get(i).FilterValueList.get(j).isChecked = false;
                    }

                }
                // filterFirstAdapter.filterCOList = filterCOList;
                // filterFirstAdapter.notifyDataSetChanged();
                //populatefilterValue(0);
                //BottomFilter.this.dismiss();
                break;



        }
    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi)
    {

    }

    public BottomFilter()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bottom_filter, container, false);
        apply_filter=view.findViewById(R.id.apply_filter);
        filter_types_list = view.findViewById(R.id.filter_types_list);
        filter_type_values_list = view.findViewById(R.id.filter_type_values_list);
        clear_filter = view.findViewById(R.id.clear_filter);
        close_dialog = view.findViewById(R.id.close_dialog);
        //filter_types_list_recycler = view.findViewById(R.id.filter_types_list_recycler);
        //filter_value_list_recycler = view.findViewById(R.id.filter_value_list_recycler);
        apply_filter.setOnClickListener(this);
        close_dialog.setOnClickListener(this);
        clear_filter.setOnClickListener(this);

        populateFilter();

       /* filterFirstAdapter = new FilterFirstAdapter(getActivity(),filterCOList);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
        filter_types_list_recycler.setNestedScrollingEnabled(true);
        filter_types_list_recycler.setHasFixedSize(true);
        filter_types_list_recycler.setNestedScrollingEnabled(false);
        filter_types_list_recycler.setLayoutManager(mLayoutManager2);
        filter_types_list_recycler.setItemAnimator(new DefaultItemAnimator());
        filter_types_list_recycler.setAdapter(filterFirstAdapter);*/

        //  populatefilterValue(0);

   /*     filterValueList.addAll(filterCOList.get(0).FilterValueList);
        //FilterValue
        filterValueAdapter = new FilterValueAdapter(getActivity(),filterValueList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        filter_value_list_recycler.setNestedScrollingEnabled(true);
        filter_value_list_recycler.setHasFixedSize(true);
        filter_value_list_recycler.setLayoutManager(mLayoutManager);
        filter_value_list_recycler.setItemAnimator(new DefaultItemAnimator());
        filter_value_list_recycler.setAdapter(filterValueAdapter);*/

        adapterClick();

        return view;
    }

    public void adapterClick(){
      /*  filterFirstAdapter.setClickListener(new FilterFirstAdapter.ClickListner() {
            @Override
            public void onSelectFilter(View view, int position) {
               populatefilterValue(position);
            }
        });*/

      /*  filterValueAdapter.setClickListener(new FilterValueAdapter.ClickListner() {
            @Override
            public void onSelectFilter(View view, int position, boolean isChecked)
            {

            }
        });*/
    }

   /* private void populatefilterValue(final int position)
    {
        filter_type_values_list.removeAllViews();
        final ServerFillterCO serverFillterCO =new ServerFillterCO();
        serverFillterCO.filter_id = filterCOList.get(position).filter_id;
        serverFillterCOList.add(serverFillterCO);

        for(int i = 0; i <filterCOList.get(position).FilterValueList.size(); i++)//filterCOList.get(position).FilterValueList.size()
        {
            final int finalI = i;
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View childView = layoutInflater.inflate(R.layout.filter_brand_value_viiew_layout, null);

            CheckBox filter_check = childView.findViewById(R.id.filter_check);

            filter_check.setText(filterCOList.get(position).FilterValueList.get(i).title);

            if(filterCOList.get(position).FilterValueList.get(i).isChecked)
            {
                filter_check.setChecked(true);
            }
            else
            {
                filter_check.setChecked(false);
            }

            filter_type_values_list.addView(childView);

            filter_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if(isChecked)
                    {
                        serverFillterCO.filter_value_id_list.add(filterCOList.get(position).FilterValueList.get(finalI).id);
                        filterCOList.get(position).FilterValueList.get(finalI).isChecked = true;
                        //Toast.makeText(getActivity(), "checked", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        serverFillterCO.filter_value_id_list.remove(filterCOList.get(position).FilterValueList.get(finalI).id);
                        filterCOList.get(position).FilterValueList.get(finalI).isChecked = false;
                        // Toast.makeText(getActivity(), "Unchecked", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
    }
*/
    private void populateFilter()
    {
        filter_types_list.removeAllViews();
        for(int i = 0; i <filterCOList.size(); i++)//filterCOList.size()
        {
            final View childView = LayoutInflater.from(getActivity()).inflate(R.layout.filter_brand_view_layout,null);

            TextView filter_text = childView.findViewById(R.id.filter_text);
            RelativeLayout filter_indicator_layout = childView.findViewById(R.id.filter_indicator_layout);
            LinearLayout filter_layout = childView.findViewById(R.id.filter_layout);
            filter_text.setText(filterCOList.get(i).filter_title);
            if(i == 0)
            {
             //   populatefilterValue(0);
            }

            final int finalI = i;
            filter_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
            //        populatefilterValue(finalI);
                }
            });

            filter_types_list.addView(childView);
        }
    }

    public static BottomFilter newInstance(ArrayList<FilterCO> filterCOList)
    {
        Bundle args = new Bundle();
        args.putParcelableArrayList("filterCOList", filterCOList);
        BottomFilter fragment = new BottomFilter();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            // filterCOList = getArguments().getParcelableArrayList("filterCOList");
            filterCOList.clear();

            String filterListString = AppUtilities.readFromPref(IConstants.FILTER_LIST);
            Type type = new TypeToken<ArrayList<FilterCO>>() {}.getType();
            filterCOList = AppController.gson.fromJson(filterListString, type);

            // filterCOList.addAll(filterCOListmain);
            // Toast.makeText(getActivity(),filterCOList+"",Toast.LENGTH_LONG).show();
        }
    }

    public void setDialogResult(OnMyDialogResult dialogResult)
    {
        mDialogResult = dialogResult;
    }

    public interface OnMyDialogResult
    {
        void finish(ArrayList<ServerFillterCO> serverFillterCOList,  ArrayList<FilterCO> filterCOList);
    }
}
