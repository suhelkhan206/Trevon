package com.tws.trevon.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tws.trevon.R;
import com.tws.trevon.activity.SingleProductScreen;
import com.tws.trevon.co.ProductCO;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.constants.IConstants;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class FeatureProductAdapterHorizontal extends RecyclerView.Adapter<FeatureProductAdapterHorizontal.ItemRowHolder> {

    private List<ProductCO> productCOList;
    private Context mContext;

    public void setEnrolledClickListener(FeatureProductAdapter.EnrolledClickListner enrolledClick) {
        this.enrolledClick = enrolledClick;
    }
    public FeatureProductAdapter.EnrolledClickListner enrolledClick;


    public interface EnrolledClickListner
    {
        void onViewClick(View view, int positionParent);

    }



    public FeatureProductAdapterHorizontal(Context context, List<ProductCO> productCOList) {
        this.productCOList = productCOList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feature_product_adapter_view_horizontal, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final ProductCO productCO = productCOList.get(position);

        String upperString = productCO.title.substring(0, 1).toUpperCase() +
                productCO.title.substring(1).toLowerCase();
        holder.product_title.setText(upperString);
        if(AppValidate.isNotEmpty(productCO.description))
        {
            holder.product_description.setText(  Html.fromHtml(productCO.description), TextView.BufferType.SPANNABLE);
        }

        holder.sale_price.setText(productCO.sell_price);
        holder.original_price.setText(IConstants.RUPEE_ICON+productCO.mrp);
        holder.stock_count.setText(productCO.stock+ " Left !");
        Drawable res;
        if (productCO.is_wished.equals("1"))
        {
            res = mContext.getResources().getDrawable(R.drawable.ic_fill_heart);

        } else
        {
            res = mContext.getResources().getDrawable(R.drawable.ic_heart_shape_outline);
        }
        holder.wish_product.setImageDrawable(res);


        Glide.with(mContext)
                .load(productCO.image)
                .apply(RequestOptions.placeholderOf(R.drawable.error_images).error(R.drawable.error_images))
                .dontAnimate()
                .into(holder.image);


        holder.rootLayout_feature_cat_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_single = new Intent(mContext, SingleProductScreen.class);
                intent_single.putExtra("productId",productCO.id);
                mContext.startActivity(intent_single);
            }
        });

        holder.wish_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                enrolledClick.onViewClick(v,position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != productCOList ? productCOList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder
    {
        public ImageView image;
        private LinearLayout rootLayout_feature_cat_home;
        TextView product_title;
        LinearLayout shipping_status_layout;
        TextView shipping_status_text;
        TextView sale_price, original_price;
        ImageView wish_product;
        TextView product_description;
        TextView stock_count;

        private ItemRowHolder(View itemView)
        {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            product_title = itemView.findViewById(R.id.product_title);
            shipping_status_layout = itemView.findViewById(R.id.shipping_status_layout);
            shipping_status_text = itemView.findViewById(R.id.shipping_status_text);
            sale_price = itemView.findViewById(R.id.sale_price);
            original_price = itemView.findViewById(R.id.original_price);
            wish_product = itemView.findViewById(R.id.wish_product);
            product_description = itemView.findViewById(R.id.product_description);
            rootLayout_feature_cat_home = itemView.findViewById(R.id.rootLayout_feature_cat_home);
            stock_count = itemView.findViewById(R.id.stock_count);
        }
    }
}


