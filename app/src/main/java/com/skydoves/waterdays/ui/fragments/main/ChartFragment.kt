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

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ViewPortHandler
import com.skydoves.waterdays.R
import com.skydoves.waterdays.WDApplication
import com.skydoves.waterdays.persistence.sqlite.SqliteManager
import com.skydoves.waterdays.ui.customViews.MyMarkerView
import com.skydoves.waterdays.utils.DateUtils
import kotlinx.android.synthetic.main.layout_chart.*
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class ChartFragment : Fragment(), OnChartValueSelectedListener {

  @Inject
  lateinit var sqliteManager: SqliteManager

  private var rootView: View? = null
  private var dateCount = 0

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val rootView = inflater.inflate(R.layout.layout_chart, container, false)
    WDApplication.component.inject(this)
    this.rootView = rootView
    return rootView
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    // set dateCount
    dateCount = -DateUtils.getDateDay(DateUtils.getFarDay(0), DateUtils.dateFormat)
    initializeChart(DateUtils.getDateDay(DateUtils.getFarDay(0), DateUtils.dateFormat))

    chart_ibtn_back.setOnClickListener { DateMoveButton(it) }
    chart_ibtn_next.setOnClickListener { DateMoveButton(it) }
  }

  internal fun DateMoveButton(v: View) {
    val DayNum = DateUtils.getDateDay(DateUtils.getFarDay(0), DateUtils.dateFormat)
    when (v.id) {
      R.id.chart_ibtn_back -> {
        dateCount -= 7
        initializeChart(6)
      }

      R.id.chart_ibtn_next -> if (dateCount == -DayNum)
        Toast.makeText(context, "다음주의 기록은 볼 수 없습니다.", Toast.LENGTH_SHORT).show()
      else {
        dateCount += 7
        if (dateCount == -DayNum)
          initializeChart(DayNum)
        else
          initializeChart(6)
      }
    }
  }

  /**
   * initialize chartView
   * @param dayCount chart
   */
  private fun initializeChart(dayCount: Int) {
    var TotalAmount = 0f
    var Max = 0f
    var sumCount = 0f
    val entries = ArrayList<Entry>()
    for (i in 0..dayCount) {
      val daySum = sqliteManager!!.getDayDrinkAmount(DateUtils.getFarDay(dateCount + i))

      // get total sum
      TotalAmount += daySum.toFloat()

      // get max
      if (i == 0)
        Max = daySum.toFloat()
      else if (Max < daySum) Max = daySum.toFloat()

      // count
      if (daySum != 0)
        sumCount++

      // add entry
      entries.add(Entry(daySum.toFloat(), i))
    }

    val tv_date = rootView!!.findViewById(R.id.chart_tv_weekdate) as TextView
    tv_date.text = DateUtils.getFarDay(dateCount) + " ~ " + DateUtils.getFarDay(dateCount + 6)

    val tv_total = rootView!!.findViewById(R.id.chart_tv_totaldrink) as TextView
    tv_total.text = String.format("%.1f", TotalAmount / 1000) + " L"

    val tv_average = rootView!!.findViewById(R.id.chart_tv_averagedrink) as TextView
    if (sumCount != 0f)
      tv_average.text = String.format("%.1f", TotalAmount / 1000f / sumCount) + " L"
    else
      tv_average.text = String.format("%.1f", TotalAmount / 1000) + " L"

    val labels = ArrayList<String>()
    labels.add("일")
    labels.add("월")
    labels.add("화")
    labels.add("수")
    labels.add("목")
    labels.add("금")
    labels.add("토")

    val dataset = LineDataSet(entries, "마신 물의 양")
    val data = LineData(labels, dataset)
    chart_mainchart.data = data
    chart_mainchart.setOnChartValueSelectedListener(this)

    val computed = intArrayOf(Color.TRANSPARENT)
    val label = arrayOf("")
    chart_mainchart.setDescription("")
    chart_mainchart.setDescriptionTextSize(16f)
    chart_mainchart.setDescriptionColor(Color.WHITE)
    chart_mainchart.legend.isEnabled = true
    chart_mainchart.legend.isWordWrapEnabled = true
    chart_mainchart.legend.textColor = Color.TRANSPARENT
    chart_mainchart.legend.setCustom(computed, label)

    chart_mainchart.setDrawGridBackground(false)
    chart_mainchart.axisLeft.setDrawGridLines(false)
    chart_mainchart.axisRight.setDrawGridLines(false)
    chart_mainchart.axisRight.textColor = Color.TRANSPARENT
    chart_mainchart.xAxis.setDrawGridLines(false)

    chart_mainchart.setPinchZoom(false)
    chart_mainchart.isDragEnabled = false
    chart_mainchart.setScaleEnabled(false)
    chart_mainchart.animateY(1700)

    val mv = context?.let { MyMarkerView(it, R.layout.custom_marker_view) }
    chart_mainchart.markerView = mv

    // X - axis settings
    val xAxis = chart_mainchart.xAxis
    xAxis.textSize = 14f
    xAxis.spaceBetweenLabels = 1
    xAxis.position = XAxis.XAxisPosition.BOTTOM
    xAxis.textColor = ColorTemplate.getHoloBlue()

    // Y - axis settings
    val leftAxis = chart_mainchart.axisLeft
    leftAxis.textColor = ColorTemplate.getHoloBlue()
    leftAxis.textSize = 14f
    leftAxis.setStartAtZero(true)
    leftAxis.spaceTop = 45f
    leftAxis.valueFormatter = YAxisValueFormatter()

    // set max Y-Axis & chart message
    val tv_chartMessage = rootView!!.findViewById(R.id.chart_tv_message) as TextView
    if (TotalAmount > 0)
      tv_chartMessage.visibility = View.INVISIBLE
    else
      tv_chartMessage.visibility = View.VISIBLE

    // dataSet settings
    dataset.setDrawFilled(true)
    dataset.circleSize = 5f
    dataset.valueTextSize = 13f
    dataset.valueTextColor = Color.WHITE
    dataset.enableDashedHighlightLine(10f, 5f, 0f)
    dataset.valueFormatter = DataSetValueFormatter()
  }

  /**
   * YAxis : Water Y-Value Formatter
   */
  private inner class YAxisValueFormatter : com.github.mikephil.charting.formatter.YAxisValueFormatter {
    override fun getFormattedValue(value: Float, yAxis: YAxis): String {
      return Math.round(value).toString() + "ml"
    }
  }

  /**
   * Water DataSet-Value Formatter
   */
  private inner class DataSetValueFormatter : ValueFormatter {
    override fun getFormattedValue(value: Float, entry: Entry, dataSetIndex: Int, viewPortHandler: ViewPortHandler): String {
      return Math.round(value).toString() + ""
    }
  }

  override fun onValueSelected(e: Entry, dataSetIndex: Int, h: Highlight) {
    val tv_sdTitle = rootView!!.findViewById(R.id.chart_tv03) as TextView
    val dName = DateUtils.getIndexOfDayName(e.xIndex)
    tv_sdTitle.text = dName

    val tv_selectedday = rootView!!.findViewById(R.id.chart_tv_selectedday) as TextView
    tv_selectedday.text = String.format("%.0f", e.`val`) + " ml"
  }

  override fun onNothingSelected() {

  }
}
