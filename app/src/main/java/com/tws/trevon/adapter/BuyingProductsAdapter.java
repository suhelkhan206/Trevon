package com.tws.trevon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tws.trevon.R;
import com.tws.trevon.co.ProductCO;
import com.tws.trevon.co.StockCO;
import com.tws.trevon.constants.IConstants;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class BuyingProductsAdapter extends RecyclerView.Adapter<BuyingProductsAdapter.MyViewHolder> {

    public List<StockCO> stock;
    private Context mContext;
    ProductCO productCO;

    public void setClickListener(BuyingProductsAdapter.ClickListner enrolledClick) {
        this.enrolledClick = enrolledClick;
    }
    public BuyingProductsAdapter.ClickListner enrolledClick;


    public interface ClickListner
    {
        void onIncreaseClick(View view, int position);
        void onDecreaseClick(View view, int position);

    }

    public BuyingProductsAdapter(Context context, List<StockCO> stock, ProductCO productCO)
    {
        this.stock = stock;
        this.mContext = context;
        this.productCO = productCO;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.buying_products_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        final StockCO stockCO = stock.get(position);

        holder.itemCount.setText(stockCO.quantity);
        holder.product_set.setText("Set of "+productCO.piece_in_set);
        holder.min_qty_set.setText("Min. qty: "+productCO.min_purchase_qty+ " Set");
        holder.productsAvailableColor.setText(stockCO.color);
        holder.productSize.setText(stockCO.product_size);

        Integer retailer_max_purchase_qty = Integer.parseInt( productCO.retailer_max_purchase_qty);
        Integer quantity = Integer.parseInt( productCO.quantity);

        Integer piece_in_set = Integer.parseInt( productCO.piece_in_set);
        if(quantity > retailer_max_purchase_qty)
        {
            Integer wholesale_price = Integer.parseInt( productCO.wholesale_price);
            Integer total = piece_in_set*wholesale_price;
            holder.productPrice.setText(IConstants.RUPEE_ICON+total+" per set");
        }
        else
        {
            Integer retail_price = Integer.parseInt( productCO.retail_price);
            Integer total = piece_in_set*retail_price;
            holder.productPrice.setText(IConstants.RUPEE_ICON+total+" per set");
        }

        holder.piece_of_set.setText("("+productCO.piece_in_set+" pcs per set)");

    }

    @Override
    public int getItemCount()
    {
        return (null != stock ? stock.size() : 0);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {


        LinearLayout decreaseButton;
        LinearLayout increaseButton;
        TextView itemCount;
        TextView productsAvailableColor;
        TextView productSize;
        TextView productPrice;
        TextView product_set, min_qty_set;
        TextView piece_of_set;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            decreaseButton = itemView.findViewById(R.id.decrease_btn);
            increaseButton = itemView.findViewById(R.id.increase_btn);
            itemCount = itemView.findViewById(R.id.item_count);
            productsAvailableColor = itemView.findViewById(R.id.products_available_color);
            productSize = itemView.findViewById(R.id.products_size);
            productPrice = itemView.findViewById(R.id.products_price);
            product_set = itemView.findViewById(R.id.product_set);
            min_qty_set = itemView.findViewById(R.id.min_qty_set);
            piece_of_set = itemView.findViewById(R.id.piece_of_set);

            decreaseButton.setOnClickListener(this);
            increaseButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.increase_btn:
                    enrolledClick.onIncreaseClick(v, getAdapterPosition());
                    break;

                case R.id.decrease_btn:
                    enrolledClick.onDecreaseClick(v, getAdapterPosition());
                    break;
            }
        }
    }
}
