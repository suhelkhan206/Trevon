package com.tws.trevon.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tws.trevon.R;
import com.tws.trevon.common.CallApi;

public class ThankYouScreen extends AbstractActivity {
    TextView order_number,delivery_date;
    LinearLayout track_order_btn;
    LinearLayout continue_shopping_btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you_screen);
        order_number = findViewById(R.id.order_number);
        delivery_date = findViewById(R.id.delivery_date);
        track_order_btn = findViewById(R.id.track_order_btn);
        continue_shopping_btn = findViewById(R.id.continue_shopping_btn);
        track_order_btn.setOnClickListener(this);
        continue_shopping_btn.setOnClickListener(this);
    }


    @Override
    protected void onViewClick(View view) {

        switch(view.getId()) {


            case R.id.track_order_btn:
                Intent i2 = new Intent(ThankYouScreen.this, MyOrderList.class);
                i2.putExtra("isSwipe","true");
                startActivity(i2);
                finish();
                break;


            case R.id.continue_shopping_btn:
                Intent i = new Intent(ThankYouScreen.this, MainActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }
    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {

    }
}