package com.skydoves.waterdays.persistence.preference;

import android.util.Pair;

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class PreferenceKeys {
    public static final Pair<String, Boolean> NEWBE = new Pair<>("NEWBE", true);
    public static final Pair<String, Boolean> INIT_CAPACITY = new Pair<>("INIT_CAPACITY", false);
    public static final Pair<String, String> WATER_GOAL = new Pair<>("WaterGoal", "2000");
    public static final Pair<String, Integer> LOCALINDEX = new Pair<>("localIndex", 0);
    public static final Pair<String, String> CUP_CAPICITY = new Pair<>("MyCup", "0");
    public static final Pair<String, Boolean> ALARM_WEAHTER = new Pair<>("setWeatherAlarm", false);
}
