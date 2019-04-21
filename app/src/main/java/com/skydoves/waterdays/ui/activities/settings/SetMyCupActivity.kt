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

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.skydoves.waterdays.R
import com.skydoves.waterdays.persistence.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_set_my_cup.*

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class SetMyCupActivity : AppCompatActivity() {

  private lateinit var preferenceManager: PreferenceManager

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_set_my_cup)

    preferenceManager = PreferenceManager(this)
    setmycup_edt_mycup.setText(preferenceManager.getString("MyCup", "250"))

    setmycup_btn_setcup.setOnClickListener {
      if (setmycup_edt_mycup.text.toString() != "") {
        preferenceManager.putString("MyCup", setmycup_edt_mycup.text.toString())
        Toast.makeText(this, "내 컵의 용량이 설정되었습니다.", Toast.LENGTH_SHORT).show()
        finish()
      } else
        Toast.makeText(this, "올바른 용량을 입력해주세요.", Toast.LENGTH_SHORT).show()
    }
  }
}
