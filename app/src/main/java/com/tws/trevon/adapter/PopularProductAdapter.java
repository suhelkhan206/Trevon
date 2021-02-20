package com.tws.trevon.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.tws.trevon.R;
import com.tws.trevon.activity.SingleProductScreen;
import com.tws.trevon.co.ProductCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.constants.IConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import androidx.core.content.FileProvider;
import androidx.multidex.BuildConfig;
import androidx.recyclerview.widget.RecyclerView;

public class PopularProductAdapter extends RecyclerView.Adapter<PopularProductAdapter.ItemRowHolder> {

    public List<ProductCO> productCOList;
    private Context mContext;

    public void setEnrolledClickListener(PopularProductAdapter.EnrolledClickListner enrolledClick) {
        this.enrolledClick = enrolledClick;
    }
    public PopularProductAdapter.EnrolledClickListner enrolledClick;


    public interface EnrolledClickListner
    {
        void onViewClick(View view, int positionParent);

    }



    public PopularProductAdapter(Context context, List<ProductCO> productCOList) {
        this.productCOList = productCOList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_product_adapter_view, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position)
    {
        final ProductCO productCO = productCOList.get(position);
        String upperString = productCO.title.substring(0, 1).toUpperCase() +
                productCO.title.substring(1).toLowerCase();
        holder.product_title.setText(upperString);
        holder.sale_price.setText(productCO.sell_price);
        holder.original_price.setText(IConstants.RUPEE_ICON+productCO.mrp);
        holder.rating_count.setText(productCO.avg_rate);

        Glide.with(AppController.getInstance().getApplicationContext())
                .load(productCO.image)
                .apply(RequestOptions.placeholderOf(R.drawable.error_images).error(R.drawable.error_images))
                .dontAnimate()
                .into(holder.image);


        holder.rootLayout_feature_cat_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent_single = new Intent(mContext, SingleProductScreen.class);
                intent_single.putExtra("productId",productCO.id);
                mContext.startActivity(intent_single);
            }
        });

        holder.sahre_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                enrolledClick.onViewClick(v,position);
                AppUtilities.shareImage(productCO.image,mContext,productCO.title);
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
        TextView rating_count;
        TextView sale_price, original_price;
        ImageView sahre_product;

        private ItemRowHolder(View itemView)
        {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            product_title = itemView.findViewById(R.id.product_title);
            sale_price = itemView.findViewById(R.id.sale_price);
            rating_count = itemView.findViewById(R.id.rating_count);
            original_price = itemView.findViewById(R.id.original_price);
            sahre_product = itemView.findViewById(R.id.sahre_product);
            rootLayout_feature_cat_home = itemView.findViewById(R.id.rootLayout_feature_cat_home);
        }
    }

}


