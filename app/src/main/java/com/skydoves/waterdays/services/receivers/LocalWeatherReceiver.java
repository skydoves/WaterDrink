package com.skydoves.waterdays.services.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.skydoves.waterdays.persistence.preference.PreferenceManager;
import com.skydoves.waterdays.utils.DateUtils;
import com.skydoves.waterdays.utils.NetworkUtils;
import com.skydoves.waterdays.utils.NotificationUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-18.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class LocalWeatherReceiver extends BroadcastReceiver {

    private Context mContext;
    private PreferenceManager systems;
    private GregorianCalendar mCalendar;
    private AlarmManager mManager;
    private final int requestC = 99000000;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        systems = new PreferenceManager(context);
        mCalendar = new GregorianCalendar();
        mManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        // Check Connected Network
        if(NetworkUtils.isNetworkAvailable(context))
        {
            try {
                if(!DateUtils.getFarDay(0).equals(systems.getString("WheatherAlarmDate", "null"))) {
                    // Get Local Weather : Reh
                    LocalWeather localWeather = new LocalWeather(context);
                    String Reh = localWeather.execute().get();

                    // Check Reh Value is Valid?
                    if (Reh != null) {
                        // get Preference Setting Value
                        SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
                        boolean vibrate = prefs.getBoolean("Setting_Vibrate", true);
                        boolean sound = prefs.getBoolean("Setting_Sound", true);

                        // Set Auto WaterGoal
                        if (prefs.getBoolean("Setting_AutoGoal", true)) {
                            int recommandAmount = systems.getInt("userWeight", 60) * 30 + (500 - Integer.parseInt(Reh) * 3);
                            systems.putString("WaterGoal", String.valueOf(recommandAmount));

                            // Send Notification
                            if (prefs.getBoolean("Setting_AutoGoalPush", true))
                                NotificationUtils.sendNotification(context, "물 한잔 해요", "오늘의 습도 : " + Reh + "%, 목표 섭취량 : " + recommandAmount + "ml", 1, vibrate, sound);
                        }

                        // Save Reh Data
                        systems.putString("Reh", Reh);

                        // Cancel this Alarm
                        mManager.cancel(pendingIntent(requestC));

                        // Set Next Alarm
                        mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), 7, 0);
                        mCalendar.add(Calendar.DATE, 1);
                        mManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), 1200 * 1000, pendingIntent(requestC));

                        // Set Preference
                        systems.putString("WheatherAlarmDate", DateUtils.getFarDay(0));
                    }
                }
            }
            catch (Exception e){
                // Network Error
            }
        }
    }

    // # Pending Intent # //
    private PendingIntent pendingIntent(int requestcode) {
        // TODO skydoves - PendingIntent with Handling requestcode
        Intent intent = new Intent(mContext, LocalWeatherReceiver.class);
        intent.putExtra("requestcode", requestcode);
        PendingIntent sender = PendingIntent.getBroadcast(mContext, requestcode, intent, 0);
        return sender;
    }
}
