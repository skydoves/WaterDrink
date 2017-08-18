package com.skydoves.waterdays.ui.activities.main;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.skydoves.waterdays.R;
import com.skydoves.waterdays.persistence.preference.PreferenceManager;
import com.skydoves.waterdays.utils.AlarmUtils;
import com.skydoves.waterdays.persistence.sqlite.SqliteManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class MakeAlarmActivity extends AppCompatActivity {

    // Systems
    private TimePickerDialog timePickerDialog;
    private int selectedType = 0;
    private boolean[] dayChecked;
    AlarmUtils systems_alarm;

    private PreferenceManager systems;
    private SqliteManager sqliteManager;

    // get Views
    @BindView(R.id.makealram_content1_tv_starttime)
    TextView tv_StartLabel;
    @BindView(R.id.makealram_content1_tv_endtime)
    TextView tv_EndLabel;
    @BindView(R.id.makealram_np_interval)
    NumberPicker numberPicker_interval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_alarm);
        ButterKnife.bind(this);

        // Initialize
        Initialize();
    }

    // Initialize
    private void Initialize()
    {
        // TODO skydoves - MakeAlarm Init : Auto-generated method stub
        // Initialize TimePicker
        timePickerDialog = new TimePickerDialog(this,android.R.style.Theme_Holo_Dialog, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Selected Type
                switch (selectedType) {
                    case 0 :
                        TextView tv_StartLabel = (TextView)findViewById(R.id.makealram_content1_tv_starttime);
                        tv_StartLabel.setText(hourOfDay + "시 " + minute + "분");
                        break;

                    case 1 :
                        TextView tv_EndLabel = (TextView)findViewById(R.id.makealram_content1_tv_endtime);
                        tv_EndLabel.setText(hourOfDay + "시 " + minute + "분");
                        break;
                }
            }
        },12,0,true);
        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Set NumberPicker
        numberPicker_interval.setMinValue(1);
        numberPicker_interval.setMaxValue(5);
        numberPicker_interval.setValue(1);
        numberPicker_interval.setWrapSelectorWheel(false);
        numberPicker_interval.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        // Initialize Systems & DB
        systems = new PreferenceManager(this);
        systems_alarm = new AlarmUtils(this);
        sqliteManager = new SqliteManager(this, SqliteManager.DATABASE_NAME, null, SqliteManager.DATABASE_VERSION);

        // Initialize variables
        dayChecked = new boolean[7];
        for(int i=0; i<7; i++) dayChecked[i] = false;
    }

    // Click Label : Select Days
    @OnClick({R.id.makealarm_tv_day0, R.id.makealarm_tv_day1, R.id.makealarm_tv_day2, R.id.makealarm_tv_day3, R.id.makealarm_tv_day4, R.id.makealarm_tv_day5, R.id.makealarm_tv_day6})
    void btn_selectday(View v)
    {
        boolean checkval = false;
        TextView tv_dayItem = (TextView)v;
        if(tv_dayItem.getAlpha() == 1) {
            tv_dayItem.setAlpha(0.5f);
            checkval = true;
        }
        else  tv_dayItem.setAlpha(1);

        // set day-Checked value
        if(v.getId() == R.id.makealarm_tv_day0)
            dayChecked[0] = checkval;
        else if(v.getId() == R.id.makealarm_tv_day1)
            dayChecked[1] = checkval;
        else if(v.getId() == R.id.makealarm_tv_day2)
            dayChecked[2] = checkval;
        else if(v.getId() == R.id.makealarm_tv_day3)
            dayChecked[3] = checkval;
        else if(v.getId() == R.id.makealarm_tv_day4)
            dayChecked[4] = checkval;
        else if(v.getId() == R.id.makealarm_tv_day5)
            dayChecked[5] = checkval;
        else if(v.getId() == R.id.makealarm_tv_day6)
            dayChecked[6] = checkval;
    }

    // Click Label : Set Start & End Time
    @OnClick({R.id.makealarm_label_starttime, R.id.makealarm_label_endtime})
    void label_setTime(View v)
    {
        if(v.getScaleX() == 1) {
            ViewCompat.animate(v)
                    .setDuration(200)
                    .scaleX(0.9f)
                    .scaleY(0.9f)
                    .setInterpolator(new CycleInterpolator(0.5f))
                    .setListener(new ViewPropertyAnimatorListener() {

                        @Override
                        public void onAnimationStart(final View view) {
                        }

                        @Override
                        public void onAnimationEnd(final View v) {
                            switch (v.getId()) {
                                case R.id.makealarm_label_starttime:
                                    selectedType = 0;
                                    timePickerDialog.show();
                                    break;

                                case R.id.makealarm_label_endtime:
                                    selectedType = 1;
                                    timePickerDialog.show();
                                    break;
                            }
                        }

                        @Override
                        public void onAnimationCancel(final View view) {
                        }
                    })
                    .withLayer()
                    .start();
        }
    }

    // Click Image Button : Question Mark
    @OnClick({R.id.makealram_ibtn_q_timeset01, R.id.makealram_ibtn_q_timeset02})
    void ibtn_questionMark(View v)
    {
        View btnview = findViewById(R.id.makealram_ibtn_q_timeset01);
        switch (v.getId()){
            case R.id.makealram_ibtn_q_timeset01 :
                Snackbar.make(btnview, "시작 시간과 종료 시간 사이에만 알림을 받습니다.", Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show();
                break;

            case R.id.makealram_ibtn_q_timeset02 :
                Snackbar.make(btnview, "시작 시간과 종료 시간 사이에 특정 간격으로 알림을 받습니다.", Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show();
                break;
        }
    }

    // Click Button : Set Alarm
    @OnClick(R.id.makealram_btn_setalarm)
    void btn_setAlarm(View v)
    {
        // TODO skydoves - Make Alarm with Handle requestcode
        // Check Start-End Time
        if(tv_StartLabel.getText().toString().contains("분") && tv_EndLabel.getText().toString().contains("분")) {
            String[] sTime = tv_StartLabel.getText().toString().split("시");
            String[] eTime = tv_EndLabel.getText().toString().split("시");
            if(Integer.parseInt(sTime[0]) <= Integer.parseInt(eTime[0])) {
                try {
                    int requestcode = systems.getInt("requestcode", 0) + 1;

                    // get daylist
                    String daylist = "";
                    for (int i = 0; i < 7; i++) {
                        if (dayChecked[i])
                            daylist += i + ",";
                    }

                    // Check day valid
                    if (daylist.equals("")) {
                        Toast.makeText(this, "최소 1개 이상의 요일을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Add Alarm Record at List
                    sqliteManager.addAlarm(requestcode, daylist, tv_StartLabel.getText().toString(), tv_EndLabel.getText().toString(), numberPicker_interval.getValue());

                    // Set AlarmManager
                    systems_alarm.setAlarm(requestcode);

                    // Set requestcode
                    systems.putInt("requestcode", requestcode);

                    Toast.makeText(this, "새 알림이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    ((MainActivity) MainActivity.mContext).UpdateFragments();
                    finish();
                } catch (Exception e) {

                }
            }
            else
                Toast.makeText(this, "종료시간은 시작시간 보다 커야합니다.", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "시작시간과 종료시간을 설정해 주세요.", Toast.LENGTH_SHORT).show();
    }
}
