package com.tws.trevon.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.tws.trevon.R;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.IConstants;

public class CustomProgressDialog extends AbstractDialogFragment implements View.OnClickListener
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public static CustomProgressDialog initializeCustomDialog(final FragmentManager fragmentManager)
    {
        CustomProgressDialog customProgressDialog = new CustomProgressDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IConstants.BOOLEAN, false);
        customProgressDialog.setArguments(bundle);
        customProgressDialog.show(fragmentManager, IConstants.EMPTY_STRING);
        return customProgressDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.f_custom_progress_dialog, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if(getArguments() != null)
        {
            setCancelable(getArguments().getBoolean(IConstants.BOOLEAN, Boolean.TRUE));
        }

        return view;
    }

    @Override
    protected void onViewClick(View view)
    {
        switch (view.getId())
        {

        }
    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {

    }
}