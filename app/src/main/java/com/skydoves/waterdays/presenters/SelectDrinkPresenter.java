package com.skydoves.waterdays.presenters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skydoves.waterdays.WDApplication;
import com.skydoves.waterdays.compose.BasePresenter;
import com.skydoves.waterdays.events.rx.RxUpdateMainEvent;
import com.skydoves.waterdays.models.Capacity;
import com.skydoves.waterdays.persistence.sqlite.SqliteManager;
import com.skydoves.waterdays.viewTypes.SelectDrinkActivityView;

import java.util.List;

import javax.inject.Inject;

/**
 * Developed by skydoves on 2017-09-22.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class SelectDrinkPresenter extends BasePresenter<SelectDrinkActivityView> {

    protected @Inject SqliteManager sqliteManager;

    @Override
    protected void onCreate(@NonNull Context context, @Nullable Bundle savedInstanceState) {
        super.onCreate(context, savedInstanceState);
        WDApplication.getComponent().inject(this);
    }

    public List<Capacity> getCapacityItemList() {
        return sqliteManager.getCapacityList();
    }

    public void addRecrodItem(int amount) {
        sqliteManager.addRecord(amount+"");
        RxUpdateMainEvent.getInstance().updateBadge();
    }

    public void addCapacity(Capacity capacity) {
        sqliteManager.addCapacity(capacity);
    }

    public void deleteCapacity(Capacity capacity) {
        sqliteManager.deleteCapacity(capacity);
    }
}