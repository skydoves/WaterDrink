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

package com.skydoves.waterdays.ui.activities.alarm

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.jakewharton.rxbinding2.view.RxView
import com.skydoves.waterdays.R
import com.skydoves.waterdays.compose.BaseActivity
import com.skydoves.waterdays.compose.qualifiers.RequirePresenter
import com.skydoves.waterdays.presenters.AlarmScreenPresenter
import com.skydoves.waterdays.utils.FillableLoaderPaths
import com.skydoves.waterdays.viewTypes.AlarmScreenActivityView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_alarmscreen.*
import java.util.concurrent.TimeUnit

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@RequirePresenter(AlarmScreenPresenter::class)
class AlarmScreenActivity
  : BaseActivity<AlarmScreenPresenter, AlarmScreenActivityView>(), AlarmScreenActivityView
{

  @SuppressLint("CheckResult")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_alarmscreen)
    initBaseView(this)

    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)

    Observable.just("")
        .delay(showMinutes.toLong(), TimeUnit.MINUTES)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) }

    RxView.clicks(findViewById(R.id.alarmscreen_btn_check))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { e ->
          val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
          notificationManager.cancel(1004)
          presenter.onCheck()
          alarmscreen_btn_check.isEnabled = false
        }
  }

  @SuppressLint("SetTextI18n")
  override fun initializeUI() {
    val amount = presenter.daliyDrink
    val goal = Integer.parseInt(presenter.waterGoal).toFloat()

    if (amount / goal * 100 < 100) {
      alarmscreen_percentage.text = (amount / goal * 100).toInt().toString() + "%"
    } else {
      alarmscreen_percentage.text = "100%"
    }

    alarmscreen_fillableLoader.setSvgPath(FillableLoaderPaths.SVG_WATERDROP)
    alarmscreen_fillableLoader.start()
    alarmscreen_fillableLoader.setPercentage(amount / goal * 100)
  }

  @SuppressLint("SetTextI18n")
  override fun onDrink(value: String) {
    val amount = presenter.daliyDrink
    val goal = Integer.parseInt(presenter.waterGoal)
    alarmscreen_fillableLoader.setPercentage(amount / goal * 100)
    alarmscreen_fillableLoader.reset()
    alarmscreen_fillableLoader.start()
    if (amount / goal * 100 < 100) {
      alarmscreen_percentage.text = ((amount + presenter.cupSize.toInt()) / goal * 100).toInt().toString() + "%"
    } else {
      alarmscreen_percentage.text = "100%"
    }
    Toast.makeText(this, value + "ml 만큼 섭취했습니다.", Toast.LENGTH_SHORT).show()
  }

  @SuppressLint("CheckResult")
  override fun onFinish() {
    Observable.just("")
        .delay(2700, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { finish() }
  }

  companion object {
    private val showMinutes = 3
  }
}
