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
