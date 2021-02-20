package com.tws.trevon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tws.trevon.R;
import com.tws.trevon.co.CategoryCO;
import com.tws.trevon.co.ProductCO;
import com.tws.trevon.co.StockCO;
import com.tws.trevon.constants.IConstants;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class BuyingOptionAdapter extends RecyclerView.Adapter<BuyingOptionAdapter.MyViewHolder> {

    public List<StockCO> stock;
    private Context mContext;
    ProductCO productCO;

    public BuyingOptionAdapter(Context context, List<StockCO> stock, ProductCO productCO)
    {
        this.stock = stock;
        this.mContext = context;
        this.productCO = productCO;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.buying_option_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        final StockCO stockCO = stock.get(position);

        holder.availableColorName.setText(stockCO.color);
        holder.size.setText(stockCO.product_size);
       // holder.buying_set.setText("Set of "+productCO.piece_in_set);
        Integer piece_in_set = Integer.parseInt( productCO.piece_in_set);
        if(productCO.product_purchase_type.equals("Retailer"))
        {
            Integer retail_price = Integer.parseInt( productCO.retail_price);
            Integer total = piece_in_set*retail_price;
            holder.priceSet.setText(IConstants.RUPEE_ICON+total+" per set"+" (contains "+productCO.piece_in_set+" pcs)");
        }
        else
        {
            Integer wholesale_price = Integer.parseInt( productCO.wholesale_price);
            Integer total = piece_in_set*wholesale_price;
            holder.priceSet.setText(IConstants.RUPEE_ICON+total+" per set"+" (contains "+productCO.piece_in_set+" pcs)");
        }

        holder.min_qty_product.setText(productCO.min_purchase_qty+ " Set");
    }


    @Override
    public int getItemCount()
    {
        return (null != stock ? stock.size() : 0);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView availableColorName;
        private TextView size;
        private TextView priceSet;
        private TextView priceDetailed;
        private TextView min_qty_product;
        private TextView buying_set;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            availableColorName = itemView.findViewById(R.id.buying_available_color);
            size = itemView.findViewById(R.id.buying_size);
            priceSet = itemView.findViewById(R.id.buying_price_set);
            priceDetailed = itemView.findViewById(R.id.buying_price_detailed);
            min_qty_product = itemView.findViewById(R.id.min_qty_product);
            buying_set = itemView.findViewById(R.id.buying_set);

        }
    }
}
