package com.skydoves.waterdays.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.support.v4.app.NotificationCompat

import com.skydoves.waterdays.R
import com.skydoves.waterdays.ui.activities.main.MainActivity

/**
 * Developed by skydoves on 2017-08-18.
 * Copyright (c) 2017 skydoves rights reserved.
 */

object NotificationUtils {
    fun sendNotification(mContext: Context, title: String, message: String, number: Int, vibrate: Boolean, sound: Boolean) {
        val intent = Intent(mContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.img_waterdrop)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.resources, R.drawable.img_waterdrop))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setNumber(number)
                .setTicker(message)
                .setContentIntent(pendingIntent)

        if (vibrate) notificationBuilder.setVibrate(longArrayOf(2000))
        if (sound) notificationBuilder.setSound(defaultSoundUri)

        val notificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1004, notificationBuilder.build())
    }
}
