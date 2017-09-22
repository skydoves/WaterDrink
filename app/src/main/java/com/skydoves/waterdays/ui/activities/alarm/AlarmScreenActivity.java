package com.skydoves.waterdays.ui.activities.alarm;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jorgecastillo.FillableLoader;
import com.skydoves.waterdays.R;
import com.skydoves.waterdays.compose.BaseActivity;
import com.skydoves.waterdays.compose.qualifiers.RequirePresenter;
import com.skydoves.waterdays.presenters.AlarmScreenPresenter;
import com.skydoves.waterdays.utils.FillableLoaderPaths;
import com.skydoves.waterdays.viewTypes.AlarmScreenActivityView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@RequirePresenter(AlarmScreenPresenter.class)
public class AlarmScreenActivity extends BaseActivity<AlarmScreenPresenter, AlarmScreenActivityView> implements AlarmScreenActivityView {

    protected @BindView(R.id.alarmscreen_fillableLoader) FillableLoader fillableLoader;
    protected @BindView(R.id.alarmscreen_percentage) TextView tv_percentage;

    private static final int showMinutes = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmscreen);
        initBaseView(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        Observable.just("")
                .delay(showMinutes, TimeUnit.MINUTES)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(delay -> getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON));
    }

    @Override
    protected void initBaseView(AlarmScreenActivityView alarmScreenView) {
        super.initBaseView(alarmScreenView);
    }

    @Override
    public void initializeUI() {
        float amount = presenter.getDaliyDrink();
        float goal = Integer.parseInt(presenter.getWaterGoal());

        if ((amount / goal) * 100 < 100) {
            tv_percentage.setText((int) ((amount / goal) * 100) + "%");
        } else {
            tv_percentage.setText("100%");
        }

        fillableLoader.setSvgPath(FillableLoaderPaths.SVG_WATERDROP);
        fillableLoader.start();
        fillableLoader.setPercentage((amount / goal) * 100);
    }

    @Override
    public void onDrink(String value) {
        float amount = presenter.getDaliyDrink();
        float goal = Integer.parseInt(presenter.getWaterGoal());
        fillableLoader.setPercentage((amount / goal) * 100);
        fillableLoader.reset();
        fillableLoader.start();
        if ((amount / goal) * 100 < 100) {
            tv_percentage.setText((int) ((amount / goal) * 100) + "%");
        } else {
            tv_percentage.setText("100%");
        }
        Toast.makeText(this, value + "ml 만큼 섭취했습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinish() {
        Observable.just("")
                .delay(2700, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(delay -> finish());
    }

    @OnClick(R.id.alarmscreen_btn_check)
    public void onCheck(View v){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1004);
        presenter.onCheck();
        v.setEnabled(false);
    }
}