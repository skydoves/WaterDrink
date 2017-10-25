package com.skydoves.waterdays.ui.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import com.skydoves.waterdays.ui.fragments.main.*

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

    override fun getItemPosition(item: Any?): Int = PagerAdapter.POSITION_NONE

    companion object {
        val COUNT_PAGERS = 5
    }
}