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
import com.skydoves.waterdays.utils.DateUtils;
import com.skydoves.waterdays.viewTypes.AlarmScreenActivityView;

import javax.inject.Inject;

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class AlarmScreenPresenter extends BasePresenter<AlarmScreenActivityView> {

    protected @Inject SqliteManager sqliteManager;
    protected @Inject PreferenceManager preferenceManager;

    @Override
    protected void onCreate(@NonNull Context context, @Nullable Bundle savedInstanceState) {
        super.onCreate(context, savedInstanceState);
        WDApplication.getComponent().inject(this);
    }

    public void onCheck() {
        String myCup = preferenceManager.getString(PreferenceKeys.CUP_CAPICITY.first, PreferenceKeys.CUP_CAPICITY.second);
        if(!myCup.equals(PreferenceKeys.CUP_CAPICITY.second)) {
            sqliteManager.addRecord(myCup);
            baseView.onDrink(myCup);
        }
        baseView.onFinish();
    }

    public float getDaliyDrink() {
        return sqliteManager.getDayDrinkAmount(DateUtils.getFarDay(0));
    }

    public String getWaterGoal() {
        return preferenceManager.getString(PreferenceKeys.WATER_GOAL.first, PreferenceKeys.WATER_GOAL.second);
    }
}
