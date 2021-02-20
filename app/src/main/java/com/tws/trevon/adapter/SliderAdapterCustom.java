package com.tws.trevon.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.tws.trevon.R;
import com.tws.trevon.activity.MainActivity;
import com.tws.trevon.activity.ProductViewAll;
import com.tws.trevon.co.SliderCO;
import com.tws.trevon.common.ConstantVariable;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapterCustom extends
        SliderViewAdapter<SliderAdapterCustom.SliderAdapterVH> {

    private Context context;
    private List<SliderCO> mSliderItems = new ArrayList<>();

    public SliderAdapterCustom(Context context, List<SliderCO> sliderItems) {
        this.context = context;
        this.mSliderItems = sliderItems;
    }


    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(SliderCO sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_slider_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        final SliderCO sliderCO = mSliderItems.get(position);

        Glide.with((context))
                .load(sliderCO.image)
                .placeholder(R.drawable.error_images)
                // .centerCrop().crossFade()
                //.diskCacheStrategy(DiskCacheStrategy.RESULT)
                .dontAnimate()
                .into(viewHolder.image);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstantVariable.CATEGORY_TITLE = sliderCO.title;
                Intent i1 =new Intent(context, ProductViewAll.class);
                i1.putExtra("category_id", sliderCO.category_id);
                i1.putExtra("type", "home_page_sliders");
                context.startActivity(i1);
            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView image;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            this.itemView = itemView;
        }
    }

}
