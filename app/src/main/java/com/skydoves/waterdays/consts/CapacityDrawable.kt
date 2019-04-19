package com.skydoves.waterdays.consts

import com.skydoves.waterdays.R

/**
 * Developed by skydoves on 2017-09-22.
 * Copyright (c) 2017 skydoves rights reserved.
 */

object CapacityDrawable {
  fun getLayout(amount: Int): Int {
    if (amount <= 125)
      return R.drawable.ic_glass0;
    else if (amount <= 250)
      return R.drawable.ic_glass01;
    else if (amount <= 350)
      return R.drawable.ic_glass06;
    else if (amount <= 500)
      return R.drawable.ic_glass05;
    else if (amount <= 750)
      return R.drawable.ic_glass07;
    else
      return R.drawable.ic_glass04;
  }
}
