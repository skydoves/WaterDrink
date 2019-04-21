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

package com.skydoves.waterdays.ui.fragments.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.skydoves.waterdays.R
import com.skydoves.waterdays.WDApplication
import com.skydoves.waterdays.events.rx.RxUpdateMainEvent
import com.skydoves.waterdays.models.Alarm
import com.skydoves.waterdays.persistence.sqlite.SqliteManager
import com.skydoves.waterdays.ui.activities.main.MakeAlarmActivity
import com.skydoves.waterdays.ui.adapters.AlarmFragmentAdapter
import com.skydoves.waterdays.ui.viewholders.AlarmViewHolder
import com.skydoves.waterdays.utils.AlarmUtils
import com.skydoves.waterdays.utils.DateUtils
import kotlinx.android.synthetic.main.layout_setnotification.*
import javax.inject.Inject

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-20.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class AlarmFragment : Fragment() {

  @Inject
  lateinit var sqliteManager: SqliteManager
  @Inject
  lateinit var alarmUtils: AlarmUtils

  private var rootView: View? = null
  private var adapter: AlarmFragmentAdapter? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val rootView = inflater!!.inflate(R.layout.layout_setnotification, container, false)
    WDApplication.component.inject(this)
    this.rootView = rootView
    return rootView
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    InitializeUI()
  }

  private fun InitializeUI() {
    adapter = AlarmFragmentAdapter(delegate)
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(context)

    val cursor = sqliteManager.readableDatabase.rawQuery("select * from AlarmList", null)
    try {
      if (cursor != null && cursor.count > 0 && cursor.moveToLast()) {
        do {
          val requestCode = cursor.getInt(0)
          val days = cursor.getString(1)
          val startTime = cursor.getString(2)
          val endTime = cursor.getString(3)
          val interval = cursor.getString(4)
          val sday = DateUtils.getDayNameList(days)
          val alarmModel = Alarm(requestCode, sday, "$startTime ~ $endTime", "간격 : " + interval + "시간")
          adapter!!.addAlarmItem(alarmModel)
        } while (cursor.moveToPrevious())
      } else
        setnotification_tv_message.visibility = View.VISIBLE
    } catch (e: Exception) {
      e.printStackTrace()
    } finally {
      cursor!!.close()
    }

    setnotification_fab.setOnClickListener {
      startActivity(Intent(activity, MakeAlarmActivity::class.java))
    }
  }

  private val delegate = object : AlarmViewHolder.Delegate {
    override fun onConfirm(alarmModel: Alarm) {
      try {
        alarmUtils.cancelAlarm(alarmModel.requestCode)
        sqliteManager.deleteAlarm(alarmModel.requestCode)
        adapter!!.sections()[0].remove(alarmModel)

        Toast.makeText(context, "알람이 삭제 되었습니다.", Toast.LENGTH_SHORT).show()
        RxUpdateMainEvent.getInstance().sendEvent()
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }
}
