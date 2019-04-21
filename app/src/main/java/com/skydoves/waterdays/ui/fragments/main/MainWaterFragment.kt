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

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.jorgecastillo.FillableLoader
import com.github.jorgecastillo.FillableLoaderBuilder
import com.github.jorgecastillo.clippingtransforms.WavesClippingTransform
import com.skydoves.waterdays.R
import com.skydoves.waterdays.WDApplication
import com.skydoves.waterdays.events.rx.RxUpdateMainEvent
import com.skydoves.waterdays.persistence.preference.PreferenceKeys
import com.skydoves.waterdays.persistence.preference.PreferenceManager
import com.skydoves.waterdays.persistence.sqlite.SqliteManager
import com.skydoves.waterdays.services.receivers.LocalWeather
import com.skydoves.waterdays.ui.activities.main.SelectDrinkActivity
import com.skydoves.waterdays.utils.DateUtils
import com.skydoves.waterdays.utils.FillableLoaderPaths
import com.skydoves.waterdays.utils.NetworkUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.layout_todaywaterdrink.*
import java.util.concurrent.ExecutionException
import javax.inject.Inject

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class MainWaterFragment : Fragment() {

  @Inject
  lateinit var preferenceManager: PreferenceManager
  @Inject
  lateinit var sqliteManager: SqliteManager

  private var rootView: View? = null
  private var mContext: Context? = null
  private lateinit var fillAbleLoader: FillableLoader

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val rootView = inflater.inflate(R.layout.layout_todaywaterdrink, container, false)
    WDApplication.component.inject(this)
    this.rootView = rootView
    this.mContext = context
    return rootView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initializeUI()
  }

  @SuppressLint("SetTextI18n", "CheckResult")
  private fun initializeUI() {
    // get today's drink amount
    val dAmount = sqliteManager.getDayDrinkAmount(DateUtils.getFarDay(0)).toFloat()
    var dGoal = Integer.parseInt(preferenceManager.getString(PreferenceKeys.WATER_GOAL.first, PreferenceKeys.WATER_GOAL.second)).toFloat()

    if (dGoal <= 0) dGoal = 1f

    // textView - Goal
    drinkamount_tv_goal.text = "${dGoal.toInt()}ml"

    // textView - Drunk
    drinkamount_tv_drunk.text = "${dAmount.toInt()}ml"

    // textView - require drinking amount
    if (dAmount < dGoal) {
      drinkamount_tv_requireamount.text = "${(dGoal - dAmount).toInt()}ml"
    } else {
      drinkamount_tv_requireamount.text = "0ml"
    }

    // textView - today's Reh
    drinkamount_tv_rh.text = preferenceManager.getString(key = "Reh", default_value = "60") + "%"

    val viewSize = resources.getDimensionPixelSize(R.dimen.fourthSampleViewSize)
    val params = RelativeLayout.LayoutParams(viewSize, viewSize)
    params.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 220f, resources.displayMetrics).toInt()
    params.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 305f, resources.displayMetrics).toInt()
    params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)

    val frameLayout = rootView!!.findViewById(R.id.layout_body) as RelativeLayout
    val loaderBuilder = FillableLoaderBuilder()
    this.fillAbleLoader = loaderBuilder.parentView(frameLayout)
        .svgPath(FillableLoaderPaths.SVG_WATERDROP)
        .layoutParams(params)
        .originalDimensions(290, 425)
        .fillColor(Color.parseColor(preferenceManager.getString(PreferenceKeys.BUBBLE_COLOR.first, PreferenceKeys.BUBBLE_COLOR.second)))
        .strokeColor(Color.parseColor(preferenceManager.getString(PreferenceKeys.BUBBLE_COLOR.first, PreferenceKeys.BUBBLE_COLOR.second)))
        .strokeDrawingDuration(0)
        .clippingTransform(WavesClippingTransform())
        .fillDuration(3000)
        .build()

    fillAbleLoader.setSvgPath(FillableLoaderPaths.SVG_WATERDROP)
    fillAbleLoader.setFillColor(Color.WHITE)
    drinkamount_percentage.bringToFront()
    setPercentage()

    drinkamount_refresh.setOnClickListener { onRefresh() }
    drinkamount_fab.setOnClickListener { onDrinkFab() }

    RxUpdateMainEvent.getInstance().observable
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { setPercentage() }
  }

  private fun getPercentage(): Float {
    val dAmount = sqliteManager.getDayDrinkAmount(DateUtils.getFarDay(0)).toFloat()
    var dGoal = Integer.parseInt(preferenceManager.getString(PreferenceKeys.WATER_GOAL.first, PreferenceKeys.WATER_GOAL.second)).toFloat()
    return (dAmount / dGoal * 100).toInt().toFloat()
  }

  @SuppressLint("SetTextI18n")
  private fun setPercentage() {
    // textView - today drunk percentage
    if (getPercentage() < 100) {
      drinkamount_percentage.text = "${getPercentage().toInt()}%"
    } else {
      drinkamount_percentage.text = "100%"
    }

    fillAbleLoader.setPercentage(getPercentage())
    fillAbleLoader.start()
  }

  @SuppressLint("SetTextI18n")
  private fun onRefresh() {
    if (NetworkUtils.isNetworkAvailable(mContext!!)) {
      try {
        // get local weather based on Reh
        val localWeather = LocalWeather(mContext)
        val reh = localWeather.execute().get()

        // refresh textView
        drinkamount_tv_rh.text = "$reh%"

        // save Reh data
        preferenceManager.putString("Reh", reh)
      } catch (e: ExecutionException) {
        e.printStackTrace()
      } catch (e: InterruptedException) {
        e.printStackTrace()
      }

    } else
      Toast.makeText(mContext, "네트워크를 확인해 주세요!", Toast.LENGTH_SHORT).show()
  }

  private fun onDrinkFab() {
    val intent = Intent(mContext, SelectDrinkActivity::class.java)
    startActivity(intent)
  }
}
