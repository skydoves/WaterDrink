package com.skydoves.waterdays.presenters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skydoves.waterdays.WDApplication;
import com.skydoves.waterdays.compose.BasePresenter;
import com.skydoves.waterdays.persistence.preference.PreferenceKeys;
import com.skydoves.waterdays.persistence.preference.PreferenceManager;
import com.skydoves.waterdays.viewTypes.SetGoalActivityView;

import javax.inject.Inject;

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class SetGoalPresenter extends BasePresenter<SetGoalActivityView> {

    protected @Inject PreferenceManager preferenceManager;

    @Override
    protected void onCreate(@NonNull Context context, @Nullable Bundle savedInstanceState) {
        super.onCreate(context, savedInstanceState);
        WDApplication.getComponent().inject(this);
    }

    public void onClickSetGoal(String value) {
        if(checkGoalValid(value)) {
            setWaterGoal(value);
            baseView.onSetSuccess();
            if(getIsNewbe()) baseView.intentMain();
        } else
            baseView.onSetFailure();
    }

    public boolean checkGoalValid(String value) {
        if(value.equals(""))
            return false;
        else if(value.equals("0"))
            return false;
        else
            return true;
    }

    public boolean getIsNewbe() {
        return preferenceManager.getBoolean(PreferenceKeys.NEWBE.first, PreferenceKeys.NEWBE.second);
    }

    public void setOldbe() {
         preferenceManager.putBoolean(PreferenceKeys.NEWBE.first, false);
    }

    public String getWaterGoal() {
        return preferenceManager.getString(PreferenceKeys.WATER_GOAL.first, PreferenceKeys.WATER_GOAL.second);
    }

    public void setWaterGoal(String goal) {
        preferenceManager.putString(PreferenceKeys.WATER_GOAL.first, goal);
    }
}