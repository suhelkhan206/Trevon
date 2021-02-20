package com.tws.trevon.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tws.trevon.R;
import com.tws.trevon.activity.sp.MyConnectionActivity2;
import com.tws.trevon.adapter.CartItemAdapter;
import com.tws.trevon.adapter.MyConnectionAdapter;
import com.tws.trevon.co.CartProductCO;
import com.tws.trevon.co.ImageCO;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.API;
import com.tws.trevon.constants.IConstants;
import com.tws.trevon.constants.IReqParams;
import com.tws.trevon.fragment.FragmentImageSlider;
import com.tws.trevon.model.OrderCO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyConnectionActivity extends AbstractActivity {

    RecyclerView my_connection_recyclreview;
    MyConnectionAdapter myConnectionAdapter;
    public List<UserCO>  userCOList = new ArrayList<>();

    CircleImageView user_image;
    TextView user_name,user_phone_number, wallet_amount;
    LinearLayout ll_select_user_profile;
    ImageView crown;
    LinearLayout no_connection_found;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_connection);
        Toolbar toolbar = findViewById(R.id.my_connection);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Connections");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

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
        no_connection_found = findViewById(R.id.no_connection_found);
        user_name.setText(UserCO.getUserCOFromDB().username);
        user_phone_number.setText(UserCO.getUserCOFromDB().mobile);
        user_image.setOnClickListener(this);
        wallet_amount.setText(UserCO.getUserCOFromDB().wallet_amount);
        Glide.with(MyConnectionActivity.this)
                .load(UserCO.getUserCOFromDB().image)
                .apply(RequestOptions.circleCropTransform().placeholderOf(R.drawable.error_images).error(R.drawable.error_images))
                .dontAnimate()
                .into(user_image);

        my_connection_recyclreview = findViewById(R.id.my_connection_recyclreview);

        myConnectionAdapter = new MyConnectionAdapter(MyConnectionActivity.this,userCOList);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(MyConnectionActivity.this);
        my_connection_recyclreview.setLayoutManager(mLayoutManager2);
        my_connection_recyclreview.setAdapter(myConnectionAdapter);


        CallApi callApi = new CallApi(API.my_connection);
        callApi.addReqParams("user_id", UserCO.getUserCOFromDB().id);//
        processCallApi(callApi);

        myConnectionAdapter.setClickListener(new MyConnectionAdapter.ClickListner()
        {
            @Override
            public void onitemClick(View view, int position)
            {
               // Toast.makeText(MyConnectionActivity2.this, "hey", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onViewClick(View view)
    {
        switch(view.getId()) {
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
    protected void onApiCallSuccess(Object responseValues, CallApi callApi)
    {
        if(API.my_connection.method.equals(callApi.networkActivity.method))
        {
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String nessage = action.getString("message");
                String status = action.getString("status");
                if (status.equalsIgnoreCase("Success"))
                {
                    userCOList.clear();
                    JSONArray onbjnew = action.getJSONArray("data");
                    for(int i = 0 ; i<onbjnew.length();i++)
                    {
                        JSONObject jsonObject1 = onbjnew.getJSONObject(i);
                        UserCO userCO = AppController.gson.fromJson(jsonObject1.toString(), UserCO.class);
                        userCOList.add(userCO);
                    }
                    if(userCOList.size() > 0)
                    {
                        no_connection_found.setVisibility(View.GONE);
                    }
                    else
                    {
                        no_connection_found.setVisibility(View.VISIBLE);
                    }

                    myConnectionAdapter.userCOList = userCOList;
                    myConnectionAdapter.notifyDataSetChanged();
                } else {
                    no_connection_found.setVisibility(View.VISIBLE);
                    Toast.makeText(this, nessage, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                no_connection_found.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }

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
            Intent i= new Intent(MyConnectionActivity.this, SearchPage.class);
            startActivity(i);
        }

        else if (item.getItemId()==R.id.notification_home)
        {
            UserCO userCO =  UserCO.getUserCOFromDB();
            if(AppValidate.isNotEmpty(userCO.id))
            {
                Intent i =new Intent(MyConnectionActivity.this,Notification.class);
                startActivityForResult(i, IConstants.INTENT_CART_COUNT_CODE);// Activity is started with requestCode 2
            }
            else
            {
                Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
                Intent ilogin = new Intent(MyConnectionActivity.this,LoginOriginal.class);
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
                Intent i =new Intent(MyConnectionActivity.this,CartScreen.class);
                startActivityForResult(i, IConstants.INTENT_CART_COUNT_CODE);// Activity is started with requestCode 2
            }
            else
            {
                Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
                Intent ilogin = new Intent(MyConnectionActivity.this,LoginOriginal.class);
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