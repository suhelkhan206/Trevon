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
import com.bumptech.glide.request.RequestOptions;
import com.tws.trevon.R;
import com.tws.trevon.co.CartProductCO;
import com.tws.trevon.co.OrderDetailCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.constants.IConstants;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by admin on 7/26/2017.
 */

public class OrderProductDetailAdapter  extends RecyclerView.Adapter<OrderProductDetailAdapter.MyViewHolder>
{
    public List<OrderDetailCO> orderDetailCOList;
    Context context;
    boolean isOrderScreen;

    public void setClickListener(OrderProductDetailAdapter.ClickListner enrolledClick) {
        this.enrolledClick = enrolledClick;
    }
    public OrderProductDetailAdapter.ClickListner enrolledClick;


    public interface ClickListner
    {
        void onDownloadInvoiceClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        public ImageView product_image;
        public TextView product_name,product_sale_price;
        public RelativeLayout order_cart_item_line;
        public LinearLayout root_view_my_cart_list;

        // public TextView product_quantity;
        // public LinearLayout remove_from_cart;
        public TextView  cart_product_size_color,cart_product_piece;
       //public LinearLayout download_invoice;


        public MyViewHolder(View view)
        {
            super(view);
            product_image = view.findViewById(R.id.product_image);
            product_name = view.findViewById(R.id.product_name);
            cart_product_piece = view.findViewById(R.id.cart_product_piece);


            product_sale_price = view.findViewById(R.id.product_sale_price);

            order_cart_item_line = view.findViewById(R.id.order_cart_item_line);
            root_view_my_cart_list = view.findViewById(R.id.root_view_my_cart_list);

            cart_product_size_color = view.findViewById(R.id.cart_product_size_color);


            //download_invoice = view.findViewById(R.id.download_invoice);
            root_view_my_cart_list.setOnClickListener(this);
            //download_invoice.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {

                /*case R.id.download_invoice:
                   enrolledClick.onDownloadInvoiceClick(v,getAdapterPosition());
                    break;*/


            }
        }
    }


    public OrderProductDetailAdapter(Context context, List<OrderDetailCO> orderDetailCOList, boolean isOrderScreen)
    {
        this.orderDetailCOList = orderDetailCOList;
        this.context = context;
        this.isOrderScreen = isOrderScreen;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_product_detail_adapter_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        OrderDetailCO orderDetailCO = orderDetailCOList.get(position);

        //holder.product_image.set
        String upperString = orderDetailCO.product.title.substring(0, 1).toUpperCase() +
                orderDetailCO.product.title.substring(1).toLowerCase();
        holder.product_name.setText(upperString);
        holder.product_sale_price.setText(IConstants.RUPEE_ICON +orderDetailCO.price);

        holder.cart_product_size_color.setText(orderDetailCO.size);
        holder.cart_product_piece.setText(orderDetailCO.quantity);


        Glide.with(AppController.getInstance().getApplicationContext())
                .load(orderDetailCO.product.image)
                .apply(RequestOptions.placeholderOf(R.drawable.error_images).error(R.drawable.error_images))
                .dontAnimate()
                .into(holder.product_image);

    }

    @Override
    public int getItemCount() {
        return orderDetailCOList.size();
    }
}

