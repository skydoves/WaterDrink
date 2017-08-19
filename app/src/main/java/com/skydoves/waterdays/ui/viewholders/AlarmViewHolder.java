package com.skydoves.waterdays.ui.viewholders;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skydoves.waterdays.R;
import com.skydoves.waterdays.models.AlarmModel;

import butterknife.BindView;

/**
 * Developed by skydoves on 2017-08-20.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class AlarmViewHolder extends BaseViewHolder {

    protected @BindView(R.id.item_alarmrecord_tv_days) TextView tv_item_days;
    protected @BindView(R.id.item_alarmrecord_tv_times) TextView tv_item_times;
    protected @BindView(R.id.item_alarmrecord_tv_interval) TextView tv_item_interval;
    protected @BindView(R.id.item_alarmrecord_btn_delete) ImageButton ibtn_item_delete;

    private AlarmModel alarmModel;

    private Delegate delegate;

    public interface Delegate {
        void onConfirm(AlarmModel alarmModel);
    }

    public AlarmViewHolder(@NonNull View view, Delegate delegate) {
        super(view);
        this.delegate = delegate;
    }

    @Override
    public void bindData(@NonNull Object data) throws Exception {
        this.alarmModel = (AlarmModel) data;

        tv_item_days.setText(alarmModel.getDays());
        tv_item_times.setText(alarmModel.getTimes());
        tv_item_interval.setText(alarmModel.getInterval());
        ibtn_item_delete.setOnClickListener(view -> {
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
            alertDlg.setTitle("알람");
            alertDlg.setPositiveButton("예", (DialogInterface dialog, int which) -> delegate.onConfirm(alarmModel));
            alertDlg.setNegativeButton("아니오", ((dialog, which) -> dialog.dismiss()));
            alertDlg.setMessage(String.format("해당 알람을 지우시겠습니까?"));
            alertDlg.show();
        });
    }

    @Override
    public void onClick(View v) {
    }
}
