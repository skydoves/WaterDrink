package com.skydoves.waterdays.utils

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager

import com.gigamole.navigationtabbar.ntb.NavigationTabBar
import com.skydoves.waterdays.R

import java.util.ArrayList

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

object NavigationUtils {
  fun getNavigationModels(mContext: Context): ArrayList<NavigationTabBar.Model> {
    val colors = mContext.resources.getStringArray(R.array.default_preview)
    val models = ArrayList<NavigationTabBar.Model>()
    models.add(
        NavigationTabBar.Model.Builder(
            ContextCompat.getDrawable(mContext, R.drawable.ic_bell),
            Color.parseColor(colors[0]))
            .title("알람 설정")
            .badgeTitle("new")
            .build()
    )
    models.add(
        NavigationTabBar.Model.Builder(
            ContextCompat.getDrawable(mContext, R.drawable.ic_note),
            Color.parseColor(colors[1]))
            .title("데일리 기록")
            .badgeTitle("new")
            .build()
    )
    models.add(
        NavigationTabBar.Model.Builder(
            ContextCompat.getDrawable(mContext, R.drawable.ic_drop),
            Color.parseColor(colors[2]))
            .title("수분 섭취량")
            .badgeTitle("new")
            .build()
    )
    models.add(
        NavigationTabBar.Model.Builder(
            ContextCompat.getDrawable(mContext, R.drawable.ic_chart),
            Color.parseColor(colors[3]))
            .title("통계 확인")
            .badgeTitle("new")
            .build()
    )
    models.add(
        NavigationTabBar.Model.Builder(
            ContextCompat.getDrawable(mContext, R.drawable.ic_setting),
            Color.parseColor(colors[4]))
            .title("환경 설정")
            .badgeTitle("new")
            .build()
    )
    return models
  }

  fun setComponents(context: Context, viewPager: ViewPager, navigationTabBar: NavigationTabBar) {
    navigationTabBar.models = NavigationUtils.getNavigationModels(context)
    navigationTabBar.setViewPager(viewPager, 2)
    navigationTabBar.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
      override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

      override fun onPageSelected(position: Int) {
        navigationTabBar.models[position].hideBadge()
      }

      override fun onPageScrollStateChanged(state: Int) {}
    })
  }
}
