package com.skydoves.waterdays.presenters

import android.content.Context
import android.os.Bundle

import com.skydoves.waterdays.WDApplication
import com.skydoves.waterdays.compose.BasePresenter
import com.skydoves.waterdays.persistence.preference.PreferenceKeys
import com.skydoves.waterdays.persistence.preference.PreferenceManager
import com.skydoves.waterdays.persistence.sqlite.SqliteManager
import com.skydoves.waterdays.viewTypes.MainActivityView

import javax.inject.Inject

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class MainPresenter : BasePresenter<MainActivityView>() {

  @Inject
  lateinit var preferenceManager: PreferenceManager
  @Inject
  lateinit var sqliteManager: SqliteManager

  override fun onCreate(context: Context, savedInstanceState: Bundle?) {
    super.onCreate(context, savedInstanceState)
    WDApplication.component.inject(this)
  }

  fun addRecord(value: String) = sqliteManager.addRecord(value)

  var weatherAlarm: Boolean
    get() = preferenceManager.getBoolean(PreferenceKeys.ALARM_WEAHTER.first, PreferenceKeys.ALARM_WEAHTER.second)
    set(value) = preferenceManager.putBoolean(PreferenceKeys.ALARM_WEAHTER.first, value)
}
