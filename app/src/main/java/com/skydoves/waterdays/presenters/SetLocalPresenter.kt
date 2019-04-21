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
import com.skydoves.waterdays.viewTypes.SetLocalActivityView

import javax.inject.Inject

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class SetLocalPresenter : BasePresenter<SetLocalActivityView>() {

  @Inject
  lateinit var preferenceManager: PreferenceManager

  override fun onCreate(context: Context, savedInstanceState: Bundle?) {
    super.onCreate(context, savedInstanceState)
    WDApplication.component.inject(this)
  }

  var localIndex: Int
    get() = preferenceManager!!.getInt(PreferenceKeys.LOCALINDEX.first, PreferenceKeys.LOCALINDEX.second)
    set(value) = preferenceManager!!.putInt(PreferenceKeys.LOCALINDEX.first, value)
}
