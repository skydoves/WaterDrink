package com.skydoves.waterdays.ui.customViews

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.EditText

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class NumberPicker(context: Context, attrs: AttributeSet) : android.widget.NumberPicker(context, attrs) {

    override fun addView(child: View) {
        super.addView(child)
        updateView(child)
    }

    override fun addView(child: View, index: Int, params: android.view.ViewGroup.LayoutParams) {
        super.addView(child, index, params)
        updateView(child)
    }

    override fun addView(child: View, params: android.view.ViewGroup.LayoutParams) {
        super.addView(child, params)
        updateView(child)
    }

    private fun updateView(view: View) {
        if (view is EditText) {
            view.textSize = 18f
            view.setTextColor(Color.parseColor("#ffffff"))
        }
    }
}