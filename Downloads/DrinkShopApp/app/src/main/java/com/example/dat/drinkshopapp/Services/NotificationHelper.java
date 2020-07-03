package com.example.dat.drinkshopapp.Services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import com.example.dat.drinkshopapp.R;

public class NotificationHelper extends ContextWrapper {
    private static final String DEVFPTPOLY_ID = "com.example.dat.drinkshopapp.Services.DAT";
    private static final String DEVFPTPOLY_NAME = "Drink Shop Client";

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createChanel();
    }

    private NotificationManager notificationManager;
    @TargetApi(Build.VERSION_CODES.O)
    private void createChanel() {
        NotificationChannel channel = new NotificationChannel(DEVFPTPOLY_ID
                , DEVFPTPOLY_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(false);
        channel.enableVibration(false);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (notificationManager == null)
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        return notificationManager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getDrinkShopNotification(String title,
                                                         String message,
                                                         Uri soundUri) {
        return  new Notification.Builder(getApplicationContext(),DEVFPTPOLY_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(soundUri)
                .setAutoCancel(true);
    }
}
