package com.tws.trevon.receivers;



 import android.content.BroadcastReceiver;
 import android.content.Context;
 import android.content.Intent;
 import androidx.core.app.NotificationCompat;
 import androidx.core.app.NotificationManagerCompat;

 import com.tws.trevon.R;

//http://javapapers.com/android/android-alarm-clock-tutorial/
public class ReminderBroadcastReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(final Context context, Intent intent)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notify Lenubet")
                .setSmallIcon(R.drawable.ic_location_icon)
                .setContentTitle("Hello Book Cab")
                .setContentText("Hello Book Cab")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompact = NotificationManagerCompat
                .from(context);
        notificationManagerCompact.notify(200, builder.build());

    }
}
