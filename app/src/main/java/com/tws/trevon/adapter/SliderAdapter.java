package com.tws.trevon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tws.trevon.R;
import com.tws.trevon.co.SliderCO;
import com.tws.trevon.common.AppController;

import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    public List<SliderCO> sliderCOList;

    public void setClickListener(SliderAdapter.ClickListner enrolledClick) {
        this.enrolledClick = enrolledClick;
    }
    public SliderAdapter.ClickListner enrolledClick;


    public interface ClickListner
    {
        void onitemClick(View view, int position);
    }

    public SliderAdapter(Context context,  List<SliderCO> sliderCOList) {
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

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enrolledClick.onitemClick(v,position);
            }
        });

        Glide.with(AppController.getInstance().getApplicationContext())
                .load(sliderCO.image)
                .apply(RequestOptions.placeholderOf(R.drawable.error_images).error(R.drawable.error_images))
                .into(linearLayout);

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
