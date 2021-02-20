package com.tws.trevon.activity;

 import android.content.BroadcastReceiver;
 import android.content.Intent;
 import android.content.IntentFilter;
 import android.net.Uri;
 import android.os.Handler;

 import androidx.annotation.NonNull;
 import androidx.localbroadcastmanager.content.LocalBroadcastManager;
 import androidx.appcompat.app.AppCompatActivity;
 import android.os.Bundle;
 import android.util.Log;
 import android.view.WindowManager;
 import android.widget.Toast;

 import com.google.android.gms.tasks.OnFailureListener;
 import com.google.android.gms.tasks.OnSuccessListener;
 import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
 import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
 import com.tws.trevon.R;
 import com.tws.trevon.co.UserCO;
 import com.tws.trevon.common.AppUtilities;
 import com.tws.trevon.common.AppValidate;
 import com.tws.trevon.common.Config;
 import com.tws.trevon.common.NotificationUtils;
 import com.tws.trevon.constants.IReqParams;

public class SplashScreen extends AppCompatActivity
{
    private static int SPLASH_SCREEN_TIME_OUT=2000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final String TAG = SplashScreen.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //This method is used so that your splash activity
        //can cover the entire screen.

        setContentView(R.layout.activity_splash_screen);

        //this will bind your SplashScreen.class file with activity_main.
        if(getIntent() != null && getIntent().getExtras() != null )
        {   //notification clicked
            String message= getIntent().getExtras().getString("message");
            Log.e(TAG, "title: " + message);
            if(AppValidate.isNotEmpty(message))
            {
                UserCO userCO = UserCO.getUserCOFromDB();
                if(AppValidate.isNotEmpty(userCO.id))
                {
                    Intent i = new Intent(this,MainActivity.class);
                    i.putExtra("is_notification","yes");
                    this.startActivity(i);
                }
                else
                {
                    updateView();
                }

            }
            else
            {
                updateView();
            }

        }
        else
        {
            updateView();
        }



    }

    public void updateView()
    {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                UserCO userCO = UserCO.getUserCOFromDB();
                Intent i = null;
                if(AppValidate.isNotEmpty(userCO.id))
                {
                    if(userCO.seller_type.equals("RESELLER"))
                    {
                         i = new Intent(SplashScreen.this, MainActivity.class);
                    }
                    else
                    {
                         i = new Intent(SplashScreen.this, com.tws.trevon.activity.sp.MainActivity.class);
                    }
                }
                else
                {
                    handleFireBaseDeepLinkClick();
                    i=new Intent(SplashScreen.this, LoginOriginal.class);
                }
                startActivity(i);
                finish();
            }
        }, SPLASH_SCREEN_TIME_OUT);}

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void handleFireBaseDeepLinkClick()
    {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null)
                        {
                            deepLink = pendingDynamicLinkData.getLink();
                        }

                        if (deepLink != null) {
                            try
                            {
                                String param = deepLink.getQueryParameter("invitedby");
                                AppUtilities.writeToPref(IReqParams.REFERAL_CODE, param);
                                Intent i=new Intent(SplashScreen.this, Registration.class);
                                startActivity(i);
                                finish();
                            }
                            catch (Exception e)
                            {

                            }

                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {

                        Toast.makeText(SplashScreen.this, ""+e, Toast.LENGTH_SHORT).show();
                        // processException(e);
                    }
                })
        ;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected void onPause()
    {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
} 


