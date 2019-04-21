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

package com.skydoves.waterdays.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * Developed by skydoves on 2017-08-18.
 * Copyright (c) 2017 skydoves rights reserved.
 */

object NetworkUtils {
  fun isNetworkAvailable(context: Context): Boolean {
    var status = false
    try {
      val cm = context
          .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
      var netInfo: NetworkInfo? = cm.activeNetworkInfo

      if (netInfo != null && netInfo.state == NetworkInfo.State.CONNECTED) {
        status = true
      } else {
        netInfo = cm.activeNetworkInfo
        if (netInfo != null && netInfo.state == NetworkInfo.State.CONNECTED)
          status = true
      }
    } catch (e: Exception) {
      e.printStackTrace()
      return false
    }

    return status
  }
}
