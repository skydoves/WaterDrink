package com.skydoves.waterdays.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Developed by skydoves on 2017-08-18.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class DateUtils {

    public static String getDateFormat() {
        return "yyyy-MM-dd";
    }

    public static String getFarDay(int far) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, far);
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.getDateFormat());
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
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getDayofWeek(String data, String dateType) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(dateType);
            Date nDate = dateFormat.parse(data);
            Calendar c = Calendar.getInstance();
            c.setTime(nDate);
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            return dayOfWeek;
        }
        catch (ParseException e){
            e.printStackTrace();
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

    public static String getIndexOfDayName(int index) {
        String dname;
        switch (index) {
            case 1 :
                dname = "월요일";
                break;
            case 2 :
                dname = "화요일";
                break;
            case  3 :
                dname = "수요일";
                break;
            case 4 :
                dname = "목요일";
                break;
            case 5 :
                dname = "금요일";
                break;
            case 6 :
                dname = "토요일";
                break;
            default :
                dname = "일요일";
                break;
        }
        return dname;
    }

    public static String getIndexofDayNameHead(int index) {
        String dayName = " (일)";
        switch (index) {
            case 1 :
                dayName = " (일)";
                break;
            case 2 :
                dayName = " (월)";
                break;
            case  3 :
                dayName = " (화)";
                break;
            case 4 :
                dayName = " (수)";
                break;
            case 5 :
                dayName = " (목)";
                break;
            case 6 :
                dayName = " (금)";
                break;
            case 7 :
                dayName = " (토)";
                break;
        }
        return dayName;
    }
}
