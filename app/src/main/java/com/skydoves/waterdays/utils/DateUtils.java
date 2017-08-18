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
}
