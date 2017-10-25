package com.skydoves.waterdays.ui.viewholders

import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.view.View
import com.skydoves.waterdays.models.Drink
import kotlinx.android.synthetic.main.item_dailyrecord.view.*

/**
 * Developed by skydoves on 2017-08-20.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class DailyDrinkViewHolder(view: View, private val delegate: Delegate) : BaseViewHolder(view) {

    private lateinit var drink: Drink

    interface Delegate {
        fun onClick(view: View, drink: Drink)
        fun onConfirm(drink: Drink)
    }

    @Throws(Exception::class)
    override fun bindData(data: Any) {
        this.drink = data as Drink

        itemView.item_dailyrecord_tv_todaydate.setText(drink.date)
        itemView.item_dailyrecord_tv_drinkamount.text = drink.amount
        itemView.item_dailyrecord_imv_main.setImageDrawable(drink.image)
        itemView.item_dailyrecord_btn_delete.setOnClickListener {
            val alertDlg = AlertDialog.Builder(it.context)
            alertDlg.setTitle("알람")

            // yes - delete
            alertDlg.setPositiveButton("예") { dialog: DialogInterface, which: Int -> delegate.onConfirm(drink) }

            // no - cancel
            alertDlg.setNegativeButton("아니오") { dialog: DialogInterface, which: Int -> dialog.dismiss() }
            alertDlg.setMessage(String.format("해당 기록을 지우시겠습니까?"))
            alertDlg.show()
        }
    }

    override fun onClick(v: View) {
        delegate.onClick(this.view(), drink)
    }

    override fun onLongClick(v: View): Boolean {
        return false
    }
}