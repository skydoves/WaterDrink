package com.skydoves.waterdays.models

import android.graphics.drawable.Drawable

/**
 * Developed by skydoves on 2017-09-21.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class Capacity {
  val image: Drawable?
  var amount: Int = 0
    private set

  constructor(amount: Int) {
    image = null
    this.amount = amount
  }

  constructor(image: Drawable?, amount: Int) {
    this.image = image
    this.amount = amount
  }
}
