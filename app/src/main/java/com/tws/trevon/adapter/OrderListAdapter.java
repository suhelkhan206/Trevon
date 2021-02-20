package com.tws.trevon.adapter;
 

 import android.content.Context;
 import androidx.recyclerview.widget.RecyclerView;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.ImageView;
 import android.widget.LinearLayout;
 import android.widget.RatingBar;
 import android.widget.RelativeLayout;
 import android.widget.TextView;

 import com.bumptech.glide.Glide;
 import com.bumptech.glide.request.RequestOptions;
 import com.tws.trevon.R;
 import com.tws.trevon.common.AppController;
 import com.tws.trevon.constants.IConstants;
 import com.tws.trevon.model.OrderCO;

 import java.util.List;

public class OrderListAdapter  extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> {

    public List<OrderCO> orderCOList;
    Context context;

    public void setEnrolledClickListener(OrderListAdapter.EnrolledClickListner enrolledClick) {
        this.enrolledClick = enrolledClick;
    }
    public OrderListAdapter.EnrolledClickListner enrolledClick;


    public interface EnrolledClickListner
    {
        void onCancelCLick(View view, int position);
        void onOrderViewCLick(View view, int position);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        public TextView order_date,transection_id,
                payment_type,total_amount;
        public LinearLayout order_root_view,cancel_order;
        public RelativeLayout order_line;
        public TextView order_id,order_status;


        public MyViewHolder(View view)
        {
            super(view);
            order_date = view.findViewById(R.id.order_date);
            order_id = view.findViewById(R.id.order_id);
            transection_id = view.findViewById(R.id.transection_id);
            payment_type = view.findViewById(R.id.payment_type);
            total_amount = view.findViewById(R.id.total_amount);
            cancel_order = view.findViewById(R.id.cancel_order);
            order_line = view.findViewById(R.id.order_line);
            order_root_view = view.findViewById(R.id.order_root_view);
            order_root_view.setOnClickListener(this);
            cancel_order.setOnClickListener(this);
            order_status = view.findViewById(R.id.order_status);
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.order_root_view:
                enrolledClick.onOrderViewCLick(v,getAdapterPosition());
                break;

                case R.id.cancel_order:
                    enrolledClick.onCancelCLick(v,getAdapterPosition());
                    break;
            }
        }
    }

    public OrderListAdapter(Context context,  List<OrderCO> orderCOList) {
        this.orderCOList = orderCOList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_view_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position)
    {
        OrderCO orderCO = orderCOList.get(position);

        holder.order_id.setText("OrderID: #"+orderCO.id);
        holder.order_date.setText(orderCO.created_date);
        holder.transection_id.setText(orderCO.transaction_id);
        holder.payment_type.setText(orderCO.payment_mode.toUpperCase());
        holder.total_amount.setText(IConstants.RUPEE_ICON+ orderCO.total_paid);

        if(orderCO.delivery_status.equals("0"))
        {
            holder.order_status.setText("Cancel");

        }
        else if(orderCO.delivery_status.equals("1"))
        {
            holder.order_status.setText("Cancel");
        }
        else if(orderCO.delivery_status.equals("2"))
        {
            holder.order_status.setText("Return");

        }
        else if(orderCO.delivery_status.equals("3"))
        {
            holder.order_status.setText("Cancelled");

        }
        else if(orderCO.delivery_status.equals("4"))
        {
            holder.order_status.setText("Refund Requested");

        }
        else if(orderCO.delivery_status.equals("5"))
        {
            holder.order_status.setText("Refunded");

        }
    }

    @Override
    public int getItemCount() {
        return orderCOList.size();
    }
}




