package com.tws.trevon.customwork;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.reflect.TypeToken;
import com.tws.trevon.R;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.constants.IConstants;

import java.lang.reflect.Type;
import java.util.ArrayList;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class ItemFragment extends Fragment
{
    private static final String POSITON = "position";
    private static final String SCALE = "scale";
    private static final String DRAWABLE_RESOURE = "resource";

    private int screenWidth;
    private int screenHeight;
    ArrayList<ItemLatest> mSliderList =new ArrayList<>();

    public static Fragment newInstance(Context context, int pos, float scale, ArrayList<ItemLatest> mSliderList) {
        Bundle b = new Bundle();
        b.putInt(POSITON, pos);
        b.putFloat(SCALE, scale);
        b.putParcelableArrayList("imageArray", mSliderList);

        return Fragment.instantiate(context, ItemFragment.class.getName(), b);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWidthAndHeight();
        mSliderList.clear();

        String filterListString = AppUtilities.readFromPref(IConstants.HOME_CROUSEL_LIST);
        Type type = new TypeToken<ArrayList<ItemLatest>>() {}.getType();
        mSliderList = AppController.gson.fromJson(filterListString, type);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        final int postion = this.getArguments().getInt(POSITON);
        float scale = this.getArguments().getFloat(SCALE);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth / 2, screenHeight / 2);
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.row_slider_item, container, false);

        ImageView image = linearLayout.findViewById(R.id.image);
        TextView text = linearLayout.findViewById(R.id.text);
        CarouselLinearLayout rootLayout = linearLayout.findViewById(R.id.rootLayout);

        text.setText(mSliderList.get(postion).getLatestVideoName());

      /*  switch (mSliderList.get(postion).getLatestVideoType()) {
            case "local":
                Glide.with(getActivity()).load(mSliderList.get(postion).getLatestVideoImgBig()).placeholder(R.drawable.loading).centerCrop().crossFade().diskCacheStrategy(DiskCacheStrategy.RESULT).into(image);
                break;
            case "server_url":
                Glide.with(getActivity()).load(mSliderList.get(postion).getLatestVideoImgBig()).placeholder(R.drawable.loading).centerCrop().crossFade().diskCacheStrategy(DiskCacheStrategy.RESULT).into(image);
                break;

            case "vimeo":
                Glide.with(getActivity()).load(mSliderList.get(postion).getLatestVideoImgBig()).placeholder(R.drawable.loading).centerCrop().crossFade().diskCacheStrategy(DiskCacheStrategy.RESULT).into(image);
                break;
            case "embed":
                Glide.with(getActivity()).load(mSliderList.get(postion).getLatestVideoImgBig()).placeholder(R.drawable.loading).centerCrop().crossFade().diskCacheStrategy(DiskCacheStrategy.RESULT).into(image);
                break;
        }*/
        //handling click event
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           /*     Intent intent = new Intent(getActivity(), ImageDetailsActivity.class);
                intent.putExtra(DRAWABLE_RESOURE, imageArray[postion]);
                startActivity(intent);*/
            }
        });

        rootLayout.setScaleBoth(scale);

        return linearLayout;
    }

    /**
     * Get device screen width and height
     */
    private void getWidthAndHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;
    }
}