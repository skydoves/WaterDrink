package com.skydoves.waterdays.utils

import android.os.Bundle

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

object BundleUtils {
  fun maybeGetBundle(state: Bundle?, key: String): Bundle? {
    state.let { return state?.getBundle(key) }
  }
}
