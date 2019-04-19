package com.skydoves.waterdays.compose

import android.content.Context
import android.os.Bundle

import androidx.annotation.CallSuper

import timber.log.Timber

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

open class BasePresenter<ViewType : BaseView> {

  /**
   * get baseView
   *
   * @return baseView
   */
  lateinit var baseView: ViewType

  @CallSuper
  open fun onCreate(context: Context, savedInstanceState: Bundle?) {
    Timber.d("onCreate %s", this.toString())
  }

  @CallSuper
  fun onDestroy() {
    Timber.d("onDestroy %s", this.toString())
  }
}