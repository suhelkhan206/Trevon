package com.tws.trevon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentTransaction;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.razorpay.Checkout;
import com.razorpay.Order;
import com.razorpay.PaymentResultListener;
import com.razorpay.RazorpayClient;
import com.tws.trevon.R;
import com.tws.trevon.co.CheckoutCO;
import com.tws.trevon.co.ImageCO;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.API;
import com.tws.trevon.constants.IConstants;
import com.tws.trevon.constants.IReqParams;
import com.tws.trevon.constants.ISysConfig;
import com.tws.trevon.fragment.FragmentImageSlider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class EliteAuthorActivity extends AbstractActivity implements PaymentResultListener {

    CircleImageView user_image;
    TextView user_name,user_phone_number, wallet_amount;
    LinearLayout ll_select_user_profile;
    ImageView crown;
    LinearLayout proceed_to_sub_button;
    RazorpayClient razorpayClient;
    Order order=null;
    Float finalPrice = 1500f;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elite_author);
        Toolbar toolbar = findViewById(R.id.elite_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Become a Elite Member");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        Checkout.preload(getApplicationContext());
        try
        {
            razorpayClient = new RazorpayClient(ISysConfig.RAZOR_PAY_API_KEY, ISysConfig.RAZOR_PAY_API_SECRET_KEY);
        }
        catch (Exception e)
        {

        }

        crown = findViewById(R.id.crown);
        if(UserCO.getUserCOFromDB().is_premium.equals("1"))
        {
            crown.setVisibility(View.VISIBLE);
        }
        else
        {
            crown.setVisibility(View.GONE);
        }

        ll_select_user_profile = findViewById(R.id.ll_select_user_profile);
        ll_select_user_profile.setVisibility(View.GONE);
        user_image = findViewById(R.id.user_image);
        user_name = findViewById(R.id.user_name);
        user_phone_number = findViewById(R.id.user_phone_number);
        wallet_amount = findViewById(R.id.wallet_amount);
        proceed_to_sub_button = findViewById(R.id.proceed_to_sub_button);
        proceed_to_sub_button.setOnClickListener(this);
        user_image.setOnClickListener(this);
        user_name.setText(UserCO.getUserCOFromDB().username);
        user_phone_number.setText(UserCO.getUserCOFromDB().mobile);
        wallet_amount.setText(UserCO.getUserCOFromDB().wallet_amount);

        Glide.with(EliteAuthorActivity.this)
                .load(UserCO.getUserCOFromDB().image)
                .apply(RequestOptions.circleCropTransform().placeholderOf(R.drawable.error_images).error(R.drawable.error_images))
                .dontAnimate()
                .into(user_image);

        AsyncTaskRunner runnerupi = new AsyncTaskRunner();
        runnerupi.execute("");

    }

    @Override
    protected void onViewClick(View view)
    {
        switch(view.getId()) {
            case R.id.proceed_to_sub_button:
                startPayment((String)order.get("id"));
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

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                FragmentImageSlider newFragment = FragmentImageSlider.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
                break;

        }
    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {

        if(API.make_premium.method.equals(callApi.networkActivity.method))
        {
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            finish();
        }
    }



    private class AsyncTaskRunner extends AsyncTask<String, String, String>
    {
        private String resp;

        @Override
        protected String doInBackground(String... params)
        {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            order=generateOrderId();
            return resp;
        }

        @Override
        protected void onPostExecute(String result)
        {
            // Toast.makeText(FirstOrderProductScreen.this, ""+order, Toast.LENGTH_SHORT).show();
        }


        @Override
        protected void onPreExecute()
        {

        }
    }

    public com.razorpay.Order generateOrderId()
    {
        Order order=null;

        try {
            JSONObject orderRequest = new JSONObject();


            orderRequest.put("amount", finalPrice*100); // amount in the smallest currency unit
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "1");
            orderRequest.put("payment_capture", false);

            order = razorpayClient.Orders.create(orderRequest);
        }
        catch (Exception e)
        {
            // Handle Exception
            // Toast.makeText(this, "RetryPaymentFail", Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
        }
        Log.e("orderId",""+order);
        return order;
    }


    public void startPayment(String orderId)
    {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;
        final Checkout  co = new Checkout();

        try
        {
            JSONObject options = new JSONObject();
            options.put("name", "Trevon");
            options.put("description", "The Power of YES");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("order_id", orderId);
          /*  if(paymentOption.equals("RAZOR_PAY"))
            {
                float calculatePrice = (orderPrice/100)*2;
                float final_price = calculatePrice+orderPrice;
                options.put("amount", final_price*100);
            }
            else
            {
                options.put("amount", orderPrice*100);
            }*/
            options.put("amount", finalPrice*100);
            JSONObject preFill = new JSONObject();
            preFill.put("email", UserCO.getUserCOFromDB().email);
            preFill.put("contact", UserCO.getUserCOFromDB().mobile);
            options.put("prefill", preFill);

            co.open(activity, options);
        }
        catch (Exception e)
        {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(final String razorpayPaymentID)
    {
        try
        {

            Timer timer=new Timer();
            final long DELAY = 100;

            timer.cancel();
            timer = new Timer();
            timer.schedule(
                    new TimerTask()
                    {
                        @Override
                        public void run()
                        {
                            orderApiCall(razorpayPaymentID);
                        }
                    },
                    DELAY
            );


        } catch (Exception e) {
            Log.e("Payment", "Exception in onPaymentSuccess", e);
        }
    }


    public void orderApiCall(final String razorpayPaymentID)
    {
        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();

        CallApi callApi = new CallApi(API.make_premium);
        callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
        processCallApi(callApi);
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("Payment", "Exception in onPaymentError", e);
        }
    }



    TextView textCartItemCount;
    int mCartItemCount;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_option_menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        MenuItem  menuItem2 = menu.findItem(R.id.notification_home);
        menuItem2.setVisible(false);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        if(!AppValidate.isEmpty(AppUtilities.readFromPref(IReqParams.USER_CART_COUNT)))
        {
            mCartItemCount = Integer.parseInt(AppUtilities.readFromPref(IReqParams.USER_CART_COUNT));
            setupBadge();
        }

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        if (item.getItemId()==R.id.action_search)
        {
            Intent i= new Intent(EliteAuthorActivity.this, SearchPage.class);
            startActivity(i);
        }

        else if (item.getItemId()==R.id.notification_home)
        {
            UserCO userCO =  UserCO.getUserCOFromDB();
            if(AppValidate.isNotEmpty(userCO.id))
            {
                Intent i =new Intent(EliteAuthorActivity.this,Notification.class);
                startActivityForResult(i, IConstants.INTENT_CART_COUNT_CODE);// Activity is started with requestCode 2
            }
            else
            {
                Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
                Intent ilogin = new Intent(EliteAuthorActivity.this,LoginOriginal.class);
                ilogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(ilogin);
                finish();
            }

        }
        else if (item.getItemId()==R.id.action_cart)
        {
            UserCO userCO =  UserCO.getUserCOFromDB();
            if(AppValidate.isNotEmpty(userCO.id))
            {
                Intent i =new Intent(EliteAuthorActivity.this,CartScreen.class);
                startActivityForResult(i, IConstants.INTENT_CART_COUNT_CODE);// Activity is started with requestCode 2
            }
            else
            {
                Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
                Intent ilogin = new Intent(EliteAuthorActivity.this,LoginOriginal.class);
                ilogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(ilogin);
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupBadge()
    {
        if (textCartItemCount != null)
        {
            if (mCartItemCount == 0)
            {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

}