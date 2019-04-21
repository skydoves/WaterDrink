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

package com.skydoves.waterdays.ui.activities.settings

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.skydoves.waterdays.R
import com.skydoves.waterdays.compose.BaseActivity
import com.skydoves.waterdays.compose.qualifiers.RequirePresenter
import com.skydoves.waterdays.presenters.SetGoalPresenter
import com.skydoves.waterdays.ui.activities.main.MainActivity
import com.skydoves.waterdays.viewTypes.SetGoalActivityView
import kotlinx.android.synthetic.main.activity_set_goal.*

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@RequirePresenter(SetGoalPresenter::class)
class SetGoalActivity
  : BaseActivity<SetGoalPresenter, SetGoalActivityView>(), SetGoalActivityView
{

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_set_goal)
    initBaseView(this)
  }

  override fun initializeUI() {
    setgoal_edt_goal.setText(presenter.waterGoal)
    setgoal_btn_setgoal.setOnClickListener { presenter.onClickSetGoal(setgoal_edt_goal.text.toString()) }
  }

  override fun intentMain() {
    val intent = Intent(this, MainActivity::class.java)
    startActivity(intent)
    presenter.setOldbe()
  }

  override fun onSetSuccess() {
    Toast.makeText(this, "목표치를 설정했습니다.", Toast.LENGTH_SHORT).show()
    finish()
  }

  override fun onSetFailure() {
    Toast.makeText(this, "올바른 목표치를 설정해 주세요!", Toast.LENGTH_SHORT).show()
  }
}
