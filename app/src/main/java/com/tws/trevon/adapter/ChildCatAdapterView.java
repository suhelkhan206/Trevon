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
import com.tws.trevon.co.SubminiCategoryCO;
import com.tws.trevon.common.ConstantVariable;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class ChildCatAdapterView extends RecyclerView.Adapter<ChildCatAdapterView.ItemRowHolder> {

    public List<CategoryCO> mimi_category;
    private Context mContext;


    public ChildCatAdapterView(Context context, List<CategoryCO> mimi_category) {
        this.mimi_category = mimi_category;
        this.mContext = context;

    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_view_adapter_view, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final CategoryCO subminiCategoryCO = mimi_category.get(position);

       /* Glide.with(mContext).load(singleItem.image)
                .placeholder(R.drawable.loading)
                .centerCrop().crossFade()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.image);*/

        Glide.with(mContext)
                .load(subminiCategoryCO.image)
                .apply(RequestOptions.placeholderOf(R.drawable.error_images)
                        .error(R.drawable.error_images))
                .transform(new CenterCrop(),
                        new RoundedCorners(14))
                .dontAnimate()
                .into(holder.child_cat_image);

        String upperString = subminiCategoryCO.title.substring(0, 1).toUpperCase() +
                subminiCategoryCO.title.substring(1).toLowerCase();
        holder.child_cat_title.setText(upperString);

        holder.root_view_child_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstantVariable.CATEGORY_TITLE = subminiCategoryCO.title;
                Intent i = new Intent(mContext, ProductViewAll.class);
                i.putExtra("category_id", subminiCategoryCO.id);
                i.putExtra("type", "sub_category_product_ list");
                mContext.startActivity(i);

            }
        });



    }

    @Override
    public int getItemCount()
    {
        return (null != mimi_category ? mimi_category.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder
    {
        public ImageView child_cat_image;
        public TextView child_cat_title;
        public LinearLayout root_view_child_cat;


        private ItemRowHolder(View itemView)
        {
            super(itemView);
            child_cat_image = itemView.findViewById(R.id.child_cat_image);
            child_cat_title = itemView.findViewById(R.id.child_cat_title);
            root_view_child_cat = itemView.findViewById(R.id.root_view_child_cat);
        }
    }
}


