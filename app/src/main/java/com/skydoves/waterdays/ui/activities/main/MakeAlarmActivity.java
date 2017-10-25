package com.skydoves.waterdays.ui.activities.main;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.skydoves.ElasticAction;
import com.github.skydoves.ElasticCheckButton;
import com.skydoves.waterdays.R;
import com.skydoves.waterdays.compose.BaseActivity;
import com.skydoves.waterdays.compose.qualifiers.RequirePresenter;
import com.skydoves.waterdays.events.rx.RxUpdateMainEvent;
import com.skydoves.waterdays.presenters.MakeAlarmPresenter;
import com.skydoves.waterdays.utils.AlarmUtils;
import com.skydoves.waterdays.viewTypes.MakeAlarmActivityView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@RequirePresenter(MakeAlarmPresenter.class)
public class MakeAlarmActivity extends BaseActivity<MakeAlarmPresenter, MakeAlarmActivityView> implements MakeAlarmActivityView {

    protected @BindView(R.id.makealram_content1_tv_starttime) TextView tv_StartLabel;
    protected @BindView(R.id.makealram_content1_tv_endtime) TextView tv_EndLabel;
    protected @BindView(R.id.makealram_np_interval) NumberPicker numberPicker;

    private AlarmUtils alarmUtils;
    private TimePickerDialog timePickerDialog;
    private int selectedType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_alarm);
        initBaseView(this);

        alarmUtils = new AlarmUtils(this);
    }

    @Override
    protected void initBaseView(MakeAlarmActivityView makeAlarmActivityView) {
        super.initBaseView(makeAlarmActivityView);
    }

    @Override
    public void initializeUI() {
        timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Dialog, (TimePicker view, int hourOfDay, int minute) -> {
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
        },12,0,true);
        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(5);
        numberPicker.setValue(1);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

    @OnClick({R.id.makealarm_label_starttime, R.id.makealarm_label_endtime})
    void label_setTime(View view) {
        int duration = 200;
        ElasticAction.doAction(view, duration, 0.9f, 0.9f);
        new Handler().postDelayed(() -> {
            switch (view.getId()) {
                case R.id.makealarm_label_starttime:
                    selectedType = 0;
                    timePickerDialog.show();
                    break;

                case R.id.makealarm_label_endtime:
                    selectedType = 1;
                    timePickerDialog.show();
                    break;
            }
        }, duration);
    }

    @OnClick({R.id.makealram_ibtn_q_timeset01, R.id.makealram_ibtn_q_timeset02})
    void ibtn_questionMark(View v) {
        View btnView = findViewById(R.id.makealram_ibtn_q_timeset01);
        switch (v.getId()){
            case R.id.makealram_ibtn_q_timeset01 :
                Snackbar.make(btnView, "시작 시간과 종료 시간 사이에만 알람을 받습니다.", Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show();
                break;

            case R.id.makealram_ibtn_q_timeset02 :
                Snackbar.make(btnView, "시작 시간과 종료 시간 사이에 특정 간격으로 알람을 받습니다.", Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show();
                break;
        }
    }

    @OnClick(R.id.makealram_btn_setalarm)
    void btn_setAlarm(View v) {
        if(tv_StartLabel.getText().toString().contains("분") && tv_EndLabel.getText().toString().contains("분")) {
            String[] sTime = tv_StartLabel.getText().toString().split("시");
            String[] eTime = tv_EndLabel.getText().toString().split("시");
            if(Integer.parseInt(sTime[0]) <= Integer.parseInt(eTime[0])) {
                try {
                    int requestCode = presenter.getPendingRequest() + 1;
                    String dayList = "";
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.panel_days);
                    for (int i = 0; i < linearLayout.getChildCount(); i++) {
                        ElasticCheckButton elasticCheckButton = (ElasticCheckButton) linearLayout.getChildAt(i);
                        if (!elasticCheckButton.isChecked())
                            dayList += i + ",";
                    }

                    if (dayList.equals("")) {
                        Toast.makeText(this, "최소 1개 이상의 요일을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    presenter.addAlarm(requestCode, dayList, tv_StartLabel.getText().toString(), tv_EndLabel.getText().toString(), numberPicker.getValue());
                    alarmUtils.setAlarm(requestCode);
                    presenter.setPendingRequest(requestCode);

                    Toast.makeText(this, "새 알람이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    RxUpdateMainEvent.getInstance().sendEvent();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
                Toast.makeText(this, "종료시간은 시작시간 보다 커야합니다.", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "시작시간과 종료시간을 설정해 주세요.", Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.makealarm_tv_day0, R.id.makealarm_tv_day1, R.id.makealarm_tv_day2, R.id.makealarm_tv_day3, R.id.makealarm_tv_day4,
            R.id.makealarm_tv_day5, R.id.makealarm_tv_day6})
    void onClickDays() {

    }
}