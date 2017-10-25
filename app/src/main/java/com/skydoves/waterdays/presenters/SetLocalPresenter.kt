package com.skydoves.waterdays.presenters

import android.content.Context
import android.os.Bundle

import com.skydoves.waterdays.WDApplication
import com.skydoves.waterdays.compose.BasePresenter
import com.skydoves.waterdays.persistence.preference.PreferenceKeys
import com.skydoves.waterdays.persistence.preference.PreferenceManager
import com.skydoves.waterdays.viewTypes.SetLocalActivityView

import javax.inject.Inject

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class SetLocalPresenter : BasePresenter<SetLocalActivityView>() {

    @Inject lateinit var preferenceManager: PreferenceManager

    override fun onCreate(context: Context, savedInstanceState: Bundle?) {
        super.onCreate(context, savedInstanceState)
        WDApplication.component.inject(this)
    }

    var localIndex: Int
        get() = preferenceManager!!.getInt(PreferenceKeys.LOCALINDEX.first, PreferenceKeys.LOCALINDEX.second)
        set(value) = preferenceManager!!.putInt(PreferenceKeys.LOCALINDEX.first, value)
}
