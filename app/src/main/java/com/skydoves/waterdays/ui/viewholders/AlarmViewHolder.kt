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

package com.skydoves.waterdays.ui.viewholders

import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.skydoves.waterdays.models.Alarm
import kotlinx.android.synthetic.main.item_alarmrecord.view.*

/**
 * Developed by skydoves on 2017-08-20.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class AlarmViewHolder(view: View, private val delegate: Delegate) : BaseViewHolder(view) {

  private lateinit var alarmModel: Alarm

  interface Delegate {
    fun onConfirm(alarmModel: Alarm)
  }

  @Throws(Exception::class)
  override fun bindData(data: Any) {
    this.alarmModel = data as Alarm

    itemView.item_alarmrecord_tv_days.text = alarmModel.days
    itemView.item_alarmrecord_tv_times.text = alarmModel.times
    itemView.item_alarmrecord_tv_interval.text = alarmModel.interval
    itemView.item_alarmrecord_btn_delete.setOnClickListener { view ->
      val alertDlg = AlertDialog.Builder(view.context)
      alertDlg.setTitle("알람")
      alertDlg.setPositiveButton("예") { dialog: DialogInterface, which: Int -> delegate.onConfirm(alarmModel) }
      alertDlg.setNegativeButton("아니오") { dialog, which -> dialog.dismiss() }
      alertDlg.setMessage(String.format("해당 알람을 지우시겠습니까?"))
      alertDlg.show()
    }
  }

  override fun onClick(v: View) {}

  override fun onLongClick(v: View): Boolean {
    return false
  }
}
