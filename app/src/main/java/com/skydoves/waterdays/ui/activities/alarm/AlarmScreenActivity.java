package com.skydoves.waterdays.ui.activities.alarm;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jorgecastillo.FillableLoader;
import com.skydoves.waterdays.R;
import com.skydoves.waterdays.persistence.preference.PreferenceManager;
import com.skydoves.waterdays.persistence.sqlite.SqliteManager;
import com.skydoves.waterdays.utils.DateUtils;
import com.skydoves.waterdays.utils.FillableLoaderPaths;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class AlarmScreenActivity extends AppCompatActivity {

    protected @BindView(R.id.alarmscreen_fillableLoader) FillableLoader fillableLoader;

    private SqliteManager sqliteManager;
    private PreferenceManager preferenceManager;

    private static final int showMinutes = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmscreen);
        ButterKnife.bind(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        preferenceManager = new PreferenceManager(this);
        sqliteManager = new SqliteManager(this, SqliteManager.DATABASE_NAME, null, SqliteManager.DATABASE_VERSION);

        Initialize();

        Observable.timer(showMinutes, TimeUnit.MINUTES)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(delay -> getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON));
    }

    private void Initialize() {
        try {
            // get Today Drink Amount
            float dAmount = sqliteManager.getDayDrinkAmount(DateUtils.getFarDay(0));
            float dGoal = Integer.parseInt(preferenceManager.getString("WaterGoal", "0"));

            // textView - today drunk percentage
            TextView tv_percentage = (TextView) findViewById(R.id.alarmscreen_percentage);
            if ((dAmount / dGoal) * 100 < 100)
                tv_percentage.setText((int) ((dAmount / dGoal) * 100) + "%");
            else
                tv_percentage.setText("100%");

            fillableLoader.setSvgPath(FillableLoaderPaths.SVG_WATERDROP);
            fillableLoader.start();
            fillableLoader.setPercentage((dAmount / dGoal) * 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.alarmscreen_btn_check)
    public void Click_btn_Check(View v){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1004);

        // add a record
        String myCup = preferenceManager.getString("MyCup", "0");
        if(!myCup.equals("0")) {
            sqliteManager.addRecord(myCup);
            Toast.makeText(this, myCup + "ml 만큼 섭취했습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
        finish();
    }
}