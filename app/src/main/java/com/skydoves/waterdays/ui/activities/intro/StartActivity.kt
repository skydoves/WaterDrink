package com.skydoves.waterdays.ui.activities.intro

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment

import com.github.paolorotolo.appintro.AppIntro
import com.skydoves.waterdays.R
import com.skydoves.waterdays.WDApplication
import com.skydoves.waterdays.compose.BaseView
import com.skydoves.waterdays.models.Capacity
import com.skydoves.waterdays.persistence.preference.PreferenceKeys
import com.skydoves.waterdays.persistence.preference.PreferenceManager
import com.skydoves.waterdays.persistence.sqlite.SqliteManager
import com.skydoves.waterdays.ui.activities.main.MainActivity
import com.skydoves.waterdays.ui.activities.settings.SetWeightActivity
import com.skydoves.waterdays.ui.fragments.intro.SlideFragment

import javax.inject.Inject

/**
 * Developed by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class StartActivity : AppIntro(), BaseView {

    @Inject lateinit var preferenceManager: PreferenceManager
    @Inject lateinit var sqliteManager: SqliteManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WDApplication.component.inject(this)

        if (!preferenceManager.getBoolean(PreferenceKeys.NEWBE.first, PreferenceKeys.NEWBE.second)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        if (!preferenceManager.getBoolean(PreferenceKeys.INIT_CAPACITY.first, PreferenceKeys.INIT_CAPACITY.second)) {
            sqliteManager.addCapacity(Capacity(null, 125))
            sqliteManager.addCapacity(Capacity(null, 250))
            sqliteManager.addCapacity(Capacity(null, 350))
            sqliteManager.addCapacity(Capacity(null, 500))
            sqliteManager.addCapacity(Capacity(null, 750))
            sqliteManager.addCapacity(Capacity(null, 1000))
            preferenceManager.putBoolean(PreferenceKeys.INIT_CAPACITY.first, true)
        }

        initializeUI()
    }

    override fun initializeUI() {
        addSlide(SlideFragment.newInstance(R.layout.intro1))
        addSlide(SlideFragment.newInstance(R.layout.intro2))
        addSlide(SlideFragment.newInstance(R.layout.intro3))
        addSlide(SlideFragment.newInstance(R.layout.intro4))
        setDoneText(getString(R.string.start))
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        ActivityStart()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        ActivityStart()
    }

    override fun onSlideChanged(oldFragment: Fragment?, newFragment: Fragment?) {
        super.onSlideChanged(oldFragment, newFragment)
    }

    private fun ActivityStart() {
        startActivity(Intent(this, SetWeightActivity::class.java))
        finish()
    }
}