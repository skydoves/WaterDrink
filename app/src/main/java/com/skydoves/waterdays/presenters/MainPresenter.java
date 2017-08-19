package com.skydoves.waterdays.presenters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skydoves.waterdays.WDApplication;
import com.skydoves.waterdays.compose.BasePresenter;
import com.skydoves.waterdays.persistence.preference.PreferenceKeys;
import com.skydoves.waterdays.persistence.preference.PreferenceManager;
import com.skydoves.waterdays.persistence.sqlite.SqliteManager;
import com.skydoves.waterdays.viewTypes.MainActivityView;

import javax.inject.Inject;

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class MainPresenter extends BasePresenter<MainActivityView> {

    protected @Inject PreferenceManager preferenceManager;
    protected @Inject SqliteManager sqliteManager;

    @Override
    protected void onCreate(@NonNull Context context, @Nullable Bundle savedInstanceState) {
        super.onCreate(context, savedInstanceState);
        WDApplication.getComponent().inject(this);
    }

    public void addRecord(String value) {
        sqliteManager.addRecord(value);
    }

    public boolean getWeatherAlarm() {
        return preferenceManager.getBoolean(PreferenceKeys.ALARM_WEAHTER.first, PreferenceKeys.ALARM_WEAHTER.second);
    }

    public void setWeatherAlarm(boolean value) {
        preferenceManager.putBoolean(PreferenceKeys.ALARM_WEAHTER.first, value);
    }
}
