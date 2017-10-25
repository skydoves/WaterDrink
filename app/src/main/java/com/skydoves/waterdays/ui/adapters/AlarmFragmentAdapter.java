package com.skydoves.waterdays.ui.adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;

import com.skydoves.waterdays.R;
import com.skydoves.waterdays.models.Alarm;
import com.skydoves.waterdays.ui.viewholders.AlarmViewHolder;
import com.skydoves.waterdays.ui.viewholders.BaseViewHolder;

import java.util.ArrayList;

/**
 * Developed by skydoves on 2017-08-20.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class AlarmFragmentAdapter extends BaseAdapter {
    private AlarmViewHolder.Delegate delegate;

    public AlarmFragmentAdapter(AlarmViewHolder.Delegate delegate) {
        this.delegate = delegate;
        addSection(new ArrayList<Alarm>());
        notifyDataSetChanged();
    }

    public void addAlarmItem(Alarm alarmModel) {
        sections().get(0).add(alarmModel);
        notifyDataSetChanged();
    }

    @Override
    protected int layout(@NonNull BaseAdapter.SectionRow sectionRow) {
        return R.layout.item_alarmrecord;
    }

    @NonNull
    @Override
    protected BaseViewHolder viewHolder(@LayoutRes int layout, @NonNull View view) {
        return new AlarmViewHolder(view, delegate);
    }
}
