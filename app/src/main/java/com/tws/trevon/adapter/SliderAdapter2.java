package com.tws.trevon.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;
import com.tws.trevon.R;
import com.tws.trevon.co.SliderCO;
import com.tws.trevon.common.AppController;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.List;

public class SliderAdapter2 extends PagerAdapter {

    private Context context;
    public List<SliderCO> sliderCOList;

    public void setClickListener(SliderAdapter2.ClickListner enrolledClick) {
        this.enrolledClick = enrolledClick;
    }
    public SliderAdapter2.ClickListner enrolledClick;


    public interface ClickListner
    {
        void onitemClick(View view, int position);
    }

    public SliderAdapter2(Context context,  List<SliderCO> sliderCOList) {
        this.context = context;
        this.sliderCOList = sliderCOList;

    }

    @Override
    public int getCount() {
        return sliderCOList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);

        SliderCO sliderCO = sliderCOList.get(position);

        ImageView linearLayout = (ImageView) view.findViewById(R.id.linearLayout);


        Glide.with(AppController.getInstance().getApplicationContext())
                .load(sliderCO.image)
                .apply(RequestOptions.placeholderOf(R.drawable.error_images).error(R.drawable.error_images))
                .transform(new CenterCrop(), new RoundedCorners(14))
                .into(linearLayout);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enrolledClick.onitemClick(v,position);
            }
        });

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}

