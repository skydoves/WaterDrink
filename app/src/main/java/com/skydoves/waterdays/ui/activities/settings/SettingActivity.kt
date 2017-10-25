package com.skydoves.waterdays.ui.activities.settings

import android.os.Bundle
import android.preference.PreferenceActivity

import com.skydoves.waterdays.R

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class SettingActivity : PreferenceActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings)
    }
}