package com.skydoves.waterdays.ui.customViews

import android.annotation.SuppressLint
import android.content.Context
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.Utils
import kotlinx.android.synthetic.main.custom_marker_view.view.*

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class MyMarkerView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {

  @SuppressLint("SetTextI18n")
  override fun refreshContent(e: Entry, highlight: Highlight) {
    if (e is CandleEntry) {
      tvContent.text = Utils.formatNumber(e.high, 0, true) + "ml"
    } else {
      tvContent.text = Utils.formatNumber(e.`val`, 0, true) + "ml"
    }
  }

  override fun getXOffset(xpos: Float): Int {
    return -(width / 2)
  }

  override fun getYOffset(ypos: Float): Int {
    return -height
  }
}