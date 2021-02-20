package com.tws.trevon.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tws.trevon.R;
import com.tws.trevon.model.NotificationCO;

import java.util.List;

/**
 * Created by admin on 7/26/2017.
 */

public class NotificationAdapter  extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    public List<NotificationCO> notificationCOList;
    Context context;

    public void setEnrolledClickListener(NotificationAdapter.EnrolledClickListner enrolledClick) {
        this.enrolledClick = enrolledClick;
    }
    public NotificationAdapter.EnrolledClickListner enrolledClick;


    public interface EnrolledClickListner
    {
        void onEnrolledCLick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        public TextView notification_title;
        public TextView notification_desc;
        public LinearLayout notification_root_view;

        public MyViewHolder(View view)
        {
            super(view);
            notification_title = view.findViewById(R.id.notification_title);
            notification_desc = view.findViewById(R.id.notification_desc);
            notification_root_view = view.findViewById(R.id.notification_root_view);

        }

        @Override
        public void onClick(View v)
        {
           /* switch (v.getId())
            {
                case R.id.enrolled_parent_view:
                    enrolledClick.onEnrolledCLick(v,getAdapterPosition());
                    break;
            }*/
        }
    }


    public NotificationAdapter(Context context,  List<NotificationCO> notificationCOList) {
        this.notificationCOList = notificationCOList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        NotificationCO notificationCO = notificationCOList.get(position);
        holder.notification_title.setText(notificationCO.title);
        holder.notification_desc.setText(notificationCO.message);
    }

    @Override
    public int getItemCount() {
        return notificationCOList.size();
    }
}


