package com.skydoves.waterdays.services.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.skydoves.waterdays.R;
import com.skydoves.waterdays.consts.IntentExtras;
import com.skydoves.waterdays.ui.activities.alarm.AlarmScreenActivity;
import com.skydoves.waterdays.utils.AlarmUtils;
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

    private enum resetType {
        BEFORE_HOUR, AFTER_HOUR, MINUTES
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int requestCode = intent.getIntExtra(IntentExtras.INSTANCE.getALARM_PENDING_REQUEST(), -1);
        int occurdateint = intent.getIntExtra(IntentExtras.INSTANCE.getALARM_PENDING_OCCUR_TIME(), -1);

        AlarmUtils alarmUtils = new AlarmUtils(context);
        GregorianCalendar mCalendar = new GregorianCalendar();
        if (requestCode != -1) {

            int cdate = occurdateint - DateUtils.INSTANCE.getDateDay(DateUtils.INSTANCE.getFarDay(0), DateUtils.INSTANCE.getDateFormat());
            String[] EndTime = alarmUtils.getEndTime(requestCode).split(",");
            if(cdate < 0)
                resetAlarm(alarmUtils, requestCode, resetType.BEFORE_HOUR.ordinal());
            else if(cdate == 0 && (mCalendar.get(Calendar.HOUR_OF_DAY) > Integer.parseInt(EndTime[0])))
                resetAlarm(alarmUtils, requestCode, resetType.AFTER_HOUR.ordinal());
            else if(cdate == 0 && (mCalendar.get(Calendar.HOUR_OF_DAY) >= Integer.parseInt(EndTime[0])) && (mCalendar.get(Calendar.MINUTE) > Integer.parseInt(EndTime[1])))
                resetAlarm(alarmUtils, requestCode, resetType.MINUTES.ordinal());
            else {
                SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
                boolean vibrate = prefs.getBoolean("Setting_Vibrate", true);
                boolean sound = prefs.getBoolean("Setting_Sound", true);
                boolean alarmScreen = prefs.getBoolean("Setting_AlarmActivity", true);

                if(alarmScreen){
                    Intent intent_AlarmActivity = new Intent(context, AlarmScreenActivity.class);
                    intent_AlarmActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent_AlarmActivity);
                }

                NotificationUtils.INSTANCE.sendNotification(context, context.getString(R.string.app_name), context.getString(R.string.msg_alarm), 1, vibrate, sound);
            }
        }
    }

    private void resetAlarm(AlarmUtils alarmUtils, int requestCode, int resetType) {
        alarmUtils.cancelAlarm(requestCode);
        alarmUtils.setAlarm(requestCode);
    }
}
