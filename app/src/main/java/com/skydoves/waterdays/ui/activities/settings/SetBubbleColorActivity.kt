package com.skydoves.waterdays.ui.activities.settings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.jakewharton.rxbinding2.view.RxView
import com.skydoves.waterdays.R
import com.skydoves.waterdays.WDApplication
import com.skydoves.waterdays.persistence.preference.PreferenceKeys
import com.skydoves.waterdays.persistence.preference.PreferenceManager
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_set_bubble_color.*
import javax.inject.Inject

class SetBubbleColorActivity : AppCompatActivity() {

    @Inject protected lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_bubble_color)
        WDApplication.component.inject(this)

        colorPickerView!!.setColorListener { color -> bubble.setColorFilter(color) }
        RxView.clicks(findViewById(R.id.confirm))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    preferenceManager.putString(PreferenceKeys.BUBBLE_COLOR.first, "#" + colorPickerView!!.colorHtml)
                    Toast.makeText(baseContext, "물방울의 색상을 변경하였습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }
    }
}
