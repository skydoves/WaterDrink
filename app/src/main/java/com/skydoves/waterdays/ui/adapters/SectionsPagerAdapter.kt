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