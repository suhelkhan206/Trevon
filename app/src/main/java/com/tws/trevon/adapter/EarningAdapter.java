package com.tws.trevon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tws.trevon.R;
import com.tws.trevon.co.EarningCO;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class EarningAdapter extends RecyclerView.Adapter<EarningAdapter.ItemRowHolder> {

    public List<EarningCO> earningCOList;
    private Context mContext;


    public EarningAdapter(Context context, List<EarningCO> earningCOList) {
        this.earningCOList = earningCOList;
        this.mContext = context;

    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.earnign_adapter_view, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {

        final EarningCO earningCO = earningCOList.get(position);

        holder.earning_amount.setText(earningCO.earning);

        if(earningCO.credit_debit.equals("debit"))
        {
            holder.earn_root_view.setBackgroundResource(R.drawable.wallet_background);
        }
        else
        {
            holder.earn_root_view.setBackgroundResource(R.drawable.wallet_background_green);
        }


    }

    @Override
    public int getItemCount()
    {
        return (null != earningCOList ? earningCOList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder
    {
        public TextView earning_amount;

        public LinearLayout earn_root_view;

        private ItemRowHolder(View itemView)
        {
            super(itemView);
            earning_amount = itemView.findViewById(R.id.earning_amount);
            earn_root_view = itemView.findViewById(R.id.earn_root_view);
        }
    }
}



