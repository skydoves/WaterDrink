package com.skydoves.waterdays.ui.fragments.main

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.github.jorgecastillo.FillableLoaderBuilder
import com.github.jorgecastillo.clippingtransforms.WavesClippingTransform
import com.skydoves.waterdays.R
import com.skydoves.waterdays.WDApplication
import com.skydoves.waterdays.persistence.preference.PreferenceKeys
import com.skydoves.waterdays.persistence.preference.PreferenceManager
import com.skydoves.waterdays.persistence.sqlite.SqliteManager
import com.skydoves.waterdays.services.receivers.LocalWeather
import com.skydoves.waterdays.ui.activities.main.SelectDrinkActivity
import com.skydoves.waterdays.utils.DateUtils
import com.skydoves.waterdays.utils.FillableLoaderPaths
import com.skydoves.waterdays.utils.NetworkUtils
import kotlinx.android.synthetic.main.layout_todaywaterdrink.*
import java.util.concurrent.ExecutionException
import javax.inject.Inject

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class MainWaterFragment : Fragment() {

    @Inject lateinit var preferenceManager: PreferenceManager
    @Inject lateinit var sqliteManager: SqliteManager

    private var rootView: View? = null
    private var mContext: Context? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.layout_todaywaterdrink, container, false)
        WDApplication.component.inject(this)
        this.rootView = rootView
        this.mContext = context
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        InitializeUI()
    }

    private fun InitializeUI() {
        // get today's drink amount
        val dAmount = sqliteManager.getDayDrinkAmount(DateUtils.getFarDay(0)).toFloat()
        var dGoal = Integer.parseInt(preferenceManager.getString(PreferenceKeys.WATER_GOAL.first, PreferenceKeys.WATER_GOAL.second)).toFloat()

        if (dGoal <= 0) dGoal = 1f

        // textView - Goal
        val tv_goal = rootView!!.findViewById(R.id.drinkamount_tv_goal) as TextView
        tv_goal.text = "${dGoal.toInt()}ml"

        // textView - Drunk
        val tv_drink = rootView!!.findViewById(R.id.drinkamount_tv_drunk) as TextView
        tv_drink.text = "${dAmount.toInt()}ml"

        // textView - require drinking amount
        val tv_remain = rootView!!.findViewById(R.id.drinkamount_tv_requireamount) as TextView
        if (dAmount < dGoal)
            tv_remain.text = "${(dGoal - dAmount).toInt()}ml"
        else
            tv_remain.text = "0ml"

        // textView - today's Reh
        val tv_rh = rootView!!.findViewById(R.id.drinkamount_tv_rh) as TextView
        tv_rh.text = preferenceManager.getString("Reh", "60") + "%"

        // textView - today drunk percentage
        val tv_percentage = rootView!!.findViewById(R.id.drinkamount_percentage) as TextView
        if (dAmount / dGoal * 100 < 100)
            tv_percentage.text = "${(dAmount / dGoal * 100).toInt()}%"
        else
            tv_percentage.text = "100%"

        val viewSize = resources.getDimensionPixelSize(R.dimen.fourthSampleViewSize)
        val params = RelativeLayout.LayoutParams(viewSize, viewSize)
        params.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 220f, resources.displayMetrics).toInt()
        params.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 305f, resources.displayMetrics).toInt()
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)

        val frameLayout = rootView!!.findViewById(R.id.layout_body) as RelativeLayout
        val loaderBuilder = FillableLoaderBuilder()
        val fillableLoader = loaderBuilder.parentView(frameLayout)
                .svgPath(FillableLoaderPaths.SVG_WATERDROP)
                .layoutParams(params)
                .originalDimensions(290, 425)
                .fillColor(Color.parseColor(preferenceManager.getString(PreferenceKeys.BUBBLE_COLOR.first, PreferenceKeys.BUBBLE_COLOR.second)))
                .strokeColor(Color.parseColor(preferenceManager.getString(PreferenceKeys.BUBBLE_COLOR.first, PreferenceKeys.BUBBLE_COLOR.second)))
                .strokeDrawingDuration(0)
                .clippingTransform(WavesClippingTransform())
                .fillDuration(3000)
                .build()

        fillableLoader.setSvgPath(FillableLoaderPaths.SVG_WATERDROP)
        fillableLoader.setFillColor(Color.WHITE)
        fillableLoader.setPercentage((dAmount / dGoal * 100).toInt().toFloat())
        fillableLoader.start()
        tv_percentage.bringToFront()

        drinkamount_refresh.setOnClickListener { onRefresh() }
        drinkamount_fab.setOnClickListener { onDrinkFab() }
    }

    internal fun onRefresh() {
        if (NetworkUtils.isNetworkAvailable(mContext!!)) {
            try {
                // get local weather based on Reh
                val localWeather = LocalWeather(mContext)
                val Reh = localWeather.execute().get()

                // refresh textView
                val tv_rh = rootView!!.findViewById(R.id.drinkamount_tv_rh) as TextView
                tv_rh.text = "${Reh}%"

                // save Reh data
                preferenceManager.putString("Reh", Reh)
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        } else
            Toast.makeText(mContext, "네트워크를 확인해 주세요!", Toast.LENGTH_SHORT).show()
    }

    internal fun onDrinkFab() {
        val intent = Intent(mContext, SelectDrinkActivity::class.java)
        startActivity(intent)
    }
}
