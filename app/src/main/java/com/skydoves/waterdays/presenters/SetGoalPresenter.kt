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
import com.skydoves.waterdays.viewTypes.SetGoalActivityView

import javax.inject.Inject

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class SetGoalPresenter : BasePresenter<SetGoalActivityView>() {

  @Inject
  lateinit var preferenceManager: PreferenceManager

  override fun onCreate(context: Context, savedInstanceState: Bundle?) {
    super.onCreate(context, savedInstanceState)
    WDApplication.component.inject(this)
  }

  fun onClickSetGoal(value: String) {
    if (checkGoalValid(value)) {
      waterGoal = value
      baseView.onSetSuccess()
      if (isNewbe) baseView.intentMain()
    } else
      baseView.onSetFailure()
  }

  fun checkGoalValid(value: String): Boolean {
    when (value) {
      "" -> return false
      "0" -> return false
    }
    return true
  }

  val isNewbe: Boolean by lazy {
    preferenceManager.getBoolean(PreferenceKeys.NEWBE.first, PreferenceKeys.NEWBE.second)
  }

  fun setOldbe() = preferenceManager.putBoolean(PreferenceKeys.NEWBE.first, false)

  var waterGoal: String
    get() = preferenceManager.getString(PreferenceKeys.WATER_GOAL.first, PreferenceKeys.WATER_GOAL.second)
    set(goal) = preferenceManager.putString(PreferenceKeys.WATER_GOAL.first, goal)
}
