package com.tws.trevon.adapter;

import android.content.Context;
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
import com.tws.trevon.co.CategoryCO;
import com.tws.trevon.common.AppController;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;


public class ShopByCategoryAdapter  extends RecyclerView.Adapter<ShopByCategoryAdapter.MyViewHolder> {

    public List<CategoryCO> categoryCOList;
    public boolean isShowEdit;
    Context context;

    public void setAddressClickListener(ShopByCategoryAdapter.AddressClickListner addressClickListner) {
        this.addressClickListner = addressClickListner;
    }
    public ShopByCategoryAdapter.AddressClickListner addressClickListner;


    public interface AddressClickListner
    {
        void onrootCLick(View view, int position);
        void onAddressRootCLick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        public ImageView category_image;
        public  TextView category_text;
        public  LinearLayout root_view_category;


        public MyViewHolder(View view)
        {
            super(view);
            category_image = view.findViewById(R.id.category_image);
            root_view_category = view.findViewById(R.id.root_view_category);
            category_text = view.findViewById(R.id.category_text);
            root_view_category.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.root_view_category:
                    addressClickListner.onrootCLick(v, getAdapterPosition());
                    break;
            }
        }
    }


    public ShopByCategoryAdapter(Context context,  List<CategoryCO> categoryCOList) {
        this.categoryCOList = categoryCOList;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_by_category_adapter_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        CategoryCO categoryCO = categoryCOList.get(position);

        String upperString = categoryCO.title.substring(0, 1).toUpperCase() +
                categoryCO.title.substring(1).toLowerCase();

        holder.category_text.setText(upperString);

        Glide.with(AppController.getInstance().getApplicationContext())
                .load(categoryCO.image)
                .error(R.drawable.error_images)
                .placeholder(R.drawable.error_images)
                .transform(new CenterCrop(), new RoundedCorners(14))
                .dontAnimate()
                //.apply(new RequestOptions().override(width, height))
                .into(holder.category_image);

    }

    @Override
    public int getItemCount()
    {
        return categoryCOList.size();
    }
}


