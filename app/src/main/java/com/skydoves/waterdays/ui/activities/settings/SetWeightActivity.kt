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
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.skydoves.waterdays.R
import com.skydoves.waterdays.persistence.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_setweight.*

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class SetWeightActivity : AppCompatActivity() {

  private var preferenceManager: PreferenceManager? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_setweight)

    preferenceManager = PreferenceManager(this)
    setweight_edt_weight.setText(preferenceManager!!.getInt("userWeight", 60).toString())

    setweight_btn_getrecommend.setOnClickListener { clickBtn(it) }
    setweight_btn_setweight.setOnClickListener { clickBtn(it) }
  }

  private fun clickBtn(v: View) {
    if (setweight_edt_weight.text.toString() != "") {
      when (v.id) {
        R.id.setweight_btn_getrecommend -> {
          val Reh = preferenceManager!!.getString("Reh", "60")
          Snackbar.make(setweight_edt_weight, "추천 수분 섭취량은 " + (Integer.parseInt(setweight_edt_weight.getText().toString()) * 30 + (500 - Integer.parseInt(Reh) * 3)) + "ml 입니다.",
              Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show()
        }

        R.id.setweight_btn_setweight -> {
          if (preferenceManager!!.getString("WaterGoal", "null") == "null") {
            val intent = Intent(this, SetGoalActivity::class.java)
            startActivity(intent)
          }
          Toast.makeText(this, "체중을 설정하였습니다.", Toast.LENGTH_SHORT).show()
          preferenceManager!!.putInt("userWeight", Integer.parseInt(setweight_edt_weight.getText().toString()))
          finish()
        }
      }
    } else
      Toast.makeText(this, "올바른 체중값을 입력해 주세요!", Toast.LENGTH_SHORT).show()
  }
}
