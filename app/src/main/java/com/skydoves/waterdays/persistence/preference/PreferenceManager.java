package com.skydoves.waterdays.persistence.preference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class PreferenceManager {

    private static final String preferenceKey = "waterdays";

    private Context mContext;

    public PreferenceManager(Context context) {
        this.mContext = context;
    }

    public boolean getBoolean(String key, boolean default_value) {
        SharedPreferences pref = mContext.getSharedPreferences(preferenceKey, mContext.MODE_PRIVATE);
        return pref.getBoolean(key, default_value);
    }

    public int getInt(String key, int default_value) {
        SharedPreferences pref = mContext.getSharedPreferences(preferenceKey, mContext.MODE_PRIVATE);
        return pref.getInt(key, default_value);
    }

    public String getString(String key, String default_value) {
        SharedPreferences pref = mContext.getSharedPreferences(preferenceKey, mContext.MODE_PRIVATE);
        return pref.getString(key, default_value);
    }
    public void putBoolean(String key, boolean default_value) {
        SharedPreferences pref = mContext.getSharedPreferences(preferenceKey, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, default_value).apply();
    }

    public void putInt(String key, int default_value) {
        SharedPreferences pref = mContext.getSharedPreferences(preferenceKey, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, default_value).apply();
    }

    public void putString(String key, String default_value) {
        SharedPreferences pref = mContext.getSharedPreferences(preferenceKey, mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, default_value).apply();
    }
}
