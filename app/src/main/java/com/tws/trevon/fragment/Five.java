package com.tws.trevon.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tws.trevon.R;
import com.tws.trevon.activity.AbstractActivity;
import com.tws.trevon.common.CallApi;


public class Five extends AbstractFragment
{
    View view;
    LinearLayout gmail_click;
    LinearLayout open_call_dailer;


    public Five() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_five, container, false);
        gmail_click = view.findViewById(R.id.gmail_click);
        open_call_dailer = view.findViewById(R.id.open_call_dailer);
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
        }
    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {

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
}