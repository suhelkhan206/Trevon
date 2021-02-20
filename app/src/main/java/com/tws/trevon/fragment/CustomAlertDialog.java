package com.tws.trevon.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.tws.trevon.R;

import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.IConstants;

public class CustomAlertDialog extends AbstractDialogFragment implements View.OnClickListener
{
    private OnDialogFragmentListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.f_custom_alert_dialog, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if(getArguments() != null)
        {
            setCancelable(getArguments().getBoolean(IConstants.BOOLEAN, Boolean.TRUE));

            if(AppValidate.isEmpty(getArguments().getString(IConstants.DIALOG_TITLE, IConstants.EMPTY_STRING)))
            {
                view.findViewById(R.id.cd_tv_title).setVisibility(View.GONE);
            }
            else
            {
                ((TextView)view.findViewById(R.id.cd_tv_title)).setText(getArguments().getString(IConstants.DIALOG_TITLE, IConstants.EMPTY_STRING));
            }

            if(AppValidate.isEmpty(getArguments().getString(IConstants.DESCRIPTION, IConstants.EMPTY_STRING)))
            {
                view.findViewById(R.id.cd_tv_description).setVisibility(View.GONE);
            }
            else
            {
                ((TextView)view.findViewById(R.id.cd_tv_description)).setText(getArguments().getString(IConstants.DESCRIPTION, IConstants.EMPTY_STRING));
            }

            if(AppValidate.isNotEmpty(getArguments().getString(IConstants.OK_BUTTON_TEXT, IConstants.EMPTY_STRING)))
            {
                ((TextView)view.findViewById(R.id.cd_tv_ok)).setText(getArguments().getString(IConstants.OK_BUTTON_TEXT, IConstants.EMPTY_STRING));
            }

            view.findViewById(R.id.cd_tv_ok).setOnClickListener(this);
            view.findViewById(R.id.cd_tv_cancel).setOnClickListener(this);
        }

        return view;
    }


    public void setOnDialogClickListener(OnDialogFragmentListener mListener)
    {
        this.mListener = mListener;
    }

    @Override
    protected void onViewClick(View view)
    {
        switch (view.getId())
        {
            case R.id.cd_tv_ok:
                if(mListener != null)
                {
                    mListener.onPositiveAction();
                }

                dismiss();
                break;

            case R.id.cd_tv_cancel:
                if(mListener != null)
                {
                    mListener.onNegativeAction();
                }

                dismiss();
                break;
        }
    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {

    }

    public interface OnDialogFragmentListener
    {
        void onPositiveAction();
        void onNegativeAction();
    }
}