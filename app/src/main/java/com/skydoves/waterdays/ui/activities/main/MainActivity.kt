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
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import com.skydoves.waterdays.R
import com.skydoves.waterdays.compose.BaseActivity
import com.skydoves.waterdays.compose.qualifiers.RequirePresenter
import com.skydoves.waterdays.consts.IntentExtras
import com.skydoves.waterdays.events.rx.RxUpdateMainEvent
import com.skydoves.waterdays.presenters.MainPresenter
import com.skydoves.waterdays.services.receivers.AlarmBootReceiver
import com.skydoves.waterdays.services.receivers.LocalWeatherReceiver
import com.skydoves.waterdays.ui.adapters.SectionsPagerAdapter
import com.skydoves.waterdays.utils.NavigationUtils
import com.skydoves.waterdays.viewTypes.MainActivityView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.GregorianCalendar

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@RequirePresenter(MainPresenter::class)
class MainActivity : BaseActivity<MainPresenter, MainActivityView>(), MainActivityView {

  private var nAdapter: NfcAdapter? = null
  private lateinit var mSectionsPagerAdapter: SectionsPagerAdapter

  @SuppressLint("CheckResult")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    initBaseView(this)

    nAdapter = NfcAdapter.getDefaultAdapter(this)
    getNFCData(intent)

    // auto weather alarm
    weatherAlarm()

    // set boot receiver
    val receiver = ComponentName(this, AlarmBootReceiver::class.java)
    val pm = packageManager
    pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)

    RxUpdateMainEvent.getInstance().observable
        .compose(bindToLifecycle<Boolean>())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { flag ->
          if (!flag) showBadge(1)
          mSectionsPagerAdapter.notifyDataSetChanged()
        }
  }

  override fun initializeUI() {
    mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
    mainactivity_viewpager.adapter = mSectionsPagerAdapter
    mainactivity_viewpager.offscreenPageLimit = 5
    NavigationUtils.setComponents(baseContext, mainactivity_viewpager, mainactivity_navi)
  }

  /**
   * show badge with delay
   * @param position
   */
  private fun showBadge(position: Int) {
    mainactivity_navi.postDelayed({
      val model = mainactivity_navi.models[position]
      mainactivity_navi.postDelayed({ model.showBadge() }, 100)
    }, 200)
  }

  private fun getNFCData(intent: Intent) {
    if (NfcAdapter.ACTION_NDEF_DISCOVERED == getIntent().action) {
      val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
      if (rawMsgs != null) {
        val messages = arrayOfNulls<NdefMessage>(rawMsgs.size)

        for (i in rawMsgs.indices)
          messages[i] = rawMsgs[i] as NdefMessage
        val payload = messages[0]!!.getRecords()[0].payload

        presenter.addRecord(String(payload))
        mSectionsPagerAdapter!!.notifyDataSetChanged()
        showBadge(1)
      }
    }
  }

  override fun onResume() {
    super.onResume()
    val filter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
    try {
      filter.addDataType("waterdays_nfc/*")
    } catch (e: Exception) {
      e.printStackTrace()
    }

    val i = Intent(this, javaClass)
    i.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
    val pIntent = PendingIntent.getActivity(this, 0, i, 0)

    val filters = arrayOf(filter)
    nAdapter!!.enableForegroundDispatch(this, pIntent, filters, null)
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    setIntent(intent)
    getNFCData(getIntent())
  }

  private fun weatherAlarm() {
    if (!presenter.weatherAlarm) {
      val mCalendar = GregorianCalendar()
      val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
      alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, mCalendar.timeInMillis, (1200 * 1000).toLong(), pendingIntent(IntentExtras.ALARM_PENDING_REQUEST_CODE))
      presenter.weatherAlarm = true
    }
  }

  private fun pendingIntent(requestCode: Int): PendingIntent {
    val intent = Intent(this, LocalWeatherReceiver::class.java)
    intent.putExtra(IntentExtras.ALARM_PENDING_REQUEST, requestCode)
    return PendingIntent.getBroadcast(this, requestCode, intent, 0)
  }
}
