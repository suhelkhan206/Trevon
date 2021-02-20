package com.tws.trevon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tws.trevon.R;
import com.tws.trevon.co.FaqCOO;
import com.tws.trevon.co.ProductCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.WebViewAppConfig;

public class FaqSinglePage extends AppCompatActivity
{
    View view;
    WebView faq_video;
    FaqCOO faqCOO;
    TextView faq_title,faq_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_single_page);

        Toolbar toolbar = findViewById(R.id.faq_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Faqs");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        faq_video = findViewById(R.id.faq_video);
        faq_title = findViewById(R.id.faq_title);
        faq_description = findViewById(R.id.faq_description);
        Intent intent = getIntent();
        String productCOString = intent.getExtras().getString("faq");
        faqCOO = AppController.gson.fromJson(productCOString, FaqCOO.class);

        faq_title.setText(faqCOO.question);
        faq_description.setText(faqCOO.answer);
        //webViewInitialize();
        if(AppValidate.isNotEmpty(faqCOO.video))
        {
            faq_video.setVisibility(View.VISIBLE);
        }
        else
        {
            faq_video.setVisibility(View.GONE);
        }
        updateVideo();


    }

    @Override
    public void onPause() {
        super.onPause();
        faq_video.onPause();
    }


    public void updateVideo()
    {
        faq_video.setWebViewClient(new Browser_Home());
        faq_video.setWebChromeClient(new ChromeClient());
        WebSettings webSettings = faq_video.getSettings();

        faq_video.getSettings().setJavaScriptEnabled(true);
        //Set whether the DOM storage API is enabled.
        faq_video.getSettings().setDomStorageEnabled(true);
        //setBuiltInZoomControls = false, removes +/- controls on screen
        faq_video.getSettings().setBuiltInZoomControls(false);
        faq_video.getSettings().setPluginState(WebSettings.PluginState.ON);
        faq_video.getSettings().setAllowFileAccess(true);
        faq_video.getSettings().setAppCacheMaxSize(1024 * 8);
        faq_video.getSettings().setAppCacheEnabled(true);
        faq_video.getSettings().setAllowContentAccess(true);

        faq_video.getSettings().setUseWideViewPort(true);


        // these settings speed up page load into the webview
        faq_video.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        faq_video.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        faq_video.requestFocus(View.FOCUS_DOWN);


        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);


        faq_video.getSettings().setAppCacheEnabled(true);
        faq_video.getSettings().setDomStorageEnabled(true);


        faq_video.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        String iframe = "<iframe width=\"300\" height=\"250\" src=\"https://www.youtube.com/embed/55VL0i-5DMc\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>";

        String data_html = "<!DOCTYPE html><html> <head> <meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"target-densitydpi=high-dpi\" /> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> <link rel=\"stylesheet\" media=\"screen and (-webkit-device-pixel-ratio:1.5)\" href=\"hdpi.css\" /></head> <body style=\"background:black;margin:0 0 0 0; padding:0 0 0 0;\"> "+iframe+" </body> </html> ";

        faq_video.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        faq_video.loadData(data_html, "text/html", "utf-8");
    }


    private class Browser_Home extends WebViewClient {
        Browser_Home(){}

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    private class ChromeClient extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        ChromeClient() {}

        public Bitmap getDefaultVideoPoster()
        {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView()
        {
            ((FrameLayout)getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
        {
            if (this.mCustomView != null)
            {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }


    private void webViewInitialize()
    {
        faq_video.setInitialScale(1);
        faq_video.setWebChromeClient(new WebChromeClient());
        faq_video.getSettings().setAllowFileAccess(true);
        faq_video.getSettings().setJavaScriptEnabled(true);
        faq_video.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        faq_video.setWebChromeClient(new WebChromeClient());
        faq_video.getSettings().setLoadsImagesAutomatically(true);
        faq_video.getSettings().setAllowFileAccess(true);
        faq_video.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        faq_video.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        faq_video.getSettings().setPluginState(WebSettings.PluginState.ON);
        faq_video.getSettings().setMediaPlaybackRequiresUserGesture(false);
        faq_video.getSettings().setDomStorageEnabled(true);
        faq_video.getSettings().setAppCacheMaxSize(1024 * 8);
        faq_video.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        faq_video.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        faq_video.requestFocus(View.FOCUS_DOWN);
        faq_video.getSettings().setAppCacheEnabled(true);

        String iframe = "<iframe width=\"300\" height=\"250\" src=\"https://www.youtube.com/embed/55VL0i-5DMc\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>";

        String data_html = "<!DOCTYPE html><html> <head> <meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"target-densitydpi=high-dpi\" /> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> <link rel=\"stylesheet\" media=\"screen and (-webkit-device-pixel-ratio:1.5)\" href=\"hdpi.css\" /></head> <body style=\"background:black;margin:0 0 0 0; padding:0 0 0 0;\"> "+iframe+" </body> </html> ";

        faq_video.loadDataWithBaseURL("https://www.youtube.com", data_html, "text/html", "UTF-8", null);
    }
}