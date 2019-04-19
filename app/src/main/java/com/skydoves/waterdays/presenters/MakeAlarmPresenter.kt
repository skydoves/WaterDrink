package com.skydoves.waterdays.presenters

import android.content.Context
import android.os.Bundle

import com.skydoves.waterdays.WDApplication
import com.skydoves.waterdays.compose.BasePresenter
import com.skydoves.waterdays.consts.IntentExtras
import com.skydoves.waterdays.persistence.preference.PreferenceManager
import com.skydoves.waterdays.persistence.sqlite.SqliteManager
import com.skydoves.waterdays.viewTypes.MakeAlarmActivityView

import javax.inject.Inject

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class MakeAlarmPresenter : BasePresenter<MakeAlarmActivityView>() {

  @Inject
  lateinit var preferenceManager: PreferenceManager
  @Inject
  lateinit var sqliteManager: SqliteManager

  override fun onCreate(context: Context, savedInstanceState: Bundle?) {
    super.onCreate(context, savedInstanceState)
    WDApplication.component.inject(this)
  }

  var pendingRequest: Int
    get() = preferenceManager.getInt(IntentExtras.ALARM_PENDING_REQUEST, 0)
    set(value) = preferenceManager.putInt(IntentExtras.ALARM_PENDING_REQUEST, value)

  fun addAlarm(requestCode: Int, dayList: String, startTime: String, endTime: String, interval: Int) {
    sqliteManager.addAlarm(requestCode, dayList, startTime, endTime, interval)
  }
}
