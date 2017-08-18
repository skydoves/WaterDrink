package com.skydoves.waterdays.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.skydoves.waterdays.R;
import com.skydoves.waterdays.ui.activities.main.MainActivity;

/**
 * Developed by skydoves on 2017-08-18.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class NotificationUtils {
    public static void sendNotification(Context mContext, String title, String message, int number, boolean vibrate, boolean sound) {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.img_waterdrop)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.img_waterdrop))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setNumber(number)
                .setTicker(message)
                .setContentIntent(pendingIntent);

        if(vibrate) notificationBuilder.setVibrate(new long[]{2000});
        if(sound) notificationBuilder.setSound(defaultSoundUri);

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1004, notificationBuilder.build());
    }
}
