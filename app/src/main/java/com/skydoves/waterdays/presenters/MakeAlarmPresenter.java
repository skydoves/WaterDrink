package com.skydoves.waterdays.presenters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skydoves.waterdays.WDApplication;
import com.skydoves.waterdays.compose.BasePresenter;
import com.skydoves.waterdays.consts.IntentExtras;
import com.skydoves.waterdays.persistence.preference.PreferenceManager;
import com.skydoves.waterdays.persistence.sqlite.SqliteManager;
import com.skydoves.waterdays.viewTypes.MakeAlarmActivityView;

import javax.inject.Inject;

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class MakeAlarmPresenter extends BasePresenter<MakeAlarmActivityView> {

    protected @Inject PreferenceManager preferenceManager;
    protected @Inject SqliteManager sqliteManager;

    @Override
    protected void onCreate(@NonNull Context context, @Nullable Bundle savedInstanceState) {
        super.onCreate(context, savedInstanceState);
        WDApplication.getComponent().inject(this);
    }

    public int getPendingRequest() {
       return preferenceManager.getInt(IntentExtras.ALARM_PENDING_REQUEST, 0);
    }

    public void setPendingRequest(int value) {
        preferenceManager.putInt(IntentExtras.ALARM_PENDING_REQUEST, value);
    }

    public void addAlarm(int requestCode, String dayList, String startTime, String endTime, int interval) {
        sqliteManager.addAlarm(requestCode, dayList, startTime, endTime, interval);
    }
}
