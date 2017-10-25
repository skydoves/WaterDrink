package com.skydoves.waterdays.presenters

import android.content.Context
import android.os.Bundle

import com.skydoves.waterdays.WDApplication
import com.skydoves.waterdays.compose.BasePresenter
import com.skydoves.waterdays.persistence.preference.PreferenceKeys
import com.skydoves.waterdays.persistence.preference.PreferenceManager
import com.skydoves.waterdays.persistence.sqlite.SqliteManager
import com.skydoves.waterdays.utils.DateUtils
import com.skydoves.waterdays.viewTypes.AlarmScreenActivityView

import javax.inject.Inject

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class AlarmScreenPresenter : BasePresenter<AlarmScreenActivityView>() {

    @Inject lateinit var sqliteManager: SqliteManager
    @Inject lateinit var preferenceManager: PreferenceManager

    override fun onCreate(context: Context, savedInstanceState: Bundle?) {
        WDApplication.component.inject(this)
        super.onCreate(context, savedInstanceState)
    }

    fun onCheck() {
        val myCup = preferenceManager.getString(PreferenceKeys.CUP_CAPICITY.first, PreferenceKeys.CUP_CAPICITY.second)
        if (myCup != PreferenceKeys.CUP_CAPICITY.second) {
            sqliteManager.addRecord(myCup)
            baseView.onDrink(myCup)
        }
        baseView.onFinish()
    }

    val daliyDrink: Float by lazy {
        sqliteManager.let { sqliteManager.getDayDrinkAmount(DateUtils.getFarDay(0)).toFloat() }
    }

    val cupSize: String by lazy {
        sqliteManager.let { preferenceManager.getString("MyCup", "250") }
    }

    val waterGoal: String by lazy {
        preferenceManager.let { preferenceManager.getString(PreferenceKeys.WATER_GOAL.first, PreferenceKeys.WATER_GOAL.second) }
    }
}
