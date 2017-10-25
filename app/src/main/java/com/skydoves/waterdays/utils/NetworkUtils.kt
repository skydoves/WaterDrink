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
