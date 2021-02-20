package com.tws.trevon.activity;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tws.trevon.R;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppValidate;

public class RefundPolicy extends AppCompatActivity {

    WebView web_popup_view;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_policy);
        web_popup_view = findViewById(R.id.web_view);

        Toolbar dash_board_toolbar = findViewById(R.id.refund_policy_tool);
        setSupportActionBar(dash_board_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        dash_board_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        web_popup_view.setWebViewClient(new WebViewClient());
        web_popup_view.getSettings().setJavaScriptEnabled(true);
        web_popup_view.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        web_popup_view.getSettings().setPluginState(WebSettings.PluginState.ON);
        web_popup_view.getSettings().setMediaPlaybackRequiresUserGesture(false);
        web_popup_view.setWebChromeClient(new WebChromeClient());
        UserCO userCO =  UserCO.getUserCOFromDB();
        if(AppValidate.isNotEmpty(userCO.id))
        {
            web_popup_view.loadUrl(UserCO.getUserCOFromDB().web_refund_policy);
        }
        else
        {
            web_popup_view.loadUrl("https://www.twstechnology.com/grocery/FrontEndPages/web_refund_policy");
        }


    }

    @Override
    public void onBackPressed()
    {
        if(web_popup_view!= null && web_popup_view.canGoBack())
            web_popup_view.goBack();// if there is previous page open it
        else
            super.onBackPressed();//if there is no previous page, close app
    }
}
