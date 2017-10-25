package com.skydoves.waterdays.presenters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skydoves.waterdays.WDApplication;
import com.skydoves.waterdays.compose.BasePresenter;
import com.skydoves.waterdays.persistence.preference.PreferenceKeys;
import com.skydoves.waterdays.persistence.preference.PreferenceManager;
import com.skydoves.waterdays.viewTypes.SetLocalActivityView;

import javax.inject.Inject;

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class SetLocalPresenter extends BasePresenter<SetLocalActivityView> {

    protected @Inject PreferenceManager preferenceManager;

    @Override
    protected void onCreate(@NonNull Context context, @Nullable Bundle savedInstanceState) {
        super.onCreate(context, savedInstanceState);
        WDApplication.getComponent().inject(this);
    }

    public int getLocalIndex() {
        return preferenceManager.getInt(PreferenceKeys.LOCALINDEX.first, PreferenceKeys.LOCALINDEX.second);
    }

    public void setLocalIndex(int value) {
        preferenceManager.putInt(PreferenceKeys.LOCALINDEX.first, value);
    }
}
