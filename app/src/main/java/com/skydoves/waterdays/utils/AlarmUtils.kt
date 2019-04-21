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

package com.skydoves.waterdays.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.skydoves.waterdays.WDApplication
import com.skydoves.waterdays.consts.IntentExtras
import com.skydoves.waterdays.persistence.sqlite.SqliteManager
import com.skydoves.waterdays.services.receivers.AlarmReceiver
import timber.log.Timber
import java.util.Calendar
import java.util.GregorianCalendar
import javax.inject.Inject

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-18.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class AlarmUtils(private val mContext: Context) {

  @Inject
  lateinit var sqliteManager: SqliteManager
  private val mManager: AlarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

  init {
    WDApplication.component.inject(this)
  }

  fun setAlarm(requestcode: Int) {
    val mCalendar = GregorianCalendar()
    var daylist = ""
    var StartTime = ""
    var EndTime = ""
    var interval = 1
    val cursor = sqliteManager.readableDatabase.rawQuery("select * from AlarmList where requestcode = " + requestcode, null)
    try {
      if (cursor != null && cursor.count > 0 && cursor.moveToFirst()) {
        do {
          daylist = cursor.getString(1)
          StartTime = cursor.getString(2)
          EndTime = cursor.getString(3)
          interval = cursor.getInt(4)
        } while (cursor.moveToNext())
      }

      val sdate_h = StartTime.split("시 ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
      val sdate_m = sdate_h[1].split("분".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
      val edate_h = EndTime.split("시 ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
      val edate_m = edate_h[1].split("분".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

      var check_overday = false
      val mDayList = daylist.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
      for (i in mDayList.indices) {
        if (DateUtils.getDateDay(DateUtils.getFarDay(0), DateUtils.dateFormat) <= Integer.parseInt(mDayList[i])) {
          val c_date = Integer.parseInt(mDayList[i]) - DateUtils.getDateDay(DateUtils.getFarDay(0), DateUtils.dateFormat)
          val d_date = DateUtils.getFarDay(c_date)
          val l_date = d_date.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

          if (c_date < 0)
            continue
          else if (c_date == 0 && mCalendar.get(Calendar.HOUR_OF_DAY) > Integer.parseInt(edate_h[0]))
            continue
          else if (c_date == 0 && mCalendar.get(Calendar.HOUR_OF_DAY) >= Integer.parseInt(edate_h[0]) && mCalendar.get(Calendar.MINUTE) > Integer.parseInt(edate_m[0])) continue

          mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), Integer.parseInt(l_date[2]), Integer.parseInt(sdate_h[0]), Integer.parseInt(sdate_m[0]))
          mManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, mCalendar.timeInMillis, (interval * 3600 * 1000).toLong(), pendingIntent(requestcode, Integer.parseInt(mDayList[i])))
          check_overday = true
          break
        }
      }

      if (!check_overday) {
        val c_date = 7 - (DateUtils.getDateDay(DateUtils.getFarDay(0), DateUtils.dateFormat) - Integer.parseInt(mDayList[0]))
        val d_date = DateUtils.getFarDay(c_date)
        val l_date = d_date.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        mCalendar.set(mCalendar.get(Calendar.YEAR), Integer.parseInt(l_date[1]) - 1, Integer.parseInt(l_date[2]), Integer.parseInt(sdate_h[0]), Integer.parseInt(sdate_m[0]))
        mManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, mCalendar.timeInMillis, (interval * 3600 * 1000).toLong(), pendingIntent(requestcode, Integer.parseInt(mDayList[0])))
      }
    } catch (e: Exception) {
      e.printStackTrace()
    } finally {
      cursor!!.close()
    }
  }

  @SuppressLint("Recycle")
  fun getEndTime(requestcode: Int): String? {
    val cursor = sqliteManager.readableDatabase.rawQuery("select * from AlarmList where requestcode = " + requestcode, null)
    try {
      var e_Time = ""
      if (cursor != null && cursor.count > 0 && cursor.moveToFirst()) {
        do {
          e_Time = cursor.getString(3)
        } while (cursor.moveToNext())
      }

      val eTime_h = e_Time.split("시 ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
      val eTime_m = eTime_h[1].split("분".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

      return eTime_h[0] + "," + eTime_m[0]
    } catch (e: Exception) {
      e.printStackTrace()
    } finally {
      cursor!!.close()
    }
    return null
  }

  fun cancelAlarm(requestcode: Int) {
    try {
      if (pendingIntent(requestcode, -1) != null) {
        mManager.cancel(pendingIntent(requestcode, -1))
        Timber.e("alarm canceled")
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }

  }

  private fun pendingIntent(requestcode: Int, occurdateint: Int): PendingIntent? {
    val intent = Intent(mContext, AlarmReceiver::class.java)
    intent.putExtra(IntentExtras.ALARM_PENDING_REQUEST, requestcode)
    intent.putExtra(IntentExtras.ALARM_PENDING_OCCUR_TIME, occurdateint)
    return PendingIntent.getBroadcast(mContext, requestcode, intent, PendingIntent.FLAG_UPDATE_CURRENT)
  }
}
