package com.skydoves.waterdays.consts

import com.skydoves.waterdays.R

/**
 * Developed by skydoves on 2017-09-22.
 * Copyright (c) 2017 skydoves rights reserved.
 */

object CapacityDrawable {
  fun getLayout(amount: Int): Int {
    return when {
      amount <= 125 -> R.drawable.ic_glass0
      amount <= 250 -> R.drawable.ic_glass01
      amount <= 350 -> R.drawable.ic_glass06
      amount <= 500 -> R.drawable.ic_glass05
      amount <= 750 -> R.drawable.ic_glass07
      else -> R.drawable.ic_glass04
    }
  }
}
