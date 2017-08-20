package com.skydoves.waterdays.ui.fragments.main;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.skydoves.waterdays.R;
import com.skydoves.waterdays.WDApplication;
import com.skydoves.waterdays.events.rx.RxUpdateMainEvent;
import com.skydoves.waterdays.models.Alarm;
import com.skydoves.waterdays.persistence.sqlite.SqliteManager;
import com.skydoves.waterdays.ui.activities.main.MakeAlarmActivity;
import com.skydoves.waterdays.ui.adapters.AlarmFragmentAdapter;
import com.skydoves.waterdays.ui.viewholders.AlarmViewHolder;
import com.skydoves.waterdays.utils.AlarmUtils;
import com.skydoves.waterdays.utils.DateUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-20.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class AlarmFragment extends Fragment {

    protected @BindView(R.id.recyclerView) RecyclerView recyclerView;

    protected @Inject SqliteManager sqliteManager;
    protected @Inject AlarmUtils alarmUtils;

    private View rootView;
    private AlarmFragmentAdapter adapter;

    public AlarmFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_setnotification, container, false);
        WDApplication.getComponent().inject(this);
        ButterKnife.bind(this, rootView);
        this.rootView = rootView;
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InitializeUI();
    }

    private void InitializeUI() {
        adapter = new AlarmFragmentAdapter(delegate);
        recyclerView.setAdapter(adapter);

        Cursor cursor = sqliteManager.getReadableDatabase().rawQuery("select * from AlarmList", null);
        try{
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToLast()) {
                do {
                    int requestCode = cursor.getInt(0);
                    String days = cursor.getString(1);
                    String startTime = cursor.getString(2);
                    String endTime = cursor.getString(3);
                    String interval = cursor.getString(4);
                    String sday = DateUtils.getDayNameList(days);
                    Alarm alarmModel = new Alarm(requestCode, sday, startTime + " ~ " + endTime, "간격 : " + interval + "시간");
                    adapter.addAlarmItem(alarmModel);
                } while (cursor.moveToPrevious());
            } else
                rootView.findViewById(R.id.setnotification_tv_message).setVisibility(View.VISIBLE);
        }
        catch (Exception e){
            e.printStackTrace();
        } finally {
            cursor.close();
        }
    }

    private AlarmViewHolder.Delegate delegate = alarmModel -> {
        try {
            alarmUtils.cancelAlarm(alarmModel.getrequestCode());
            sqliteManager.deleteAlarm(alarmModel.getrequestCode());
            adapter.sections().get(0).remove(alarmModel);

            Toast.makeText(getContext(), "알람이 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
            RxUpdateMainEvent.getInstance().sendEvent();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    };

    @OnClick(R.id.setnotification_fab)
    void Click_Fab_Add(View v) {
        Intent intent = new Intent(getContext(), MakeAlarmActivity.class);
        startActivity(intent);
    }
}