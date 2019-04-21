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

package com.skydoves.waterdays.services.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.skydoves.waterdays.consts.IntentExtras;
import com.skydoves.waterdays.persistence.preference.PreferenceManager;
import com.skydoves.waterdays.utils.DateUtils;
import com.skydoves.waterdays.utils.NetworkUtils;
import com.skydoves.waterdays.utils.NotificationUtils;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by skydoves on 2016-10-15. Updated by skydoves on 2017-08-18. Copyright (c) 2017 skydoves
 * rights reserved.
 */
public class LocalWeatherReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    PreferenceManager preferenceManager = new PreferenceManager(context);
    GregorianCalendar mCalendar = new GregorianCalendar();
    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

    // check network is connected?
    if (NetworkUtils.INSTANCE.isNetworkAvailable(context)) {
      try {
        if (!DateUtils.INSTANCE
            .getFarDay(0)
            .equals(preferenceManager.getString("WheatherAlarmDate", "null"))) {
          LocalWeather localWeather = new LocalWeather(context);
          String Reh = localWeather.execute().get();

          // check null value
          if (Reh != null) {
            SharedPreferences prefs =
                android.preference.PreferenceManager.getDefaultSharedPreferences(context);
            boolean vibrate = prefs.getBoolean("Setting_Vibrate", true);
            boolean sound = prefs.getBoolean("Setting_Sound", true);

            // set auto-waterGoal
            if (prefs.getBoolean("Setting_AutoGoal", true)) {
              int recommandAmount =
                  preferenceManager.getInt("userWeight", 60) * 30
                      + (500 - Integer.parseInt(Reh) * 3);
              preferenceManager.putString("WaterGoal", String.valueOf(recommandAmount));

              // send notification
              if (prefs.getBoolean("Setting_AutoGoalPush", true))
                NotificationUtils.INSTANCE.sendNotification(
                    context,
                    "물 한잔 해요",
                    "오늘의 습도 : " + Reh + "%, 목표 섭취량 : " + recommandAmount + "ml",
                    1,
                    vibrate,
                    sound);
            }

            // save Reh data
            preferenceManager.putString("Reh", Reh);

            // cancel alarm
            alarmManager.cancel(pendingIntent(context, IntentExtras.ALARM_PENDING_REQUEST_CODE));

            // set next alarm
            mCalendar.set(
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH),
                7,
                0);
            mCalendar.add(Calendar.DATE, 1);
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                mCalendar.getTimeInMillis(),
                1200 * 1000,
                pendingIntent(context, IntentExtras.ALARM_PENDING_REQUEST_CODE));
            preferenceManager.putString("WheatherAlarmDate", DateUtils.INSTANCE.getFarDay(0));
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private PendingIntent pendingIntent(Context mContext, int requestCode) {
    Intent intent = new Intent(mContext, LocalWeatherReceiver.class);
    intent.putExtra(IntentExtras.ALARM_PENDING_REQUEST, requestCode);
    return PendingIntent.getBroadcast(mContext, requestCode, intent, 0);
  }
}
