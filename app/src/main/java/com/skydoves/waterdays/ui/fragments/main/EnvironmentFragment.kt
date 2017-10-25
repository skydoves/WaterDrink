package com.skydoves.waterdays.ui.fragments.main

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.skydoves.ElasticAction
import com.skydoves.waterdays.R
import com.skydoves.waterdays.ui.activities.settings.*
import kotlinx.android.synthetic.main.layout_settings.*
import org.jetbrains.anko.support.v4.startActivity

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class EnvironmentFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.layout_settings, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        settings_tv_nfc.setOnClickListener { MenuClick(it) }
        settings_tv_recommend.setOnClickListener { MenuClick(it) }
        settings_tv_goal.setOnClickListener { MenuClick(it) }
        settings_tv_mycup.setOnClickListener { MenuClick(it) }
        settings_tv_setlocation.setOnClickListener { MenuClick(it) }
        settings_tv_setColor.setOnClickListener { MenuClick(it) }
        settings_tv_setting.setOnClickListener { MenuClick(it) }
    }

    internal fun MenuClick(v: View) {
        val duration = 200
        ElasticAction.doAction(v, duration, 0.9f, 0.9f)
        Handler().postDelayed({
            when (v.id) {
                R.id.settings_tv_nfc -> startActivity<NFCActivity>()

                R.id.settings_tv_recommend -> startActivity<SetWeightActivity>()

                R.id.settings_tv_goal ->  startActivity<SetGoalActivity>()

                R.id.settings_tv_mycup ->  startActivity<SetMyCupActivity>()

                R.id.settings_tv_setlocation ->  startActivity<SetLocalActivity>()

                R.id.settings_tv_setColor -> startActivity<SetBubbleColorActivity>()

                R.id.settings_tv_setting -> startActivity<SettingActivity>()
            }
        }, duration.toLong())
    }
}