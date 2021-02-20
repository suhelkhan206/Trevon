package com.tws.trevon.customwork;

import android.os.Bundle;
import android.util.DisplayMetrics;

import com.tws.trevon.R;

import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


public class MainActivity extends AppCompatActivity {
    public final static int LOOPS = 1000;
    public CarouselPagerAdapter adapter;
    public ViewPager pager;
    public static int count = 10; //ViewPager items size
    /**
     * You shouldn't define first page = 0.
     * Let define firstpage = 'number viewpager size' to make endless carousel
     */
    public static int FIRST_PAGE = 10;
    ArrayList<ItemLatest> mSliderList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        pager = (ViewPager) findViewById(R.id.myviewpager);

        for(int i=0; i<10;i++)
        {
            ItemLatest tl = new ItemLatest();
            tl.setLatestImageUrl("https://filocab.com/front/logo.jpg");
            tl.setLatestVideoType("local");
            tl.setLatestVideoName("adfdsg");
            mSliderList.add(tl);
        }

        //set page margin between pages for viewpager
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int pageMargin = ((metrics.widthPixels / 4) * 2);
        pager.setPageMargin(-pageMargin);

        adapter = new CarouselPagerAdapter(this, getSupportFragmentManager(), mSliderList);
        pager.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        pager.addOnPageChangeListener(adapter);

        // Set current item to the middle page so we can fling to both
        // directions left and right
        pager.setCurrentItem(FIRST_PAGE);
        pager.setOffscreenPageLimit(3);
    }
}