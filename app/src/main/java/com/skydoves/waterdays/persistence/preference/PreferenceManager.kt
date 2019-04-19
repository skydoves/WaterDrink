package com.skydoves.waterdays.persistence.preference

import android.content.Context

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class PreferenceManager(private val mContext: Context) {

  fun getBoolean(key: String, default_value: Boolean): Boolean {
    val pref = mContext.getSharedPreferences(preferenceKey, 0)
    return pref.getBoolean(key, default_value)
  }

  fun getInt(key: String, default_value: Int): Int {
    val pref = mContext.getSharedPreferences(preferenceKey, 0)
    return pref.getInt(key, default_value)
  }

  fun getString(key: String, default_value: String): String {
    val pref = mContext.getSharedPreferences(preferenceKey, 0)
    return pref.getString(key, default_value)
  }

  fun putBoolean(key: String, default_value: Boolean) {
    val pref = mContext.getSharedPreferences(preferenceKey, 0)
    val editor = pref.edit()
    editor.putBoolean(key, default_value).apply()
  }

  fun putInt(key: String, default_value: Int) {
    val pref = mContext.getSharedPreferences(preferenceKey, 0)
    val editor = pref.edit()
    editor.putInt(key, default_value).apply()
  }

  fun putString(key: String, default_value: String) {
    val pref = mContext.getSharedPreferences(preferenceKey, 0)
    val editor = pref.edit()
    editor.putString(key, default_value).apply()
  }

  companion object {
    private val preferenceKey = "waterdays"
  }
}
