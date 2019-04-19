package com.skydoves.waterdays.ui.adapters

import android.view.View
import androidx.annotation.LayoutRes
import com.skydoves.waterdays.R
import com.skydoves.waterdays.models.Drink
import com.skydoves.waterdays.ui.viewholders.BaseViewHolder
import com.skydoves.waterdays.ui.viewholders.DailyDrinkViewHolder
import java.util.ArrayList

/**
 * Developed by skydoves on 2017-08-20.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class DailyDrinkAdapter(private val delegate: DailyDrinkViewHolder.Delegate) : BaseAdapter() {

  init {
    addSection(ArrayList<Drink>())
    notifyDataSetChanged()
  }

  fun addDrinkItem(drink: Drink) {
    sections()[0].add(drink)
    notifyDataSetChanged()
  }

  fun updateDrinkItem(position: Int, drink: Drink) {
    sections()[0][position] = drink
    notifyDataSetChanged()
  }

  fun getPosition(drink: Drink): Int {
    return sections()[0].indexOf(drink)
  }

  fun remove(drink: Drink) {
    sections()[0].remove(drink)
    notifyDataSetChanged()
  }

  fun clear() {
    sections()[0].let { sections()[0].clear() }
    notifyDataSetChanged()
  }

  override fun layout(sectionRow: SectionRow): Int {
    return R.layout.item_dailyrecord
  }

  override fun viewHolder(@LayoutRes layout: Int, view: View): BaseViewHolder {
    return DailyDrinkViewHolder(view, delegate)
  }
}