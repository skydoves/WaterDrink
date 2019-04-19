package com.skydoves.waterdays.ui.adapters

import android.view.View
import androidx.annotation.LayoutRes
import com.skydoves.waterdays.R
import com.skydoves.waterdays.models.Capacity
import com.skydoves.waterdays.ui.viewholders.BaseViewHolder
import com.skydoves.waterdays.ui.viewholders.SelectDrinkViewHolder
import java.util.ArrayList

/**
 * Developed by skydoves on 2017-09-22.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class SelectDrinkAdapter(private val delegate: SelectDrinkViewHolder.Delegate) : BaseAdapter() {

  init {
    addSection(ArrayList<Capacity>())
    notifyDataSetChanged()
  }

  fun addCapacityItem(capacity: Capacity) {
    sections()[0].add(capacity)
    notifyDataSetChanged()
  }

  fun removeDrinkItem(capacity: Capacity) {
    sections()[0].remove(capacity)
    notifyDataSetChanged()
  }

  override fun layout(sectionRow: SectionRow): Int {
    return R.layout.item_selectdrink
  }

  override fun viewHolder(@LayoutRes layout: Int, view: View): BaseViewHolder {
    return SelectDrinkViewHolder(view, delegate)
  }
}