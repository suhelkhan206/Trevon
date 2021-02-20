package com.tws.trevon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.tws.trevon.R;
import com.tws.trevon.adapter.BuyingOptionAdapter;
import com.tws.trevon.adapter.CategroyParentAdapter;
import com.tws.trevon.adapter.FeatureProductAdapter;
import com.tws.trevon.adapter.FeatureProductAdapterHorizontal;
import com.tws.trevon.adapter.NotificationAdapter;
import com.tws.trevon.adapter.ProductFiIlterAdapter;
import com.tws.trevon.adapter.SliderAdapter;
import com.tws.trevon.co.ImageCO;
import com.tws.trevon.co.ProductCO;
import com.tws.trevon.co.ProductSizeCO;
import com.tws.trevon.co.SliderCO;
import com.tws.trevon.co.StockCO;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.common.ItemOffsetDecoration;
import com.tws.trevon.common.PicassoImageGetter;
import com.tws.trevon.constants.API;
import com.tws.trevon.constants.IConstants;
import com.tws.trevon.constants.IReqParams;
import com.tws.trevon.fragment.FragmentImageSlider;
import com.tws.trevon.fragment.PopupBottomDailogFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SingleProductScreen extends AbstractActivity implements AdapterView.OnItemSelectedListener, PopupBottomDailogFragment.ItemClickListener
{
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    SliderAdapter sliderAdapter;
    TextView product_title, sale_price, original_price;
    TextView product_description_content;
    EditText enter_pin_code;
    LinearLayout change_pincode;
    RecyclerView buying_option_recycler_view;
    String  productId;
    ProductCO productCO;
    LinearLayout product_container;
    LinearLayout buy_now;
    ImageView product_hidden_image;
    FeatureProductAdapterHorizontal featureProductAdapter;
    TextView pin_change_text;
    public String pincode_status= null;
    ArrayList<String> doc_type_list = new ArrayList<>();
    BuyingOptionAdapter buyingOptionAdapter;
    TextView margine_value,min_qty;
    TextView textCartItemCount;
    int mCartItemCount;
    TextView tag_line;
    RecyclerView product_filter;

    ProductFiIlterAdapter productFiIlterAdapter;

    public void showBottomSheet(View view)
    {
        String productCOString = AppController.gson.toJson(productCO);
        if(!productCOString.equals("null"))
        { PopupBottomDailogFragment addPhotoBottomDialogFragment = PopupBottomDailogFragment.newInstance(productCOString);
            addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                    PopupBottomDailogFragment.TAG);}
        else
        {
            Toast.makeText(this, "Empty product", Toast.LENGTH_SHORT).show();
        }

    }
    @Override public void onItemClick(String item)
    {
        if(!AppValidate.isEmpty(AppUtilities.readFromPref(IReqParams.USER_CART_COUNT)))
        {
            mCartItemCount = Integer.parseInt(AppUtilities.readFromPref(IReqParams.USER_CART_COUNT));
        }
        else
        {
            mCartItemCount = 0;
        }
        setupBadge();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product_screen);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Toolbar toolbar = findViewById(R.id.single_product_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(" ");


        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        doc_type_list.add("Retailer");
        doc_type_list.add("Wholeseller");

        circleIndicator = findViewById(R.id.indicator_unselected_background);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        product_title = findViewById(R.id.product_title);
        sale_price = findViewById(R.id.sale_price);
        original_price  = findViewById(R.id.original_price);
        margine_value = findViewById(R.id.margine_value);
        product_description_content = findViewById(R.id.product_description_content);
        enter_pin_code = findViewById(R.id.enter_pin_code);
        change_pincode = findViewById(R.id.change_pincode);
        buying_option_recycler_view = findViewById(R.id.buying_option_recycler_view);
        product_container = findViewById(R.id.product_container);
        min_qty = findViewById(R.id.min_qty);
        buy_now = findViewById(R.id.buy_now);
        product_hidden_image= findViewById(R.id.product_hidden_image);
        pin_change_text = findViewById(R.id.pin_change_text);
        tag_line = findViewById(R.id.tag_line);
        product_filter = findViewById(R.id.product_filter);
        buy_now.setOnClickListener(this);
        change_pincode.setOnClickListener(this);

        if(!AppValidate.isEmpty(AppUtilities.readFromPref(IReqParams.USER_CART_COUNT)))
        {
            mCartItemCount = Integer.parseInt(AppUtilities.readFromPref(IReqParams.USER_CART_COUNT));
        }
        else
        {
            mCartItemCount = 0;
        }
        setupBadge();

        if( getIntent().getExtras() != null)
        {
            Intent intent = getIntent();
            productId = intent.getStringExtra("productId");
        }

        CallApi callApi = new CallApi(API.singleProductDetails);
        callApi.addReqParams("product_id", productId);//
        callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
        //  callApi.addReqParams("city_id",  AppUtilities.readFromPref(IPreferences.CITY_ID));//productId
        processCallApi(callApi);



    }

    public void setData(final ProductCO productCO)
    {
        populateSpinner(doc_type_list);
        Glide.with(AppController.getInstance().getApplicationContext())
            .load(productCO.image)
            .transform(new CenterCrop(),
                    new RoundedCorners(14))
                .dontAnimate()
                .into(product_hidden_image);

        String upperString = productCO.title.substring(0, 1).toUpperCase() +
                productCO.title.substring(1).toLowerCase();
      //  getSupportActionBar().setTitle(upperString);
        product_title.setText(productCO.title);
        tag_line.setText(productCO.srt_description);
        sale_price.setText("MRP. "+IConstants.RUPEE_ICON+productCO.mrp+"/- (per pcs)");
        min_qty.setText("Min. Qty. "+productCO.min_purchase_qty+ " set"+" ("+productCO.piece_in_set+" pcs/set)");
        //product_discount.setText(IConstants.RUPEE_ICON+productCO.discount + " Discount");
        if(AppValidate.isNotEmpty(productCO.description))
        {
            PicassoImageGetter imageGetter = new PicassoImageGetter(product_description_content);
           // product_description_content.setText(Html.fromHtml(productCO.description), TextView.BufferType.SPANNABLE);

            CharSequence data =  Html.fromHtml(productCO.description, imageGetter, null);
            CharSequence trimmed = AppUtilities.trim(data,0, data.length());
            product_description_content.setText(trimmed);
        }

        if(productCO.filter.size() > 0)
        {
            product_filter.setVisibility(View.VISIBLE);
            productFiIlterAdapter = new ProductFiIlterAdapter(SingleProductScreen.this,productCO.filter);
            RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(SingleProductScreen.this);
            product_filter.setLayoutManager(mLayoutManager2);
            product_filter.setItemAnimator(new DefaultItemAnimator());
            product_filter.setAdapter(productFiIlterAdapter);
            productFiIlterAdapter.notifyDataSetChanged();
        }
        else
        {
            product_filter.setVisibility(View.GONE);
        }



        sliderAdapter = new SliderAdapter(SingleProductScreen.this, productCO.sliders);
        viewPager.setAdapter(sliderAdapter);
        sliderAdapter.notifyDataSetChanged();
        //Timer timer = new Timer();
        //timer.scheduleAtFixedRate(new SliderTimer(), 2000, 4000);

        circleIndicator.setViewPager(viewPager);
        sliderAdapter.notifyDataSetChanged();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SingleProductScreen.this, LinearLayoutManager.VERTICAL, false);
        buying_option_recycler_view.setLayoutManager(layoutManager);
        buyingOptionAdapter = new BuyingOptionAdapter(SingleProductScreen.this, productCO.stock, productCO);
        buying_option_recycler_view.setAdapter(buyingOptionAdapter);
        buyingOptionAdapter.stock = productCO.stock;
        buyingOptionAdapter.notifyDataSetChanged();

        sliderAdapter.setClickListener(new SliderAdapter.ClickListner()
        {
            @Override
            public void onitemClick(View view, int position)
            {
                List<ImageCO> imageCOList = new ArrayList<>();
                for(int i = 0; i<productCO.sliders.size(); i++)
                {
                    ImageCO imageCO = new ImageCO();
                    imageCO.url = productCO.sliders.get(i).image;
                    imageCO.type = "image";
                    imageCOList.add(imageCO);
                }

               /* if(imageCO.type.toLowerCase().contains("pdf"))
                {
                    //AppUtilities.showPdfFromUrl(EditBuisnessProfile.this, imageCO.url);
                }
                else if(imageCO.type.toLowerCase().contains("image"))
                {*/

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("images", new ArrayList(imageCOList));
                bundle.putString("isPdfShow","no");
                bundle.putInt("position", 0);
                bundle.putInt("type",2);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                FragmentImageSlider newFragment = FragmentImageSlider.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");

                //}
                    /*
            }
        });

    }

    private void adapterClick()
    {
        featureProductAdapter.setEnrolledClickListener(new FeatureProductAdapter.EnrolledClickListner() {
            @Override
            public void onViewClick(View view, int positionParent)
            {

                /*CallApi callApi = new CallApi(API.userAddSharedCatalog);
                callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);
                callApi.addReqParams("product_id", productCO.similer_product.get(positionParent).product_id);
                callApi.processInBackground= true;
                processCallApi(callApi);*/
            }
        });
    }


    @Override
    protected void onViewClick(View view)
    {
        switch (view.getId())
        {

            case R.id.change_pincode:

                if(enter_pin_code.getText().toString().trim().length() == 0)
                {
                    enter_pin_code.requestFocus();
                    Toast.makeText(SingleProductScreen.this, "Enter pincode", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    pin_change_text.setText("CHANGE");
                    CallApi callApipin = new CallApi(API.check_pincode);
                    callApipin.addReqParams("pincode",enter_pin_code.getText().toString().trim());
                    processCallApi(callApipin);
                    //Toast.makeText(this, "api not avilable", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.buy_now:

              /*  if (Integer.valueOf(productCO.quantity) >= Integer.valueOf(productSizeCO.stock))
                {
                    Toast.makeText(this, "product out of stock", Toast.LENGTH_SHORT).show();
                } else {
                    if (AppValidate.isNotEmpty(productCO.pincode_status))
                    {
                        String productCOString = AppController.gson.toJson(productCO);
                        Intent i = new Intent(SingleProductScreen.this, Order.class);
                        i.putExtra("productCO", productCOString);
                        startActivity(i);
                    }
                    else
                    {
                        enter_pin_code.requestFocus();
                        Toast.makeText(this, "Check COD Availability", Toast.LENGTH_SHORT).show();
                    }
                    }*/

                  showBottomSheet(view);


                //productCO
                break;

        }

    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi)
    {
        if(API.singleProductDetails.method.equals(callApi.networkActivity.method))
        {
            try
            {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");

                if (status.equalsIgnoreCase("Success"))
                {
                    product_container.setVisibility(View.VISIBLE);
                    JSONObject data = action.getJSONObject("data");
                    productCO = AppController.gson.fromJson(data.toString(), ProductCO.class);
                   /* if(productCO.product_size.size() > 0)
                    {
                        productSizeCO = productCO.product_size.get(0);
                        productCO.product_size.get(0).isSelect = true;
                        productCO.SelectedSizeID = productSizeCO.id;
                        productCO.selectedSize = productSizeCO.product_size;

                    }*/
                    setData(productCO);
                }
                else
                {
                    product_container.setVisibility(View.GONE);
                    Toast.makeText(this, "somethig wrong", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e) {
                product_container.setVisibility(View.GONE);
                e.printStackTrace();
            }

            /* UserCO userCO = AppController.gson.fromJson(userCOJsonString, UserCO.class);
            userCO.saveInDB();

            Toast.makeText(this, UserCO.getUserCOFromDB()+"", Toast.LENGTH_SHORT).show();
            UserCO.getUserCOFromDB();*/
        }
        else if(API.check_pincode.method.equals(callApi.networkActivity.method))
        {
            try
            {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");

                if (status.equalsIgnoreCase("success"))
                {
                    pincode_status = action.getString("data");
                    if(pincode_status.equals("Y"))
                    {
                        Toast.makeText(this, "COD available", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this, "COD not available", Toast.LENGTH_SHORT).show();
                    }
                    productCO.pincode_status = pincode_status;
                }
                else
                {
                    Toast.makeText(this, "somethig wrong", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


   /* private class SliderTimer extends TimerTask
    {

        @Override
        public void run()
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < productCO.sliders.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });


        }

    }*/


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
            Intent i= new Intent(SingleProductScreen.this, SearchPage.class);
            startActivity(i);
        }

        else if (item.getItemId()==R.id.notification_home)
        {
            UserCO userCO =  UserCO.getUserCOFromDB();
            if(AppValidate.isNotEmpty(userCO.id))
            {
                Intent i =new Intent(SingleProductScreen.this,Notification.class);
                startActivityForResult(i, IConstants.INTENT_CART_COUNT_CODE);// Activity is started with requestCode 2
            }
            else
            {
                Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
                Intent ilogin = new Intent(SingleProductScreen.this,LoginOriginal.class);
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
                Intent i =new Intent(SingleProductScreen.this,CartScreen.class);
                startActivityForResult(i, IConstants.INTENT_CART_COUNT_CODE);// Activity is started with requestCode 2
            }
            else
            {
                Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
                Intent ilogin = new Intent(SingleProductScreen.this,LoginOriginal.class);
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

    private void populateSpinner(ArrayList<String> tenureCOList)
    {
        // Get reference of widgets from XML layout
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);

        final List<String> plantsList = new ArrayList<>();
        for(String tenureCO : tenureCOList)
        {
            plantsList.add(tenureCO);
        }

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,plantsList){
            @Override
            public boolean isEnabled(int position)
            {
                /*if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {*/
                    return true;
               // }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent)
            {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
               /* if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else
                {*/
                    tv.setTextColor(Color.BLACK);
               // }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
    {

        // If user change the default selection
        // First item is disable and it is used for hint
      /*  if(position > 0)
        {*/
           String selected_doc_type = (String) parent.getItemAtPosition(position);
       // }
        productCO.product_purchase_type=selected_doc_type;
        if(selected_doc_type.equals("Retailer"))
        {
            min_qty.setText("Min. Qty. "+productCO.min_purchase_qty+ " set"+" ("+productCO.piece_in_set+" pcs/set)");
            int mrp = Integer.parseInt(productCO.mrp);
            int retail_price = Integer.parseInt(productCO.retail_price);
            int margin =  mrp - retail_price;
            margine_value.setText("Margin "+margin+"/-");
            original_price.setText(IConstants.RUPEE_ICON+productCO.retail_price+"/-");
        }
        else
        {
            min_qty.setText("Min. Qty. "+productCO.retailer_max_purchase_qty+ " set"+" ("+productCO.piece_in_set+" pcs/set)");
            int mrp = Integer.parseInt(productCO.mrp);
            int retail_price = Integer.parseInt(productCO.wholesale_price);
            int margin =  mrp - retail_price;
            margine_value.setText("Margin "+margin+"/-");
            original_price.setText(IConstants.RUPEE_ICON+productCO.wholesale_price+"/-");
        }
        if(productCO.stock.size() > 0)
        {
            buyingOptionAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        // TODO Auto-generated method stub
    }



    @Override
    public void onResume() {
        if(!AppValidate.isEmpty(AppUtilities.readFromPref(IReqParams.USER_CART_COUNT)))
        {
            mCartItemCount = Integer.parseInt(AppUtilities.readFromPref(IReqParams.USER_CART_COUNT));
        }
        else
        {
            mCartItemCount = 0;
        }
        setupBadge();

        super.onResume();
    }



}