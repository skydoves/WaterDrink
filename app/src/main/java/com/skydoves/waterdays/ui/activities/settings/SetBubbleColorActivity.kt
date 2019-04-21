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

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding2.view.RxView
import com.skydoves.colorpickerview.listeners.ColorListener
import com.skydoves.waterdays.R
import com.skydoves.waterdays.WDApplication
import com.skydoves.waterdays.persistence.preference.PreferenceKeys
import com.skydoves.waterdays.persistence.preference.PreferenceManager
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_set_bubble_color.*
import javax.inject.Inject

open class SetBubbleColorActivity : AppCompatActivity() {

  @Inject
  protected lateinit var preferenceManager: PreferenceManager

  @SuppressLint("CheckResult")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_set_bubble_color)
    WDApplication.component.inject(this)

    colorPickerView.setColorListener(object : ColorListener {
      override fun onColorSelected(color: Int, fromUser: Boolean) {
        bubble.setColorFilter(color)
      }
    })

    RxView.clicks(findViewById(R.id.confirm))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          preferenceManager.putString(PreferenceKeys.BUBBLE_COLOR.first, "#" + colorPickerView.colorEnvelope.hexCode)
          Toast.makeText(baseContext, "물방울의 색상을 변경하였습니다.", Toast.LENGTH_SHORT).show()
          finish()
        }
  }
}
