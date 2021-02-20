package com.tws.trevon.activity.sp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tws.trevon.R;
import com.tws.trevon.co.ImageCO;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.fragment.AbstractFragment;
import com.tws.trevon.fragment.FragmentImageSlider;


import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentTransaction;
import de.hdodenhof.circleimageview.CircleImageView;


public class Help extends AbstractFragment {

    CircleImageView user_image;
    TextView user_name,user_phone_number, wallet_amount;
    ImageView crown;

    LinearLayout gmail_click;
    LinearLayout open_call_dailer;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_help, container, false);

        user_image = view.findViewById(R.id.user_image);
        user_name = view.findViewById(R.id.user_name);
        user_phone_number = view.findViewById(R.id.user_phone_number);
        wallet_amount = view.findViewById(R.id.wallet_amount);
        user_image.setOnClickListener(this);
        crown = view.findViewById(R.id.crown);
        if(UserCO.getUserCOFromDB().is_premium.equals("1"))
        {
            crown.setVisibility(View.VISIBLE);
        }
        else
        {
            crown.setVisibility(View.GONE);
        }

        user_name.setText(UserCO.getUserCOFromDB().username);
        user_phone_number.setText(UserCO.getUserCOFromDB().mobile);
        wallet_amount.setText(UserCO.getUserCOFromDB().wallet_amount);

        Glide.with(Help.this)
                .load(UserCO.getUserCOFromDB().image)
                .apply(RequestOptions.circleCropTransform().placeholderOf(R.drawable.error_images).error(R.drawable.error_images))
                .dontAnimate()
                .into(user_image);

        gmail_click = view.findViewById(R.id.gmail_click);
        open_call_dailer =view.findViewById(R.id.open_call_dailer);
        gmail_click.setOnClickListener(this);
        open_call_dailer.setOnClickListener(this);

        return view;
    }

    @Override
    protected void onViewClick(View view) {
        switch(view.getId()) {
            case R.id.gmail_click:
                openGmail();
                break;
            case R.id.open_call_dailer:
                openCallDaileer();
                break;
            case R.id.user_image:
                List<ImageCO> imageCOList = new ArrayList<>();
                ImageCO imageCO = new ImageCO();
                imageCO.url = UserCO.getUserCOFromDB().image;
                imageCO.type = "image";
                imageCOList.add(imageCO);

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("images", new ArrayList(imageCOList));
                bundle.putString("isPdfShow","no");
                bundle.putInt("position", 0);
                bundle.putInt("type",2);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentImageSlider newFragment = FragmentImageSlider.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
                break;

        }
    }

    public void openGmail()
    {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"help@trevon.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "App feedback");
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There are no email client installed on your device.", Toast.LENGTH_SHORT).show();
        }
    }

    public void openCallDaileer()
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:9517536542"));
        startActivity(intent);
    }


    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {

    }
}