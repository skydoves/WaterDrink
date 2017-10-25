package com.skydoves.waterdays.ui.adapters

import android.support.annotation.LayoutRes
import android.view.View

import com.skydoves.waterdays.R
import com.skydoves.waterdays.models.Alarm
import com.skydoves.waterdays.ui.viewholders.AlarmViewHolder
import com.skydoves.waterdays.ui.viewholders.BaseViewHolder

import java.util.ArrayList

/**
 * Developed by skydoves on 2017-08-20.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class AlarmFragmentAdapter(private val delegate: AlarmViewHolder.Delegate) : BaseAdapter() {

    init {
        addSection(ArrayList<Alarm>())
        notifyDataSetChanged()
    }

    fun addAlarmItem(alarmModel: Alarm) {
        sections()[0].add(alarmModel)
        notifyDataSetChanged()
    }

    override fun layout(sectionRow: BaseAdapter.SectionRow): Int {
        return R.layout.item_alarmrecord
    }

    override fun viewHolder(@LayoutRes layout: Int, view: View): BaseViewHolder {
        return AlarmViewHolder(view, delegate)
    }
}
