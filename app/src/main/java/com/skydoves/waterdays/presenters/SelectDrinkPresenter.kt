package com.skydoves.waterdays.presenters

import android.content.Context
import android.os.Bundle

import com.skydoves.waterdays.WDApplication
import com.skydoves.waterdays.compose.BasePresenter
import com.skydoves.waterdays.events.rx.RxUpdateMainEvent
import com.skydoves.waterdays.models.Capacity
import com.skydoves.waterdays.persistence.sqlite.SqliteManager
import com.skydoves.waterdays.viewTypes.SelectDrinkActivityView

import javax.inject.Inject

/**
 * Developed by skydoves on 2017-09-22.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class SelectDrinkPresenter : BasePresenter<SelectDrinkActivityView>() {

    @Inject lateinit var sqliteManager: SqliteManager

    override fun onCreate(context: Context, savedInstanceState: Bundle?) {
        super.onCreate(context, savedInstanceState)
        WDApplication.component.inject(this)
    }

    val capacityItemList: List<Capacity> by lazy {
        sqliteManager.capacityList
    }

    fun addRecrodItem(amount: Int) {
        sqliteManager.addRecord("${amount}")
        RxUpdateMainEvent.getInstance().updateBadge()
    }

    fun addCapacity(capacity: Capacity) = sqliteManager.addCapacity(capacity)
    fun deleteCapacity(capacity: Capacity) = sqliteManager.deleteCapacity(capacity)
}