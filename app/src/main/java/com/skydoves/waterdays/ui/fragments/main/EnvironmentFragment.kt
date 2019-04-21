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

package com.skydoves.waterdays.ui.fragments.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.skydoves.elasticviews.ElasticAnimation
import com.skydoves.elasticviews.ElasticFinishListener
import com.skydoves.waterdays.R
import com.skydoves.waterdays.ui.activities.settings.NFCActivity
import com.skydoves.waterdays.ui.activities.settings.SetBubbleColorActivity
import com.skydoves.waterdays.ui.activities.settings.SetGoalActivity
import com.skydoves.waterdays.ui.activities.settings.SetLocalActivity
import com.skydoves.waterdays.ui.activities.settings.SetMyCupActivity
import com.skydoves.waterdays.ui.activities.settings.SetWeightActivity
import com.skydoves.waterdays.ui.activities.settings.SettingActivity
import kotlinx.android.synthetic.main.layout_settings.*

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class EnvironmentFragment : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val rootView = inflater.inflate(R.layout.layout_settings, container, false)
    return rootView
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    settings_tv_nfc.setOnClickListener { menuClick(it) }
    settings_tv_recommend.setOnClickListener { menuClick(it) }
    settings_tv_goal.setOnClickListener { menuClick(it) }
    settings_tv_mycup.setOnClickListener { menuClick(it) }
    settings_tv_setlocation.setOnClickListener { menuClick(it) }
    settings_tv_setColor.setOnClickListener { menuClick(it) }
    settings_tv_setting.setOnClickListener { menuClick(it) }
  }

  private fun menuClick(view: View) {
    ElasticAnimation(view).setScaleX(0.9f).setScaleY(0.9f).setDuration(200).setOnFinishListener(object : ElasticFinishListener {
      override fun onFinished() {
        when (view.id) {
          R.id.settings_tv_nfc -> startActivity(Intent(context, NFCActivity::class.java))
          R.id.settings_tv_recommend -> startActivity(Intent(context, SetWeightActivity::class.java))
          R.id.settings_tv_goal -> startActivity(Intent(context, SetGoalActivity::class.java))
          R.id.settings_tv_mycup -> startActivity(Intent(context, SetMyCupActivity::class.java))
          R.id.settings_tv_setlocation -> startActivity(Intent(context, SetLocalActivity::class.java))
          R.id.settings_tv_setColor -> startActivity(Intent(context, SetBubbleColorActivity::class.java))
          R.id.settings_tv_setting -> startActivity(Intent(context, SettingActivity::class.java))
        }
      }
    }).doAction()
  }
}
