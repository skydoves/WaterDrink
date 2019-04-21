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

package com.skydoves.waterdays.ui.activities.intro

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

import com.github.paolorotolo.appintro.AppIntro
import com.skydoves.waterdays.R
import com.skydoves.waterdays.WDApplication
import com.skydoves.waterdays.compose.BaseView
import com.skydoves.waterdays.models.Capacity
import com.skydoves.waterdays.persistence.preference.PreferenceKeys
import com.skydoves.waterdays.persistence.preference.PreferenceManager
import com.skydoves.waterdays.persistence.sqlite.SqliteManager
import com.skydoves.waterdays.ui.activities.main.MainActivity
import com.skydoves.waterdays.ui.activities.settings.SetWeightActivity
import com.skydoves.waterdays.ui.fragments.intro.SlideFragment

import javax.inject.Inject

/**
 * Developed by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class StartActivity : AppIntro(), BaseView {

  @Inject
  lateinit var preferenceManager: PreferenceManager
  @Inject
  lateinit var sqliteManager: SqliteManager

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WDApplication.component.inject(this)

    if (!preferenceManager.getBoolean(PreferenceKeys.NEWBE.first, PreferenceKeys.NEWBE.second)) {
      startActivity(Intent(this, MainActivity::class.java))
      finish()
      return
    }

    if (!preferenceManager.getBoolean(PreferenceKeys.INIT_CAPACITY.first, PreferenceKeys.INIT_CAPACITY.second)) {
      sqliteManager.addCapacity(Capacity(null, 125))
      sqliteManager.addCapacity(Capacity(null, 250))
      sqliteManager.addCapacity(Capacity(null, 350))
      sqliteManager.addCapacity(Capacity(null, 500))
      sqliteManager.addCapacity(Capacity(null, 750))
      sqliteManager.addCapacity(Capacity(null, 1000))
      preferenceManager.putBoolean(PreferenceKeys.INIT_CAPACITY.first, true)
    }

    initializeUI()
  }

  override fun initializeUI() {
    addSlide(SlideFragment.newInstance(R.layout.intro1))
    addSlide(SlideFragment.newInstance(R.layout.intro2))
    addSlide(SlideFragment.newInstance(R.layout.intro3))
    addSlide(SlideFragment.newInstance(R.layout.intro4))
    setDoneText(getString(R.string.start))
  }

  override fun onSkipPressed(currentFragment: Fragment?) {
    super.onSkipPressed(currentFragment)
    ActivityStart()
  }

  override fun onDonePressed(currentFragment: Fragment?) {
    super.onDonePressed(currentFragment)
    ActivityStart()
  }

  override fun onSlideChanged(oldFragment: Fragment?, newFragment: Fragment?) {
    super.onSlideChanged(oldFragment, newFragment)
  }

  private fun ActivityStart() {
    startActivity(Intent(this, SetWeightActivity::class.java))
    finish()
  }
}
