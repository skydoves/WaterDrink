package com.skydoves.waterdays.models;

/**
 * Developed by skydoves on 2017-08-20.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class AlarmModel {
    private int requestCode;
    private String days;
    private String times;
    private String interval;

    public AlarmModel(int requestCode, String days, String times, String interval) {
        this.requestCode = requestCode;
        this.days = days;
        this.times = times;
        this.interval = interval;
    }

    public int getrequestCode() {
        return requestCode;
    }

    public String getDays() {
        return days;
    }

    public String getTimes() {
        return times;
    }

    public String getInterval() {
        return interval;
    }
}
