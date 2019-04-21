/*
 * Copyright (C) 2016 skydoves
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.waterdays.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
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
