package com.skydoves.waterdays.ui.viewholders

import android.view.View
import com.skydoves.waterdays.models.Capacity
import kotlinx.android.synthetic.main.item_selectdrink.view.*

/**
 * Developed by skydoves on 2017-09-22.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class SelectDrinkViewHolder(view: View, private val delegate: Delegate) : BaseViewHolder(view) {

  private lateinit var capacity: Capacity

  interface Delegate {
    fun onClick(view: View, capacity: Capacity)
    fun onLongClick(view: View, capacity: Capacity)
  }

  @Throws(Exception::class)
  override fun bindData(data: Any) {
    this.capacity = data as Capacity

    itemView.item_selectdrink_img.setImageDrawable(capacity.image)
    itemView.item_selectdrink_tv.text = "${capacity.amount}ml"
  }

  override fun onClick(v: View) {
    this.delegate.onClick(this.view(), capacity)
  }

  override fun onLongClick(v: View): Boolean {
    this.delegate.onLongClick(this.view(), capacity)
    return true
  }
}