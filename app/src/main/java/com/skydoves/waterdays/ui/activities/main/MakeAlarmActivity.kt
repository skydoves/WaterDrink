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

package com.skydoves.waterdays.ui.activities.main

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.NumberPicker
import android.widget.TimePicker
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.skydoves.elasticviews.ElasticCheckButton
import com.skydoves.waterdays.R
import com.skydoves.waterdays.compose.BaseActivity
import com.skydoves.waterdays.compose.qualifiers.RequirePresenter
import com.skydoves.waterdays.events.rx.RxUpdateMainEvent
import com.skydoves.waterdays.presenters.MakeAlarmPresenter
import com.skydoves.waterdays.utils.AlarmUtils
import com.skydoves.waterdays.viewTypes.MakeAlarmActivityView
import kotlinx.android.synthetic.main.activity_make_alarm.*

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@RequirePresenter(MakeAlarmPresenter::class)
class MakeAlarmActivity : BaseActivity<MakeAlarmPresenter, MakeAlarmActivityView>(), MakeAlarmActivityView {

  private var alarmUtils: AlarmUtils? = null
  private var timePickerDialog: TimePickerDialog? = null
  private var selectedType = 0

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_make_alarm)
    initBaseView(this)

    alarmUtils = AlarmUtils(this)
  }

  @SuppressLint("SetTextI18n")
  override fun initializeUI() {
    timePickerDialog = TimePickerDialog(this, android.R.style.Theme_Holo_Dialog, { view: TimePicker, hourOfDay: Int, minute: Int ->
      when (selectedType) {
        0 -> makealram_content1_tv_starttime.text = hourOfDay.toString() + "시 " + minute + "분"
        1 -> makealram_content1_tv_endtime.text = hourOfDay.toString() + "시 " + minute + "분"
      }
    }, 12, 0, true)
    timePickerDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    makealram_np_interval.minValue = 1
    makealram_np_interval.maxValue = 5
    makealram_np_interval.value = 1
    makealram_np_interval.wrapSelectorWheel = false
    makealram_np_interval.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

    makealarm_label_starttime.setOnClickListener { label_setTime(it) }
    makealarm_label_endtime.setOnClickListener { label_setTime(it) }

    makealram_ibtn_q_timeset01.setOnClickListener { ibtn_questionMark(it) }
    makealram_ibtn_q_timeset02.setOnClickListener { ibtn_questionMark(it) }
    makealram_btn_setalarm.setOnClickListener { btn_setAlarm(it) }

    makealarm_tv_day0.setOnClickListener { }
    makealarm_tv_day1.setOnClickListener { }
    makealarm_tv_day2.setOnClickListener { }
    makealarm_tv_day3.setOnClickListener { }
    makealarm_tv_day4.setOnClickListener { }
    makealarm_tv_day5.setOnClickListener { }
    makealarm_tv_day6.setOnClickListener { }
  }

  private fun label_setTime(view: View) {
    val duration = 200
    Handler().postDelayed({
      when (view.id) {
        R.id.makealarm_label_starttime -> {
          selectedType = 0
          timePickerDialog!!.show()
        }

        R.id.makealarm_label_endtime -> {
          selectedType = 1
          timePickerDialog!!.show()
        }
      }
    }, duration.toLong())
  }

  private fun ibtn_questionMark(v: View) {
    when (v.id) {
      R.id.makealram_ibtn_q_timeset01 -> Snackbar.make(makealram_ibtn_q_timeset01, "시작 시간과 종료 시간 사이에만 알람을 받습니다.", Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show()

      R.id.makealram_ibtn_q_timeset02 -> Snackbar.make(makealram_ibtn_q_timeset01, "시작 시간과 종료 시간 사이에 특정 간격으로 알람을 받습니다.", Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show()
    }
  }

  private fun btn_setAlarm(v: View) {
    if (makealram_content1_tv_starttime.text.toString().contains("분") && makealram_content1_tv_endtime.text.toString().contains("분")) {
      val sTime = makealram_content1_tv_starttime.text.toString().split("시".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
      val eTime = makealram_content1_tv_endtime.text.toString().split("시".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
      if (Integer.parseInt(sTime[0]) <= Integer.parseInt(eTime[0])) {
        try {
          val requestCode = presenter.pendingRequest + 1
          var dayList = ""
          for (i in 0 until panel_days.childCount) {
            val elasticCheckButton = panel_days.getChildAt(i) as ElasticCheckButton
            if (!elasticCheckButton.isChecked)
              dayList += "$i,"
          }

          if (dayList == "") {
            Toast.makeText(this, "최소 1개 이상의 요일을 선택해 주세요.", Toast.LENGTH_SHORT).show()
            return
          }

          presenter.addAlarm(requestCode, dayList, makealram_content1_tv_starttime.text.toString(), makealram_content1_tv_endtime.text.toString(), makealram_np_interval.value)
          alarmUtils!!.setAlarm(requestCode)
          presenter.pendingRequest = requestCode

          Toast.makeText(this, "새 알람이 등록되었습니다.", Toast.LENGTH_SHORT).show()
          RxUpdateMainEvent.getInstance().sendEvent()
          finish()
        } catch (e: Exception) {
          e.printStackTrace()
        }

      } else
        Toast.makeText(this, "종료시간은 시작시간 보다 커야합니다.", Toast.LENGTH_SHORT).show()
    } else
      Toast.makeText(this, "시작시간과 종료시간을 설정해 주세요.", Toast.LENGTH_SHORT).show()
  }
}
