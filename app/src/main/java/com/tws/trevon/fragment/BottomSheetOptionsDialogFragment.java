package com.tws.trevon.fragment;


import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tws.trevon.R;
import com.tws.trevon.co.BottomMenuItemCO;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.constants.IConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MG on 17-07-2016.
 */
public class BottomSheetOptionsDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener
{
    OnOptionSelected mOnOptionSelected;
    String title;
    ArrayList<BottomMenuItemCO> bottomMenuItemCOList;
    private int themeColor;

    public static BottomSheetOptionsDialogFragment newInstance(final ArrayList<BottomMenuItemCO> bottomMenuItemCOList)
    {
        return newInstance(bottomMenuItemCOList, IConstants.EMPTY_STRING);
    }
    public static BottomSheetOptionsDialogFragment newInstance(final ArrayList<BottomMenuItemCO> bottomMenuItemCOList, final String titleRes)
    {
        return newInstance(bottomMenuItemCOList, titleRes, IConstants.EMPTY_RESOURCE_ID);
    }

    public static BottomSheetOptionsDialogFragment newInstance(final ArrayList<BottomMenuItemCO> bottomMenuItemCOList, final String titleRes, int themeColor)
    {
        BottomSheetOptionsDialogFragment bottomSheetOptionsDialogFragment = new BottomSheetOptionsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(IConstants.LIST, bottomMenuItemCOList);
        args.putString(IConstants.CODE, titleRes);

        if(themeColor == 0)
        {
            themeColor = IConstants.EMPTY_RESOURCE_ID;
        }

        args.putInt(IConstants.COLOR_THEME, themeColor);
        bottomSheetOptionsDialogFragment.setArguments(args);
        return bottomSheetOptionsDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bottomMenuItemCOList = getArguments().getParcelableArrayList(IConstants.LIST);
        title = getArguments().getString(IConstants.CODE);
        themeColor = getArguments().getInt(IConstants.COLOR_THEME, IConstants.EMPTY_RESOURCE_ID);

/*        if(themeColorRes != IConstants.EMPTY_RESOURCE_ID)
        {
            themeColorRes = getActivity().getResources().getColor(themeColorRes);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.c_bottom_menu_list, container, false);

        if(AppValidate.isNotEmpty(title))
        {
            ((TextView)view.findViewById(R.id.bml_tv_menu_title)).setText(title);
        }

        RecyclerView menuRV = (RecyclerView)view.findViewById(R.id.bml_rv_option_list);
        menuRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        final BottomOptionMenuAdapter bottomOptionMenuAdapter = new BottomOptionMenuAdapter(bottomMenuItemCOList, themeColor);
        menuRV.setHasFixedSize(true);
        menuRV.setAdapter(bottomOptionMenuAdapter);
        bottomOptionMenuAdapter.setOnItemClickListener(new BottomOptionMenuAdapter.OnItemClickListener() {
            @Override
            public void onViewClick(final int position)
            {
                mOnOptionSelected.onOptionSelected(bottomMenuItemCOList.get(position), position);
                dismiss();
            }
        });

        view.findViewById(R.id.bml_tv_cancel).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.bml_tv_cancel:
                dismiss();
                break;
        }
    }

    public interface OnOptionSelected {
        void onOptionSelected(BottomMenuItemCO bottomMenuItemCO, int position);
    }

    public void setOnOptionClickListener(final OnOptionSelected mOnOptionSelected) {
        this.mOnOptionSelected = mOnOptionSelected;
    }

    static class BottomOptionMenuAdapter extends RecyclerView.Adapter<BottomOptionMenuAdapter.MenuVH>
    {
        OnItemClickListener mItemClickListener;
        List<BottomMenuItemCO> bottomMenuItemCOList;
        private int themeColor;

        public BottomOptionMenuAdapter(List<BottomMenuItemCO> bottomMenuItemCOList, final int themeColor)
        {
            super();
            this.bottomMenuItemCOList = bottomMenuItemCOList;
            this.themeColor = themeColor;
        }

        @Override
        public MenuVH onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.c_bottom_menu_item, parent, false);

            MenuVH menuVH = new MenuVH(view);
            return menuVH;
        }

        @Override
        public void onBindViewHolder(MenuVH menuVH, int position)
        {
            final BottomMenuItemCO bottomMenuItemCO = bottomMenuItemCOList.get(position);

            if(bottomMenuItemCO.iconRes == IConstants.EMPTY_RESOURCE_ID)
            {
                menuVH.iconIV.setVisibility(View.GONE);
            }
            else
            {
                menuVH.iconIV.setVisibility(View.VISIBLE);
                menuVH.iconIV.setImageResource(bottomMenuItemCO.iconRes);
            }

            if(bottomMenuItemCO.nameRes == IConstants.EMPTY_RESOURCE_ID)
            {
                if(AppValidate.isEmpty(bottomMenuItemCO.nameText))
                {
                    menuVH.nameTV.setVisibility(View.GONE);
                }
                else
                {
                    menuVH.nameTV.setVisibility(View.VISIBLE);
                    menuVH.nameTV.setText(bottomMenuItemCO.nameText);
                }
            }
            else
            {
                menuVH.nameTV.setVisibility(View.VISIBLE);
                menuVH.nameTV.setText(bottomMenuItemCO.nameRes);
            }

            if(themeColor != IConstants.EMPTY_RESOURCE_ID)
            {
                menuVH.iconIV.setColorFilter(themeColor, PorterDuff.Mode.SRC_ATOP);
                menuVH.nameTV.setTextColor(themeColor);
            }

            if(bottomMenuItemCO.isSelected)
            {
                menuVH.layoutRL.setBackgroundColor(menuVH.layoutRL.getContext().getResources().getColor(R.color.super_ultra_light_gray_color));
            }
            else
            {
                menuVH.layoutRL.setBackgroundColor(menuVH.layoutRL.getContext().getResources().getColor(R.color.white_color));
            }
        }

        @Override
        public int getItemCount() {
            return bottomMenuItemCOList.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        public class MenuVH extends RecyclerView.ViewHolder implements View.OnClickListener
        {
            ImageView iconIV;
            TextView nameTV;
            public RelativeLayout layoutRL;

            MenuVH(View itemView)
            {
                super(itemView);
                iconIV = (ImageView)itemView.findViewById(R.id.bmi_iv_menu_icon);
                nameTV = (TextView)itemView.findViewById(R.id.bmi_tv_group_name);
                layoutRL = (RelativeLayout)itemView.findViewById(R.id.bmi_rl_menu_complete_layout);

                itemView.findViewById(R.id.bmi_rl_menu_layout).setOnClickListener(this);
            }

            @Override
            public void onClick(View view)
            {
                if(mItemClickListener != null)
                {
                    switch (view.getId())
                    {
                        case R.id.bmi_rl_menu_layout:
                            mItemClickListener.onViewClick(getAdapterPosition());
                            break;
                    }
                }
            }
        }

        public interface OnItemClickListener {
            void onViewClick(int position);
        }

        public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
            this.mItemClickListener = mItemClickListener;
        }
    }
}