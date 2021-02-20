package com.tws.trevon.adapter;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.tws.trevon.R;
import com.tws.trevon.co.MyAddressCO;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.constants.IConstants;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by admin on 7/26/2017.
 */

public class MyConnectionAdapter  extends RecyclerView.Adapter<MyConnectionAdapter.MyViewHolder>
{
    public List<UserCO> userCOList;
    Context context;
    boolean isOrderScreen;

    public void setClickListener(MyConnectionAdapter.ClickListner enrolledClick) {
        this.enrolledClick = enrolledClick;
    }
    public MyConnectionAdapter.ClickListner enrolledClick;


    public interface ClickListner
    {
        void onitemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        public TextView user_name;
        public LinearLayout root_view_connection;


        public MyViewHolder(View view)
        {
            super(view);
            user_name = view.findViewById(R.id.user_name);
            root_view_connection = view.findViewById(R.id.root_view_connection);
            root_view_connection.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.root_view_connection:
                    enrolledClick.onitemClick(v, getAdapterPosition());
                    break;
            }
        }
    }


    public MyConnectionAdapter(Context context, List<UserCO> userCOList)
    {
        this.userCOList = userCOList;
        this.context = context;

    }

    @Override
    public MyConnectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_connection_adapter_view, parent, false);

        return new MyConnectionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyConnectionAdapter.MyViewHolder holder, int position)
    {
        UserCO userCO = userCOList.get(position);

       holder.user_name.setText(userCO.first_name);
    }

    @Override
    public int getItemCount() {
        return userCOList.size();
    }
}

