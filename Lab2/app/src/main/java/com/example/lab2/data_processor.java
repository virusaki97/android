package com.example.lab2;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.example.lab2.MainActivity.TAG;
import static com.example.lab2.MainActivity.mThis;
import static com.example.lab2.data_tools.*;

public class data_processor extends Service {




    private final static int INTERVAL = 1000 * 60;

    Handler mHandler = new Handler();

    Runnable mHandlerTask = new Runnable()
    {
        @Override
        public void run()
        {
            long time = new Date().getTime();

            Calendar now = new GregorianCalendar();
            now.setTimeInMillis(time);
            time = date_to_timestamp(now.get(Calendar.YEAR), now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE));
            List<obj_event> l_events = get_events(time, 0, "", false);
            if(!l_events.isEmpty())
            {
                remove_event(time);
                mThis.UpdateTheList();
                String szDate = new SimpleDateFormat("[HH:mm]   dd/MMM/yyyy", Locale.ENGLISH).format(now.getTime());
                showNotification(szDate, l_events.get(0).getTextMsg());

            }
            mHandler.postDelayed(mHandlerTask, INTERVAL);
        }
    };


    void showNotification(String title, String message) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "YOUR_CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(message)// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mHandlerTask.run();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
