package com.skydoves.waterdays.ui.activities.alarm

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.jakewharton.rxbinding2.view.RxView
import com.skydoves.waterdays.R
import com.skydoves.waterdays.compose.BaseActivity
import com.skydoves.waterdays.compose.qualifiers.RequirePresenter
import com.skydoves.waterdays.presenters.AlarmScreenPresenter
import com.skydoves.waterdays.utils.FillableLoaderPaths
import com.skydoves.waterdays.viewTypes.AlarmScreenActivityView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_alarmscreen.*
import java.util.concurrent.TimeUnit

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@RequirePresenter(AlarmScreenPresenter::class)
class AlarmScreenActivity : BaseActivity<AlarmScreenPresenter, AlarmScreenActivityView>(), AlarmScreenActivityView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarmscreen)
        initBaseView(this)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)

        Observable.just("")
                .delay(showMinutes.toLong(), TimeUnit.MINUTES)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { delay -> window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) }

        RxView.clicks(findViewById(R.id.alarmscreen_btn_check))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { e ->
                    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.cancel(1004)
                    presenter.onCheck()
                    findViewById(R.id.alarmscreen_btn_check).isEnabled = false
                }
    }

    override fun initBaseView(alarmScreenView: AlarmScreenActivityView) {
        super.initBaseView(alarmScreenView)
    }

    override fun initializeUI() {
        val amount = presenter.daliyDrink
        val goal = Integer.parseInt(presenter.waterGoal).toFloat()

        if (amount / goal * 100 < 100) {
            alarmscreen_percentage.text = (amount / goal * 100).toInt().toString() + "%"
        } else {
            alarmscreen_percentage.text = "100%"
        }

        alarmscreen_fillableLoader.setSvgPath(FillableLoaderPaths.SVG_WATERDROP)
        alarmscreen_fillableLoader.start()
        alarmscreen_fillableLoader.setPercentage(amount / goal * 100)
    }

    override fun onDrink(value: String) {
        val amount = presenter.daliyDrink
        val goal = Integer.parseInt(presenter.waterGoal)
        alarmscreen_fillableLoader.setPercentage(amount / goal * 100)
        alarmscreen_fillableLoader.reset()
        alarmscreen_fillableLoader.start()
        if (amount / goal * 100 < 100) {
            alarmscreen_percentage.text = ((amount + presenter.cupSize.toInt()) /  goal * 100).toInt().toString() + "%"
        } else {
            alarmscreen_percentage.text = "100%"
        }
        Toast.makeText(this, value + "ml 만큼 섭취했습니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onFinish() {
        Observable.just("")
                .delay(2700, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { finish() }
    }

    companion object {
        private val showMinutes = 3
    }
}