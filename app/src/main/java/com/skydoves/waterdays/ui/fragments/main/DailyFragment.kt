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
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.skydoves.waterdays.R
import com.skydoves.waterdays.WDApplication
import com.skydoves.waterdays.consts.CapacityDrawable
import com.skydoves.waterdays.events.rx.RxUpdateMainEvent
import com.skydoves.waterdays.models.Drink
import com.skydoves.waterdays.persistence.sqlite.SqliteManager
import com.skydoves.waterdays.ui.adapters.DailyDrinkAdapter
import com.skydoves.waterdays.ui.viewholders.DailyDrinkViewHolder
import com.skydoves.waterdays.utils.DateUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.layout_dailyrecord.*
import javax.inject.Inject

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class DailyFragment : Fragment() {

  @Inject
  lateinit var sqliteManager: SqliteManager

  private var rootView: View? = null
  private val adapter: DailyDrinkAdapter by lazy { DailyDrinkAdapter(delegate) }

  private var dateCount = 0

  override fun onAttach(context: Context) {
    WDApplication.component.inject(this)
    super.onAttach(context)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    this.rootView  = inflater.inflate(R.layout.layout_dailyrecord, container, false)
    return rootView
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    initializeUI()
  }

  @SuppressLint("CheckResult")
  private fun initializeUI() {
    dailyrecord_listview.adapter = adapter
    addItems(DateUtils.getFarDay(0))
    previous.setOnClickListener { dateMoveButton(it) }
    next.setOnClickListener { dateMoveButton(it) }
    RxUpdateMainEvent.getInstance().observable
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          adapter.clear()
          addItems(DateUtils.getFarDay(0))
        }
  }

  /**
   * daily drink viewHolder delegate
   */
  private val delegate = object : DailyDrinkViewHolder.Delegate {
    override fun onClick(view: View, drink: Drink) {
      val alert = AlertDialog.Builder(context!!)
      alert.setTitle(getString(R.string.title_edit_capacity))
      val input = EditText(context)
      input.inputType = InputType.TYPE_CLASS_NUMBER
      input.setRawInputType(Configuration.KEYBOARD_12KEY)
      alert.setView(input)
      alert.setPositiveButton(getString(R.string.yes)) { _: DialogInterface, _: Int ->
        try {
          val amount = Integer.parseInt(input.text.toString())
          if (amount in 1..2999) {
            sqliteManager.updateRecordAmount(drink.index, amount)
            val drink_edited = Drink(drink.index, amount.toString() + "ml", drink.date, ContextCompat.getDrawable(context!!, CapacityDrawable.getLayout(amount))!!)
            val position = adapter.getPosition(drink)
            if (position != -1) {
              adapter.updateDrinkItem(position, drink_edited)
              RxUpdateMainEvent.getInstance().sendEvent()
              Toast.makeText(context, R.string.msg_edited_capacity, Toast.LENGTH_SHORT).show()
            }
          } else
            Toast.makeText(context, R.string.msg_invalid_input, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
          e.printStackTrace()
        }
      }

      alert.setNegativeButton(getString(R.string.no)) { _: DialogInterface, _: Int -> }
      alert.show()
      val mgr = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
      mgr.showSoftInputFromInputMethod(input.applicationWindowToken, InputMethodManager.SHOW_FORCED)
    }

    override fun onConfirm(drink: Drink) {
      sqliteManager.deleteRecord(drink.index)
      adapter.remove(drink)
      RxUpdateMainEvent.getInstance().sendEvent()
    }
  }

  private fun dateMoveButton(v: View) {
    when (v.id) {
      R.id.previous -> {
        dateCount--
        addItems(DateUtils.getFarDay(dateCount))
      }

      R.id.next -> if (dateCount < 0) {
        dateCount++
        addItems(DateUtils.getFarDay(dateCount))
      } else
        Toast.makeText(context, "내일의 기록은 볼 수 없습니다.", Toast.LENGTH_SHORT).show()
    }
  }

  /**
   * add items
   * @param date now date value
   */
  private fun addItems(date: String) {
    val tv_todayDate = rootView!!.findViewById(R.id.dailyrecord_tv_todaydate) as TextView
    tv_todayDate.text = date

    // append day of week Label
    if (dateCount == 0) {
      tv_todayDate.append(" (오늘)")
    } else {
      val dayOfWeek = DateUtils.getDayofWeek(date, DateUtils.dateFormat)
      tv_todayDate.append(DateUtils.getIndexofDayNameHead(dayOfWeek))
    }

    // clear
    adapter.clear()

    // add items
    val cursor = sqliteManager.readableDatabase.rawQuery("select * from RecordList where recorddate >= datetime(date('$date','localtime')) and recorddate < datetime(date('$date', 'localtime', '+1 day'))", null)
    if (cursor != null && cursor.count > 0 && cursor.moveToLast()) {
      do {
        val drinkAmount = cursor.getInt(2)
        val mainicon: Int
        val datetime = cursor.getString(1).split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        mainicon = CapacityDrawable.getLayout(drinkAmount)

        // add listItem
        val drink = Drink(cursor.getInt(0), Integer.toString(drinkAmount) + "ml", datetime[0] + ":" + datetime[1], ContextCompat.getDrawable(context!!, mainicon)!!)
        adapter.addDrinkItem(drink)
      } while (cursor.moveToPrevious())
      cursor.close()
    }

    // if no cursor exist
    val tv_message = rootView!!.findViewById(R.id.dailyrecord_tv_message) as TextView
    if (cursor!!.count == 0)
      tv_message.visibility = View.VISIBLE
    else
      tv_message.visibility = View.INVISIBLE
  }
}
