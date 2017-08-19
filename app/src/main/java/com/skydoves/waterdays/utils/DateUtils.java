package com.skydoves.waterdays.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Developed by skydoves on 2017-08-18.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class DateUtils {
    public static String getFarDay(int far) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, far);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(cal.getTime());
        return currentDateandTime;
    }

    public static int getDateDay(String date, String dateType) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(dateType);
            Date nDate = dateFormat.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(nDate);
            return cal.get(Calendar.DAY_OF_WEEK) - 1;
        } catch (Exception e) {
        }
        return -1;
    }

    public static int getDayofWeek(String data, String dateType){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(dateType);
            Date nDate = dateFormat.parse(data);
            Calendar c = Calendar.getInstance();
            c.setTime(nDate);
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            return dayOfWeek;
        }
        catch (Exception e){
        }
        return -1;
    }

    public static String getDayNameList(String days) {
        StringBuilder builder = new StringBuilder();
        if(days.contains("0"))
            builder.append("일");
        if(days.contains("1"))
            builder.append("월");
        if(days.contains("2"))
            builder.append("화");
        if(days.contains("3"))
            builder.append("수");
        if(days.contains("4"))
            builder.append("목");
        if(days.contains("5"))
            builder.append("금");
        if(days.contains("6"))
            builder.append("토");
         return builder.toString();
    }
}
