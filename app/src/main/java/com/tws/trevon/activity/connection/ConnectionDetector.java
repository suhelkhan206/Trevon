package com.tws.trivon.activity.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Paritosh on 3/22/2016.
 */
public class ConnectionDetector {

    private static final String TAG = ConnectionDetector.class.getSimpleName();



    public static boolean isConnectingToInternet(Context context)
    {
        try{
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null)
        {
            Log.d(TAG, "no internet connection");
            return false;
        }
        else
        {
            if(info.isConnected())
            {
                Log.d(TAG," internet connection available...");
                return true;
            }
            else
            {
                Log.d(TAG, " internet connection");
                return true;
            }

        }

        }catch (Exception e){

        }
        return false;
    }
}
