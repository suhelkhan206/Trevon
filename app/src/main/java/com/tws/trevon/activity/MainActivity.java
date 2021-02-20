package com.tws.trevon.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.tws.trevon.R;
import com.tws.trevon.co.ImageCO;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.API;
import com.tws.trevon.constants.IConstants;
import com.tws.trevon.constants.IPreferences;
import com.tws.trevon.constants.IReqParams;
import com.tws.trevon.fragment.Bannerpage;
import com.tws.trevon.fragment.CategoryFragment;
import com.tws.trevon.fragment.Five;
import com.tws.trevon.fragment.FragmentImageSlider;
import com.tws.trevon.fragment.Home;
import com.tws.trevon.fragment.MyAccount;
import com.tws.trevon.fragment.MyOrder;
import com.tws.trevon.fragment.NotificationFragment;
import com.tws.trevon.fragment.SupportPage;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AbstractActivity implements NavigationView.OnNavigationItemSelectedListener
{
    Uri mInvitationUrl;
    TextView textCartItemCount;
    int mCartItemCount;
    public BottomNavigationView navigation;
    public int backClickCount = 0;
    String isSwipe;
    TextView drawer_header_test;
    TextView user_name_drawer;
    DrawerLayout drawer;

    CircleImageView user_image;
    TextView user_name,user_phone_number, wallet_amount,gst_no;
    LinearLayout ll_select_user_profile;
    TextView company_name;


    final Fragment fragment1 = new Home();
    final Fragment fragment2 = new CategoryFragment();
    final Fragment fragment3 = new MyOrder();
    final Fragment fragment4 = new MyAccount();
    final Fragment fragment5 = new NotificationFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar dash_board_toolbar = findViewById(R.id.dash_board_toolbar);
        setSupportActionBar(dash_board_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //navigation.setItemIconTintList(null);

        fm.beginTransaction().add(R.id.fragment_container, fragment5, "5").hide(fragment5).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.fragment_container,fragment1, "1").commit();

        //navigation.setSelectedItemId(R.id.home);


         drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, dash_board_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawer, this.getTheme());
        toggle.setHomeAsUpIndicator(drawable);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black_color));

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null); // <----- HERE

        navigationView.setNavigationItemSelectedListener(this);



        UserCO userCO =  UserCO.getUserCOFromDB();
        View hView =  navigationView.getHeaderView(0);
        // Navigation view header
        ll_select_user_profile = hView.findViewById(R.id.ll_select_user_profile);
        ll_select_user_profile.setVisibility(View.GONE);
        user_image = hView.findViewById(R.id.user_image);
        company_name = hView.findViewById(R.id.company_name);
        user_name = hView.findViewById(R.id.user_name);
        user_phone_number = hView.findViewById(R.id.user_phone_number);
        gst_no = hView.findViewById(R.id.gst_no);
        gst_no.setVisibility(View.VISIBLE);


        update_detail();

        // drawer_header_test = (TextView)hView.findViewById(R.id.drawer_header_test);
       // user_name_drawer = (TextView)hView.findViewById(R.id.user_name_drawer);

      /*  if(AppValidate.isNotEmpty(userCO.user_id))
        {
            drawer_header_test.setText(userCO.mobile);
            user_name_drawer.setText(userCO.name);
        }
        else
        {
            drawer_header_test.setText("Login First !");
            user_name_drawer.setText("Welcome");
        }*/

        if(!AppValidate.isEmpty(AppUtilities.readFromPref(IReqParams.USER_CART_COUNT)))
        {
            mCartItemCount = Integer.parseInt(AppUtilities.readFromPref(IReqParams.USER_CART_COUNT));
        }
        else
        {
            mCartItemCount = 0;
        }
        setupBadge();

        callFCMTokenTOServer();

    }

    private void update_detail()
    {
        try{
            String company_name_camel = UserCO.getUserCOFromDB().company_name.substring(0, 1).toUpperCase() +
                    UserCO.getUserCOFromDB().company_name.substring(1).toLowerCase();

            company_name.setText(company_name_camel);
        }
        catch(Exception e)
        {

        }


        String username;
        if(AppValidate.isNotEmpty(UserCO.getUserCOFromDB().username))
        {
            username = UserCO.getUserCOFromDB().username.substring(0, 1).toUpperCase() +
                    UserCO.getUserCOFromDB().username.substring(1).toLowerCase();
        }
        else {
            username="";
        }
        String[] tokens = username.split(" ");
        String retVal = tokens[0];

        user_name.setText(retVal.trim()+", ");
        user_phone_number.setText(UserCO.getUserCOFromDB().business_mobile_no.trim());
        gst_no.setText(UserCO.getUserCOFromDB().gst_no);

        Glide.with(MainActivity.this)
                .load(UserCO.getUserCOFromDB().business_profile_image)
                .apply(RequestOptions.circleCropTransform().placeholderOf(R.drawable.error_images).error(R.drawable.error_images))
                .dontAnimate()
                .into(user_image);

        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ImageCO> imageCOList = new ArrayList<>();
                ImageCO imageCO = new ImageCO();
                imageCO.url = UserCO.getUserCOFromDB().business_profile_image;
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
            }
        });
    }

    public void highLightNavigationBottom() {

//        bottomNavigationView.getMenu().getItem(position).setChecked(true);

        navigation.setSelectedItemId(R.id.categories);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            UserCO userCO =  UserCO.getUserCOFromDB();
            Fragment fragment;
            switch (item.getItemId())
            {
                case R.id.home:
                   /* fragment = new Home();
                    replaceFragment(fragment);*/
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    return true;
                case R.id.categories:
                   /* fragment = new CategoryFragment();
                    replaceFragment(fragment);*/
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    return true;

                case R.id.order:
                    /*fragment = new MyOrder();
                    replaceFragment(fragment);*/
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    return true;

                case R.id.profile:
                    /*fragment = new MyAccount();
                    replaceFragment(fragment);*/
                    fm.beginTransaction().hide(active).show(fragment4).commit();
                    active = fragment4;
                    return true;

                case R.id.notification:
                    /*fragment = new NotificationFragment();
                    replaceFragment(fragment);*/
                    fm.beginTransaction().hide(active).show(fragment5).commit();
                    active = fragment5;
                    return true;
            }
            return false;
        }
    };

    public void replaceFragment(Fragment fragment)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    private void callFCMTokenTOServer()
    {
        final String token =  AppUtilities.readFromPref(IConstants.FCM_TOKEN);
        String userId = null;
        UserCO userCO =  UserCO.getUserCOFromDB();
        if(AppValidate.isNotEmpty(userCO.id))
        {
            userId = UserCO.getUserCOFromDB().id;
        }

        Handler handler = new Handler(Looper.getMainLooper());
        final String finalUserId = userId;
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                CallApi callApi = new CallApi(API.upDateFCMToken);
                callApi.addReqParams("user_id", finalUserId);
                callApi.addReqParams("token",token);
                callApi.processInBackground = true;
                callApi.isSilentCall = true;
                processCallApi(callApi);
            }
        });
    }

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
        if(item.getItemId() == android.R.id.home){ // use android.R.id
            drawer.openDrawer(Gravity.LEFT);
        }
        else if (item.getItemId()==R.id.action_search)
        {
            Intent i= new Intent(MainActivity.this, SearchPage.class);
            startActivity(i);
        }

        else if (item.getItemId()==R.id.notification_home)
        {
            UserCO userCO =  UserCO.getUserCOFromDB();
            if(AppValidate.isNotEmpty(userCO.id))
            {
                Intent i =new Intent(MainActivity.this,Notification.class);
                startActivityForResult(i, IConstants.INTENT_CART_COUNT_CODE);// Activity is started with requestCode 2
            }
            else
            {
                Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
                Intent ilogin = new Intent(MainActivity.this,LoginOriginal.class);
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
                Intent i =new Intent(MainActivity.this,CartScreen.class);
                startActivityForResult(i, IConstants.INTENT_CART_COUNT_CODE);// Activity is started with requestCode 2
            }
            else
            {
                Toast.makeText(this, "Login First", Toast.LENGTH_SHORT).show();
                Intent ilogin = new Intent(MainActivity.this,LoginOriginal.class);
                ilogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(ilogin);
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(resultCode== IConstants.INTENT_CART_COUNT_CODE)
        {
            mCartItemCount = Integer.parseInt(AppUtilities.readFromPref(IReqParams.USER_CART_COUNT));
            setupBadge();
        }

    }

    @Override
    protected void onViewClick(View view) {

        switch(view.getId())
        {
           /* case R.id.category_btn:
                Intent i =new Intent(DashBoard.this, CategoryPage.class);
                startActivity(i);
                break;

            case R.id.address_search:
                Intent location = new Intent(DashBoard.this, LocationSearch.class);
                startActivityForResult(location, 201);
                break;

            case R.id.editText_search_page:
                Intent search = new Intent(DashBoard.this, SearchPage.class);
                startActivityForResult(search, 202);
                break;*/

        }

    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi)
    {
        if (API.upDateFCMToken.method.equals(callApi.networkActivity.method))
        {
            Log.d("service_start_done","done"+responseValues);
        }
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



    @Override
    public void onBackPressed()
    {

        if(fragment1.isVisible())
        {
            backClickCount = backClickCount+1;
            if(backClickCount == 1)
            {
                Toast.makeText(getApplicationContext(), IConstants.LAST_BACK_CLICK,Toast.LENGTH_SHORT).show();
            }
            else if(backClickCount == 2)
            {
                finish();
                // super.onBackPressed();
            }
        }
        else
        {
            backClickCount =0;
            navigation.setSelectedItemId(R.id.home);
        }




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
        update_detail();

        super.onResume();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.bussiness) {
            Intent iMySharedCatelog = new Intent(MainActivity.this, EditBuisnessProfile.class);
            startActivity(iMySharedCatelog);
        }
        else if (id == R.id.my_wish_list) {
            Intent iMyWishList = new Intent(MainActivity.this, MyWishList.class);
            startActivity(iMyWishList);
        }
        else if (id == R.id.my_orders) {
            Intent iMyOrderList = new Intent(MainActivity.this, MyOrderList.class);
            startActivity(iMyOrderList);
        }

        else if (id == R.id.order_your_design)
        {
            Intent io =new Intent(MainActivity.this, CustomOrderCreation.class);
            startActivity(io);
        }



        else if (id == R.id.my_address) {
            Intent i =new Intent(MainActivity.this, MyAddress.class);
            i.putExtra("is_checkout","N");
            startActivity(i);
        }


        else if (id == R.id.share_earn)
        {
            createLink();
        }
        else if (id == R.id.support)
        {

            Intent i = new Intent(MainActivity.this, AllInOnePage.class);
            i.putExtra("type", "Support");
            startActivity(i);

        } else if (id == R.id.term_of_use) {
            Intent i = new Intent(MainActivity.this, AllInOnePage.class);
            i.putExtra("type", "Term of Use");
            startActivity(i);

        }

        else if (id == R.id.privacy_policy) {
            Intent i = new Intent(MainActivity.this, AllInOnePage.class);
            i.putExtra("type", "Privacy Policy");
            startActivity(i);
        }

        else if (id == R.id.about) {
            Intent i = new Intent(MainActivity.this, AllInOnePage.class);
            i.putExtra("type", "About");
            startActivity(i);
        }

        else if (id == R.id.help)
        {
            Intent i = new Intent(MainActivity.this, Help.class);
            startActivity(i);

        }



      /*  else if (id == R.id.share) {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "VYAPAAR SAARTHI");
                String shareMessage = "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }

        }*/


        else if (id == R.id.logout)
        {
            UserCO userCO = new UserCO();
            userCO.deleteFromDB();
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, LoginOriginal.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void createLink() {

        //creating dynamic link
        String uid = UserCO.getUserCOFromDB().id;

        Log.d("TAG", "EEEEE: " + uid);
        String link = "https://trevon.page.link/?invitedby=" + uid;
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(link))
                .setDomainUriPrefix("https://trevon.page.link")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.trevon")
                                .setMinimumVersion((int)3.0)
                                .build())
//                .setIosParameters(
//                        new DynamicLink.IosParameters.Builder("com.example.ios")
//                                .setAppStoreId("123456789")
//                                .setMinimumVersion("1.0.1")
//                                .build())
                .buildShortDynamicLink()
                .addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
                    @Override
                    public void onSuccess(ShortDynamicLink shortDynamicLink) {

                        mInvitationUrl = shortDynamicLink.getShortLink();

                        Log.d("TAG", "TTTTTTTTT ::: PATH " +  shortDynamicLink.getPreviewLink());

                        Log.d("TAG", "TTTTTTTTT " + mInvitationUrl.toString());
                        Log.d("TAG", "TTTTTTTTT " + shortDynamicLink.toString());
                        sendDynamicLink(mInvitationUrl);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "EEEEEE " + e.getMessage());
                    }
                });


    }

    //Sharing the dynamic Link
    private void sendDynamicLink(Uri mInvitationUrl) {

        String referrerName = UserCO.getUserCOFromDB().username;
        String subject = String.format("%s wants you to Download The Trevon!", referrerName);
        String invitationLink = mInvitationUrl.toString();
        String msg = "Download the Trevon and earn some money! Use my referrer link: "
                + invitationLink;
        String msgHtml = String.format("<p>Download the Trevon and earn some money! Use my "
                + "<a href=\"%s\">referrer link</a>!</p>", invitationLink);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
        sendIntent.putExtra(Intent.EXTRA_HTML_TEXT, msgHtml);
        sendIntent.setType("text/plain");

        if (sendIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(sendIntent);
        }
    }
}
