/*
 * Copyright (C) 2016 skydoves
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

  @Inject
  lateinit var sqliteManager: SqliteManager
  @Inject
  lateinit var preferenceManager: PreferenceManager

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
