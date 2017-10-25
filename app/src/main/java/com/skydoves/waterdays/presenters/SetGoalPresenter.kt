package com.skydoves.waterdays.presenters

import android.content.Context
import android.os.Bundle

import com.skydoves.waterdays.WDApplication
import com.skydoves.waterdays.compose.BasePresenter
import com.skydoves.waterdays.persistence.preference.PreferenceKeys
import com.skydoves.waterdays.persistence.preference.PreferenceManager
import com.skydoves.waterdays.viewTypes.SetGoalActivityView

import javax.inject.Inject

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class SetGoalPresenter : BasePresenter<SetGoalActivityView>() {

    @Inject lateinit var preferenceManager: PreferenceManager

    override fun onCreate(context: Context, savedInstanceState: Bundle?) {
        super.onCreate(context, savedInstanceState)
        WDApplication.component.inject(this)
    }

    fun onClickSetGoal(value: String) {
        if (checkGoalValid(value)) {
            waterGoal = value
            baseView.onSetSuccess()
            if (isNewbe) baseView.intentMain()
        } else
            baseView.onSetFailure()
    }

    fun checkGoalValid(value: String): Boolean {
        when(value) {
            "" -> return false
            "0" -> return false
        }
        return true
    }

    val isNewbe: Boolean by lazy {
        preferenceManager.getBoolean(PreferenceKeys.NEWBE.first, PreferenceKeys.NEWBE.second)
    }

    fun setOldbe() = preferenceManager.putBoolean(PreferenceKeys.NEWBE.first, false)

    var waterGoal: String
        get() = preferenceManager.getString(PreferenceKeys.WATER_GOAL.first, PreferenceKeys.WATER_GOAL.second)
        set(goal) = preferenceManager.putString(PreferenceKeys.WATER_GOAL.first, goal)
}