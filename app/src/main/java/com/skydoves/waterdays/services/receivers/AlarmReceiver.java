package com.skydoves.waterdays.services.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.skydoves.waterdays.utils.AlarmUtils;
import com.skydoves.waterdays.ui.activities.alarm.AlarmScreenActivity;
import com.skydoves.waterdays.utils.DateUtils;
import com.skydoves.waterdays.utils.NotificationUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private int requestcode, occurdateint;

    // Systems
    private AlarmUtils systems_alarm;
    private GregorianCalendar mCalendar;

    @Override
    public void onReceive(Context context, Intent intent) {
        // get requestcode
        requestcode = intent.getIntExtra("requestcode", -1);
        occurdateint = intent.getIntExtra("occurdateint", -1);

        systems_alarm = new AlarmUtils(context);
        mCalendar = new GregorianCalendar();

        // # Check Validate of requestcode # //
        if (requestcode != -1) {
            int cdate = occurdateint - DateUtils.getDateDay(DateUtils.getFarDay(0), "yyyy-MM-dd");
            String[] EndTime = systems_alarm.getEndTime(requestcode).split(",");

            // Over EndTime : Reset next Alarm
            if(cdate < 0)
                ResetAlarm(0);
            else if(cdate == 0 && (mCalendar.get(Calendar.HOUR_OF_DAY) > Integer.parseInt(EndTime[0])))
                ResetAlarm(1);
            else if(cdate == 0 && (mCalendar.get(Calendar.HOUR_OF_DAY) >= Integer.parseInt(EndTime[0])) && (mCalendar.get(Calendar.MINUTE) > Integer.parseInt(EndTime[1])))
                ResetAlarm(2);

            // # Between Start-End Time # //
            else {
                // get Preference Setting Value
                SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
                boolean vibrate = prefs.getBoolean("Setting_Vibrate", true);
                boolean sound = prefs.getBoolean("Setting_Sound", true);
                boolean alarmScreen = prefs.getBoolean("Setting_AlarmActivity", true);

                // show Alarm Activity
                if(alarmScreen){
                    Intent intent_AlarmActivity = new Intent(context, AlarmScreenActivity.class);
                    intent_AlarmActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent_AlarmActivity);
                }

                // send Notification
                NotificationUtils.sendNotification(context, "물 한잔 해요", "수분을 보충할 시간입니다!", 1, vibrate, sound);
            }
        }
    }

    // Cancel & Reset new Alarm
    private void ResetAlarm(int resetType) {
        systems_alarm.cancelAlarm(requestcode);
        systems_alarm.setAlarm(requestcode);
    }
}
