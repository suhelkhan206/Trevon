
package com.tws.trevon.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.service.BootService;

//http://javapapers.com/android/android-alarm-clock-tutorial/
public class BootReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(final Context context, Intent intent)
    {
        try
        {
            Intent serviceIntent = new Intent(context, BootService.class);
            // Start service
            //context.startService(serviceIntent);
            BootService.enqueueWork(context, serviceIntent);
        }
        catch(Exception ex)
        {
            AppUtilities.processException(
                    ex,
                    "BootReceiver onReceive",
                    null,
                    AppUtilities.dumpIntent(intent));
        }
    }
}
