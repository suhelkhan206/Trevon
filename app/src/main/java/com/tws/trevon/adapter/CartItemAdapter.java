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
import com.tws.trevon.common.AppController;
import com.tws.trevon.constants.IConstants;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by admin on 7/26/2017.
 */

public class CartItemAdapter  extends RecyclerView.Adapter<CartItemAdapter.MyViewHolder>
{
    public List<CartProductCO> cartProductCOList;
    Context context;
    boolean isOrderScreen;

    public void setClickListener(CartItemAdapter.ClickListner enrolledClick) {
        this.enrolledClick = enrolledClick;
    }
    public CartItemAdapter.ClickListner enrolledClick;


    public interface ClickListner
    {
        void onIncreaseClick(View view, int position);
        void onDecreaseClick(View view, int position);
        void onitemClick(View view, int position);
        void onitemRemoveClick(View view, int position);
        void onitemDetailClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        public ImageView product_image;
        public TextView product_name,product_sale_price,product_price_original;
        public LinearLayout increase_order_btn, decrease_order_btn;
        public TextView order_item_count;
        public LinearLayout root_view_my_cart_list;
        public LinearLayout product_count_status;
       // public TextView product_quantity;
       // public LinearLayout remove_from_cart;
        public TextView  cart_product_size_color,cart_product_piece;
        public LinearLayout view_detail;


        public MyViewHolder(View view)
        {
            super(view);
            product_image = view.findViewById(R.id.product_image);
            product_name = view.findViewById(R.id.product_name);
            product_sale_price = view.findViewById(R.id.product_sale_price);
            product_price_original = view.findViewById(R.id.product_price_original);
            increase_order_btn = view.findViewById(R.id.increase_order_btn);
            decrease_order_btn = view.findViewById(R.id.decrease_order_btn);
            order_item_count = view.findViewById(R.id.order_item_count);

            root_view_my_cart_list = view.findViewById(R.id.root_view_my_cart_list);
            product_count_status = view.findViewById(R.id.product_count_status);
           // product_quantity = view.findViewById(R.id.product_quantity);
            cart_product_size_color = view.findViewById(R.id.cart_product_size_color);
            cart_product_piece = view.findViewById(R.id.cart_product_piece);
           // remove_from_cart = view.findViewById(R.id.remove_from_cart);
            view_detail = view.findViewById(R.id.view_detail);
            increase_order_btn.setOnClickListener(this);
            decrease_order_btn.setOnClickListener(this);
            root_view_my_cart_list.setOnClickListener(this);
            view_detail.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.increase_order_btn:
                    enrolledClick.onIncreaseClick(v,getAdapterPosition());
                    break;

                case R.id.decrease_order_btn:
                    enrolledClick.onDecreaseClick(v,getAdapterPosition());
                    break;
                case R.id.root_view_my_cart_list:
                    enrolledClick.onitemClick(v,getAdapterPosition());
                    break;

                case R.id.view_detail:
                    enrolledClick.onitemDetailClick(v,getAdapterPosition());
                    break;


            }
        }
    }


    public CartItemAdapter(Context context, List<CartProductCO> cartProductCOList, boolean isOrderScreen)
    {
        this.cartProductCOList = cartProductCOList;
        this.context = context;
        this.isOrderScreen = isOrderScreen;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item_adapter_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        CartProductCO cartProductCO = cartProductCOList.get(position);

        //holder.product_image.set
        String upperString = cartProductCO.product.title.substring(0, 1).toUpperCase() +
                cartProductCO.product.title.substring(1).toLowerCase();
        holder.product_name.setText(upperString);

        Integer retailer_max_purchase_qty = Integer.parseInt( cartProductCO.product.retailer_max_purchase_qty);
        Integer total_quantity = Integer.parseInt( cartProductCO.total_quantity);

        if(total_quantity > retailer_max_purchase_qty)
        {
            holder.product_sale_price.setText(IConstants.RUPEE_ICON +cartProductCO.product.wholesale_price);
        }
        else
        {
            holder.product_sale_price.setText(IConstants.RUPEE_ICON +cartProductCO.product.retail_price);
        }

        holder.product_price_original.setText(IConstants.RUPEE_ICON+cartProductCO.product.mrp);
      //  holder.cart_product_size_color.setText(cartProductCO.size);
        holder.cart_product_piece.setText(cartProductCO.product.piece_in_set);
        holder.order_item_count.setText(cartProductCO.total_quantity);


        if(isOrderScreen)
        {
            holder.product_price_original.setVisibility(View.GONE);
          //  holder.product_quantity.setVisibility(View.VISIBLE);
          //  holder.product_quantity.setText("Qty: "+cartProductCO.total_quantity);
            holder.product_count_status.setVisibility(View.GONE);
            //holder.remove_from_cart.setVisibility(View.GONE);
            holder.view_detail.setVisibility(View.GONE);
        }
        else
        {
            holder.product_price_original.setVisibility(View.VISIBLE);
         //   holder.product_quantity.setVisibility(View.GONE);
            holder.product_count_status.setVisibility(View.GONE);
          //  holder.remove_from_cart.setVisibility(View.VISIBLE);
            holder.view_detail.setVisibility(View.VISIBLE);
        }



     /*   if(position==cartProductCOList.size()-1)
        {
            holder.order_cart_item_line.setVisibility(View.GONE);
        }
        else
        {
            holder.order_cart_item_line.setVisibility(View.VISIBLE);
        }*/

       /* Drawable res = context.getResources().getDrawable(ProductCOList.get(position));
          holder.featured_brands.setImageDrawable(res);*/

        Glide.with(AppController.getInstance().getApplicationContext())
                .load(cartProductCO.product.image)
                .apply(RequestOptions.placeholderOf(R.drawable.error_images).error(R.drawable.error_images))
                .dontAnimate()
                .into(holder.product_image);

    }

    @Override
    public int getItemCount() {
        return cartProductCOList.size();
    }
}
