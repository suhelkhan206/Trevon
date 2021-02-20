
package com.tws.trevon.receivers;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.service.AlarmService;
import com.tws.trevon.service.PushJobService;

//http://javapapers.com/android/android-alarm-clock-tutorial/
public class AlarmReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(final Context context, Intent intent)
    {
        try
        {
            //Intent serviceIntent = new Intent(context, AlarmService.class);
            // Start service
            //context.startService(serviceIntent);
            //AlarmService.enqueueWork(context, serviceIntent);
//            AppUtilities.showUserMessageInDevMode("AlarmReceiver called");

            if(android.os.Build.VERSION.SDK_INT > 25)
            {
                ComponentName serviceComponent = new ComponentName(context, PushJobService.class);
                JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
                //builder.setMinimumLatency(1 * 1000); // wait at least
                builder.setOverrideDeadline(1 * 1000); // maximum delay
                builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
                //builder.setRequiresDeviceIdle(true); // device should be idle
                //builder.setRequiresCharging(false); // we don't care if the device is charging or not
                JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
                jobScheduler.schedule(builder.build());
            }
            else
            {
                Intent serviceIntent = new Intent(context, AlarmService.class);
                context.startService(serviceIntent);
            }
        }
        catch(Exception ex)
        {
            AppUtilities.processException(
                    ex,
                    "AlarmReceiver onReceive",
                    null,
                    AppUtilities.dumpIntent(intent));
        }
    }
}
