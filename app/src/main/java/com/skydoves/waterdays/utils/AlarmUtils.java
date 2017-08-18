package com.skydoves.waterdays.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.skydoves.waterdays.persistence.sqlite.SqliteManager;
import com.skydoves.waterdays.services.receivers.AlarmReceiver;

import java.util.Calendar;
import java.util.GregorianCalendar;

import timber.log.Timber;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-18.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class AlarmUtils {

    private Context mContext;

    private AlarmManager mManager;
    private SqliteManager sqliteManager;

    public AlarmUtils(Context context){
        this.mContext = context;
        this.mManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        this.sqliteManager = new SqliteManager(context, SqliteManager.DATABASE_NAME, null, SqliteManager.DATABASE_VERSION);
    }

    public void setAlarm(int requestcode) {
        GregorianCalendar mCalendar = new GregorianCalendar();
        String daylist="", StartTime="", EndTime="";
        int interval=1;
        Cursor cursor = sqliteManager.getReadableDatabase().rawQuery("select * from AlarmList where requestcode = " + requestcode , null);
        try {
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    daylist = cursor.getString(1);
                    StartTime = cursor.getString(2);
                    EndTime = cursor.getString(3);
                    interval = cursor.getInt(4);
                } while (cursor.moveToNext());
            }

            String[] sdate_h = StartTime.split("시 ");
            String[] sdate_m = sdate_h[1].split("분");
            String[] edate_h = EndTime.split("시 ");
            String[] edate_m = edate_h[1].split("분");

            boolean check_overday = false;
            String[] mDayList = daylist.split(",");
            for(int i=0; i<mDayList.length ; i++) {
                if(DateUtils.getDateDay(DateUtils.getFarDay(0), "yyyy-MM-dd") <= Integer.parseInt(mDayList[i])) {
                    int c_date = Integer.parseInt(mDayList[i]) - DateUtils.getDateDay(DateUtils.getFarDay(0), "yyyy-MM-dd");
                    String d_date = DateUtils.getFarDay(c_date);
                    String[] l_date = d_date.split("-");

                    if(c_date < 0) continue;
                    else if (c_date == 0 && (mCalendar.get(Calendar.HOUR_OF_DAY) > Integer.parseInt(edate_h[0]))) continue;
                    else if(c_date == 0 && (mCalendar.get(Calendar.HOUR_OF_DAY) >= Integer.parseInt(edate_h[0])) && mCalendar.get(Calendar.MINUTE) > Integer.parseInt(edate_m[0])) continue;

                    mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), Integer.parseInt(l_date[2]), Integer.parseInt(sdate_h[0]), Integer.parseInt(sdate_m[0]));
                    mManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), interval * 3600 * 1000, pendingIntent(requestcode, Integer.parseInt(mDayList[i])));
                    check_overday = true;
                    break;
                }
            }

            if(!check_overday) {
                int c_date = 7 - (DateUtils.getDateDay(DateUtils.getFarDay(0), "yyyy-MM-dd")  - Integer.parseInt(mDayList[0]));
                String d_date = DateUtils.getFarDay(c_date);
                String[] l_date = d_date.split("-");

                mCalendar.set(mCalendar.get(Calendar.YEAR), Integer.parseInt(l_date[1])-1, Integer.parseInt(l_date[2]), Integer.parseInt(sdate_h[0]), Integer.parseInt(sdate_m[0]));
                mManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), interval * 3600 * 1000, pendingIntent(requestcode, Integer.parseInt(mDayList[0])));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            cursor.close();
        }
    }

    public String getEndTime(int requestcode) {
        Cursor cursor = sqliteManager.getReadableDatabase().rawQuery("select * from AlarmList where requestcode = " + requestcode, null);
        try {
            String e_Time="";
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    e_Time = cursor.getString(3);
                } while (cursor.moveToNext());
            }

            String[] eTime_h = e_Time.split("시 ");
            String[] eTime_m = eTime_h[1].split("분");

            return eTime_h[0] + "," + eTime_m[0];
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            cursor.close();
        }
        return null;
    }

    public void cancelAlarm(int requestcode) {
        try{
            if(pendingIntent(requestcode, -1) != null) {
                mManager.cancel(pendingIntent(requestcode, -1));
                Timber.e("alarm canceled");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PendingIntent pendingIntent(int requestcode, int occurdateint) {
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        intent.putExtra("requestcode", requestcode);
        intent.putExtra("occurdateint", occurdateint);
        PendingIntent sender = PendingIntent.getBroadcast(mContext, requestcode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return sender;
    }
}