package com.tws.trevon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tws.trevon.R;
import com.tws.trevon.co.ProductCO;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class SearchProductAdapter extends RecyclerView.Adapter<SearchProductAdapter.MyViewHolder>
{
    public List<ProductCO> featuredFoodCOList;
    private SearchProductAdapter.RVItemClickListener rVItemClickListener;
    private Context context;
    int pos;

    public void setRVItemClickListener(SearchProductAdapter.RVItemClickListener rVItemClickListener)
    {
        this.rVItemClickListener = rVItemClickListener;
    }


    public interface RVItemClickListener
    {
        void onFavouriteProductClick(View view, int position, int pos);
    }


    public SearchProductAdapter(Context context, List<ProductCO> featuredFoodCOList, int position)
    {
        this.featuredFoodCOList=featuredFoodCOList;
        this.context = context;
        this.pos = position;
    }


    @Override
    public SearchProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_product_adapter_view, parent, false);

        return new SearchProductAdapter.MyViewHolder(itemView);

    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        ProductCO productCO = featuredFoodCOList.get(position);

        try{
            String upperString = productCO.title.substring(0, 1).toUpperCase() +
                    productCO.title.substring(1).toLowerCase();
            holder.search_text.setText(upperString);
        }
        catch(Exception e)
        {

        }


    }

    @Override
    public int getItemCount() {
        return featuredFoodCOList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView search_text;
        LinearLayout search_root;

        public MyViewHolder(View rowView)
        {
            super(rowView);
            search_text = rowView.findViewById(R.id.search_text);
            search_root = rowView.findViewById(R.id.search_root);

            search_root.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            switch(v.getId())
            {
                case R.id.search_root:
                    rVItemClickListener.onFavouriteProductClick(v,getAdapterPosition(),pos);
                    break;
            }

        }
    }

}

