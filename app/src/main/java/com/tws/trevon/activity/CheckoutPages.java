package com.tws.trevon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.Order;
import com.razorpay.PaymentResultListener;
import com.razorpay.RazorpayClient;
import com.tws.trevon.R;
import com.tws.trevon.co.CheckoutCO;
import com.tws.trevon.co.CheckoutCO;
import com.tws.trevon.co.MyAddressCO;
import com.tws.trevon.co.ProductCO;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.API;
import com.tws.trevon.constants.IConstants;
import com.tws.trevon.constants.IReqParams;
import com.tws.trevon.constants.ISysConfig;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CheckoutPages extends AbstractActivity  implements PaymentResultListener
{
    RelativeLayout payment_upi,payment_by_card,payment_cod, payment_wallet;
    RadioButton payment_upi_radio_id,payment_online_radio_id,
            payment_cod_radio_id, payment_wallet_amount_image_radio_id;

    public String SELECTED_PAYMENT_MODE = "";
    RelativeLayout payment_button;
    RazorpayClient razorpayClient;
    Order order=null;
    CheckoutCO productCO;
    MyAddressCO myAddressCO;

    TextView payment_total_amount_order,payment_wallet_text;
    final int UPI_PAYMENT = 0;
    Float finalPrice;
    Float walet_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_pages);
        Toolbar toolbar = findViewById(R.id.checkout_tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Checkout");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        payment_upi = findViewById(R.id.payment_upi);
        payment_by_card = findViewById(R.id.payment_by_card);
        payment_cod = findViewById(R.id.payment_cod);
        payment_wallet = findViewById(R.id.payment_wallet);
        payment_upi_radio_id = findViewById(R.id.payment_upi_radio_id);
        payment_online_radio_id = findViewById(R.id.payment_online_radio_id);
        payment_cod_radio_id = findViewById(R.id.payment_cod_radio_id);
        payment_wallet_amount_image_radio_id= findViewById(R.id.payment_wallet_amount_image_radio_id);
        payment_button = findViewById(R.id.payment_button);
        payment_total_amount_order = findViewById(R.id.payment_total_amount_order);
        payment_wallet_text = findViewById(R.id.payment_wallet_text);
        payment_button.setOnClickListener(this);
        payment_upi.setOnClickListener(this);
        payment_by_card.setOnClickListener(this);
        payment_cod.setOnClickListener(this);
        payment_wallet.setOnClickListener(this);
        Checkout.preload(getApplicationContext());
        try
        {
            razorpayClient = new RazorpayClient(ISysConfig.RAZOR_PAY_API_KEY, ISysConfig.RAZOR_PAY_API_SECRET_KEY);
        }
        catch (Exception e)
        {

        }

        Intent intent = getIntent();
        String productCOString = intent.getExtras().getString("productCO");
        String addressCOString = intent.getExtras().getString("addressCO");
        productCO = AppController.gson.fromJson(productCOString, CheckoutCO.class);
        myAddressCO = AppController.gson.fromJson(addressCOString, MyAddressCO.class);
        payment_cod.setVisibility(View.VISIBLE);

      /*  if(productCO.pincode_status.equals("Y"))
        {
            payment_cod.setVisibility(View.VISIBLE);
        }
        else
        {
            payment_cod.setVisibility(View.GONE);
        }*/
        finalPrice = Float.parseFloat(productCO.totalPrice);
        payment_total_amount_order.setText(IConstants.RUPEE_ICON+productCO.totalPrice);

        walet_amount = Float.parseFloat(UserCO.getUserCOFromDB().wallet_amount);

        if(walet_amount >= finalPrice )
        {
            payment_wallet_text.setTextColor(Color.parseColor("#303b41"));
            payment_wallet_amount_image_radio_id.setEnabled(true);
            //Toast.makeText(this, "wallet enable", Toast.LENGTH_SHORT).show();
        }
        else
        {
            payment_wallet_text.setTextColor(Color.parseColor("#d9d9d9"));
            payment_wallet_amount_image_radio_id.setEnabled(false);
           // Toast.makeText(this, "wallet disable", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    protected void onViewClick(View view)
    {
        switch(view.getId())
        {
            case R.id.payment_upi:
                SELECTED_PAYMENT_MODE = "UPI";
                payment_upi_radio_id.setChecked(true);
                payment_online_radio_id.setChecked(false);
                payment_cod_radio_id.setChecked(false);
                payment_wallet_amount_image_radio_id.setChecked(false);
                AsyncTaskRunner runnerupi = new AsyncTaskRunner();
                runnerupi.execute("");

                break;

            case R.id.payment_by_card:
                SELECTED_PAYMENT_MODE = "ONLINE";
                payment_upi_radio_id.setChecked(false);
                payment_online_radio_id.setChecked(true);
                payment_cod_radio_id.setChecked(false);
                payment_wallet_amount_image_radio_id.setChecked(false);
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute("");

                break;

            case R.id.payment_cod:
                SELECTED_PAYMENT_MODE = "COD";
                payment_upi_radio_id.setChecked(false);
                payment_online_radio_id.setChecked(false);
                payment_cod_radio_id.setChecked(true);
                payment_wallet_amount_image_radio_id.setChecked(false);
                break;

            case R.id.payment_wallet:

                if(walet_amount >= finalPrice )
                {
                    SELECTED_PAYMENT_MODE = "WALLET";
                    payment_upi_radio_id.setChecked(false);
                    payment_online_radio_id.setChecked(false);
                    payment_cod_radio_id.setChecked(false);
                    payment_wallet_amount_image_radio_id.setChecked(true);
                }

                break;


            case R.id.payment_button:
                if(SELECTED_PAYMENT_MODE.equals("UPI"))
                {
                    CheckoutCO checkOutOrderCO = calculcatePayment("UPI");
                    payUsingUpi( "Suhel","8233338206@upi", "send money", String.valueOf(finalPrice));//String.valueOf(totalAmount)
                }
                else if(SELECTED_PAYMENT_MODE.equals("ONLINE"))
                {
                    if(AppValidate.isEmpty((String)order.get("id")))
                    {
                        Toast.makeText(this, "Order not Generate Try Again", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        startPayment((String)order.get("id"));
                    }
                }
                else if(SELECTED_PAYMENT_MODE.equals("COD"))
                {
                    try
                    {
                        CheckoutCO checkOutOrderCO = calculcatePayment("COD");
                        // orderCO.orderId = (String)order.get("id");//(String)order.get("id")
                        String orderJson = AppController.gson.toJson(checkOutOrderCO);

                        CallApi callApi = new CallApi(API.add_order);
                        callApi.addReqParams("orderCO", orderJson);
                        processCallApi(callApi);

                    } catch (Exception e)
                    {
                        Log.e("Payment", "Exception in onPaymentSuccess", e);
                    }
                }
                else if(SELECTED_PAYMENT_MODE.equals("WALLET"))
                {
                    try
                    {
                        CheckoutCO checkOutOrderCO = calculcatePayment("WALLET");
                        // orderCO.orderId = (String)order.get("id");//(String)order.get("id")
                        String orderJson = AppController.gson.toJson(checkOutOrderCO);

                        CallApi callApi = new CallApi(API.add_order);
                        callApi.addReqParams("orderCO", orderJson);
                        processCallApi(callApi);

                    } catch (Exception e)
                    {
                        Log.e("Payment", "Exception in onPaymentSuccess", e);
                    }
                }
                else
                {
                    Toast.makeText(this, "Please select any payment method", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi)
    {
        if(API.add_order.method.equals(callApi.networkActivity.method))
        {
            AppUtilities.writeToPref(IReqParams.USER_CART_COUNT,"0");
            //Toast.makeText(this, "Payment Successful" + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            Intent i =new Intent(CheckoutPages.this,ThankYouScreen.class);
            startActivity(i);
            finish();
        }
    }


    private CheckoutCO calculcatePayment(String type)
    {
       /* if(isCouponApplied)
        {
            orderCO.is_coupon_apply = "1";
        }
        else
        {
            orderCO.is_coupon_apply = "0";
        }*/
        // orderCO.loanSchemeId = schemeId;

     /*   if(SELECTED_PAYMENT_MODE.equals("RAZOR_PAY"))
        {
            float calculatePrice = (finalPrice/100)*2;
            float final_price = calculatePrice+finalPrice;
            orderCO.totalAmount = String.valueOf(final_price);
        }
        else
        {*/

       // }
        productCO.payment_mode = type;
        productCO.address_id = myAddressCO.id;
        productCO.user_id = UserCO.getUserCOFromDB().id;
       // orderCO.merchant_id = productCO.merchant_id;
       // orderCO.product_id = productCO.product_id;
       //     orderCO.size = productCO.selectedSize;
       // float pro_price = Float.parseFloat(productCO.product_price);
       // float inpu_price = Float.parseFloat(productCO.finalCustomerPrice);
      //  float margin_earns = (inpu_price-pro_price);
       // int qty = Integer.parseInt(productCO.quantity);
     //   float totalmargine = margin_earns*qty;
      //  orderCO.margin = ""+totalmargine;
        // orderCO.from_wallet = productCO.;
        // orderCO.coupon_discount = productCO.product_id;
       // orderCO.shipping_charge = productCO.shipping_charge;
        //orderCO.total_paid =String.valueOf(finalPrice);
        //orderCO.stock_id = productCO.SelectedSizeID;


        return productCO;
    }


    class AsyncTaskRunner extends AsyncTask<String, String, String>
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

    public Order generateOrderId()
    {
        Order order=null;

        try {
            JSONObject orderRequest = new JSONObject();
           /* if(paymentOption.equals("RAZOR_PAY"))
            {
                float calculatePrice = (orderPrice/100)*2;
                float final_price = calculatePrice+orderPrice;
                orderRequest.put("amount", final_price*100); // amount in the smallest currency unit
                //  payment_total_amount_order.setText(IConstants.RUPEE_ICON+""+final_price);
            }
            else
            {
                //  payment_total_amount_order.setText(IConstants.RUPEE_ICON+orderPrice);
                orderRequest.put("amount", orderPrice*100); // amount in the smallest currency unit

            }*/

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
        CheckoutCO checkOutOrderCO = calculcatePayment("ONLINE");
        checkOutOrderCO.order_no = razorpayPaymentID;//(String)order.get("id")
        String orderJson = AppController.gson.toJson(checkOutOrderCO);


        CallApi callApi = new CallApi(API.add_order);
        callApi.addReqParams("orderCO", orderJson);
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



    /////////////UPI//////////////////

    void payUsingUpi(  String name,String upiId, String note, String amount)
    {
        //Log.e("main ", "name "+name +"--up--"+upiId+"--"+ note+"--"+amount);
        // main: name pavan n--up--pavan.n.sap@okaxis--Test UPI Payment--5.00
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                //.appendQueryParameter("mc", "")
                //.appendQueryParameter("tid", "02125412")
                .appendQueryParameter("tr", "25584584")
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                //.appendQueryParameter("refUrl", "blueapp")
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if(null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(CheckoutPages.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response "+resultCode );
        /*
       E/main: response -1
       E/UPI: onActivityResult: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPIPAY: upiPaymentDataOperation: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPI: payment successfull: 922118921612
         */
        switch (requestCode)
        {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11))
                {
                    if (data != null)
                    {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;

        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(CheckoutPages.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success"))
            {
                //Code to handle successful transaction here.
               /* Toast.makeText(CheckOutPage.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "payment successfull: "+approvalRefNo);*/

                try
                {
                    CheckoutCO checkOutOrderCO = calculcatePayment("UPI");
                    checkOutOrderCO.order_no = approvalRefNo;//(String)order.get("id")
                    String orderJson = AppController.gson.toJson(checkOutOrderCO);


                    CallApi callApi = new CallApi(API.add_order);
                    callApi.addReqParams("orderCO", orderJson);
                    processCallApi(callApi);



                } catch (Exception e) {
                    Log.e("Payment", "Exception in onPaymentSuccess", e);
                }
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(CheckoutPages.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: "+approvalRefNo);

            }
            else {
                Toast.makeText(CheckoutPages.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: "+approvalRefNo);

            }
        } else {
            Log.e("UPI", "Internet issue: ");

            Toast.makeText(CheckoutPages.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }


}


