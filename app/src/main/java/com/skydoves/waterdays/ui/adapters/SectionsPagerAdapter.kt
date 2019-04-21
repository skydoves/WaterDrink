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

package com.skydoves.waterdays.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.skydoves.waterdays.ui.fragments.main.AlarmFragment
import com.skydoves.waterdays.ui.fragments.main.ChartFragment
import com.skydoves.waterdays.ui.fragments.main.DailyFragment
import com.skydoves.waterdays.ui.fragments.main.EnvironmentFragment
import com.skydoves.waterdays.ui.fragments.main.MainWaterFragment

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class SectionsPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

  override fun getItem(position: Int): Fragment {
    var fragment: Fragment = MainWaterFragment()
    when (position) {
      0 -> fragment = AlarmFragment()
      1 -> fragment = DailyFragment()
      2 -> fragment = MainWaterFragment()
      3 -> fragment = ChartFragment()
      4 -> fragment = EnvironmentFragment()
    }
    return fragment
  }

  override fun getCount(): Int = COUNT_PAGERS

  companion object {
    const val COUNT_PAGERS = 5
  }
}
