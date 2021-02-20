package com.tws.trevon.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.tws.trevon.R;
import com.tws.trevon.activity.ProductViewAll;
import com.tws.trevon.co.CategoryCO;
import com.tws.trevon.co.ProductFilterCO;
import com.tws.trevon.co.SubminiCategoryCO;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class ProductFiIlterAdapter extends RecyclerView.Adapter<ProductFiIlterAdapter.ItemRowHolder> {

    public List<ProductFilterCO> productFilterCOList;
    private Context mContext;


    public ProductFiIlterAdapter(Context context, List<ProductFilterCO> productFilterCOList) {
        this.productFilterCOList = productFilterCOList;
        this.mContext = context;

    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_filter_object_view, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ProductFilterCO productFilterCO = productFilterCOList.get(position);


        String filter_title = productFilterCO.filter_title.substring(0, 1).toUpperCase() +
                productFilterCO.filter_title.substring(1).toLowerCase();

        String filter_value_title = productFilterCO.filter_value_title.substring(0, 1).toUpperCase() +
                productFilterCO.filter_value_title.substring(1).toLowerCase();


        holder.filter_key.setText(filter_title +": "+filter_value_title);


       // holder.filter_value.setText(filter_value_title);
    }

    @Override
    public int getItemCount()
    {
        return (null != productFilterCOList ? productFilterCOList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder
    {

        public TextView filter_key;



        private ItemRowHolder(View itemView)
        {
            super(itemView);
            filter_key = itemView.findViewById(R.id.filter_key);
        }
    }
}



