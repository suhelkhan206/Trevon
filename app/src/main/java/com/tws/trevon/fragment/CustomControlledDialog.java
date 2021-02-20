package com.tws.trevon.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.tws.trevon.R;
import com.tws.trevon.co.DialogCO;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.common.Utilities;
import com.tws.trevon.constants.IConstants;

public class CustomControlledDialog extends AbstractDialogFragment implements View.OnClickListener
{
    private OnDialogFragmentListener mListener;
    private DialogCO dialogCO;

    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view;

        dialogCO = getArguments().getParcelable(IConstants.OBJECT);

        if(DialogCO.ALERT_DIALOG_TYPE.equals(dialogCO.type))
        {
            view = inflater.inflate(R.layout.f_custom_controlled_alert_dialog, container, false);
        }
        else if(DialogCO.POSITIVE_DIALOG_TYPE.equals(dialogCO.type))
        {
            view = inflater.inflate(R.layout.f_custom_controlled_positive_message_dialog, container, false);
        }
        else
        {
            view = inflater.inflate(R.layout.f_custom_controlled_message_dialog, container, false);
        }

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setCancelable(Utilities.isTrue(dialogCO.dialogCancelable));

        if(AppValidate.isEmpty(dialogCO.title))
        {
            view.findViewById(R.id.cd_tv_title).setVisibility(View.GONE);
        }
        else
        {
            ((TextView)view.findViewById(R.id.cd_tv_title)).setText(dialogCO.title);
        }

        if(AppValidate.isEmpty(dialogCO.description))
        {
            view.findViewById(R.id.cd_tv_description).setVisibility(View.GONE);
        }
        else
        {
            ((TextView)view.findViewById(R.id.cd_tv_description)).setText(dialogCO.description);
        }

        if(AppValidate.isNotEmpty(dialogCO.okButtonTitle))
        {
            ((TextView)view.findViewById(R.id.cd_tv_ok)).setText(dialogCO.okButtonTitle);
        }

        if(Utilities.isTrue(dialogCO.negativeButtonRequired))
        {
            view.findViewById(R.id.cd_tv_cancel).setVisibility(View.VISIBLE);

            if(AppValidate.isNotEmpty(dialogCO.negativeButtonTitle))
            {
                ((TextView)view.findViewById(R.id.cd_tv_cancel)).setText(dialogCO.negativeButtonTitle);
            }
        }
        else
        {
            view.findViewById(R.id.cd_tv_cancel).setVisibility(View.GONE);
        }

        view.findViewById(R.id.cd_tv_ok).setOnClickListener(this);
        view.findViewById(R.id.cd_tv_cancel).setOnClickListener(this);

        return view;
    }

    @Override
    protected void onViewClick(View view)
    {
        switch (view.getId())
        {
            case R.id.cd_tv_ok:
                if(mListener == null)
                {
                    processCustomAction(dialogCO.okButtonActionType, dialogCO.okButtonActionJsonData);
                }
                else
                {
                    mListener.onPositiveAction(dialogCO.okButtonActionJsonData);
                }

                dismiss();
                break;

            case R.id.cd_tv_cancel:
                if(mListener == null)
                {
                    processCustomAction(dialogCO.negativeButtonActionType, dialogCO.negativeButtonActionJsonData);
                }
                else
                {
                    mListener.onNegativeAction(dialogCO.negativeButtonActionJsonData);
                }

                dismiss();
                break;
        }
    }

    private void processCustomAction(final String type, final Object data)
    {
        if(AppValidate.isNotEmpty(type))
        {
            switch(type)
            {
                case DialogCO.LINK_ACTION_TYPE:
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    /*intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.youtube"));*/
                    intent.setData(Uri.parse(data.toString()));
                    startActivity(intent);
                    break;
            }
        }
    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {

    }

    public void setOnDialogClickListener(OnDialogFragmentListener mListener)
    {
        this.mListener = mListener;
    }

    public interface OnDialogFragmentListener
    {
        void onPositiveAction(final Object data);
        void onNegativeAction(final Object data);
    }
}