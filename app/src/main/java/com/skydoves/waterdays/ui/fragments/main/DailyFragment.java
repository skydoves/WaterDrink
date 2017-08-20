package com.skydoves.waterdays.ui.fragments.main;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.skydoves.waterdays.R;
import com.skydoves.waterdays.WDApplication;
import com.skydoves.waterdays.events.rx.RxUpdateMainEvent;
import com.skydoves.waterdays.models.Drink;
import com.skydoves.waterdays.persistence.sqlite.SqliteManager;
import com.skydoves.waterdays.ui.adapters.DailyDrinkAdapter;
import com.skydoves.waterdays.ui.viewholders.DailyDrinkViewHolder;
import com.skydoves.waterdays.utils.DateUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class DailyFragment extends Fragment {

    protected @Inject SqliteManager sqliteManager;

    protected @BindView(R.id.dailyrecord_listview) RecyclerView recyclerView;

    private View rootView;

    private int dateCount = 0;
    private DailyDrinkAdapter adapter;

    public DailyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_dailyrecord, container, false);
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
        adapter = new DailyDrinkAdapter(delegate);
        recyclerView.setAdapter(adapter);
        addItems(DateUtils.getFarDay(0));
    }

    private DailyDrinkViewHolder.Delegate delegate = new DailyDrinkViewHolder.Delegate() {
        @Override
        public void onConfirm(Drink drink) {
            sqliteManager.deleteRecord(drink.getIndex());
            adapter.remove(drink);
            RxUpdateMainEvent.getInstance().sendEvent();
        }
    };

    @OnClick({R.id.dailyrecord_ibtn_back, R.id.dailyrecord_ibtn_next})
    public void DateMoveButton(View v) {
        switch (v.getId()){
            case R.id.dailyrecord_ibtn_back :
                dateCount--;
                addItems(DateUtils.getFarDay(dateCount));
                break;

            case R.id.dailyrecord_ibtn_next :
                if(dateCount < 0) {
                    dateCount++;
                    addItems(DateUtils.getFarDay(dateCount));
                }
                else
                    Toast.makeText(getContext(), "내일의 기록은 볼 수 없습니다.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void addItems(String date) {
        TextView tv_todayDate = (TextView)rootView.findViewById(R.id.dailyrecord_tv_todaydate);
        tv_todayDate.setText(date);

        // append day of week Label
        if(dateCount == 0) {
            tv_todayDate.append(" (오늘)");
        } else {
            int dayOfWeek = DateUtils.getDayofWeek(date, DateUtils.getDateFormat());
            tv_todayDate.append(DateUtils.getIndexofDayNameHead(dayOfWeek));
        }

        // clear
        adapter.clear();

        // add items
        Cursor cursor = sqliteManager.getReadableDatabase().rawQuery("select * from RecordList where recorddate >= datetime(date('" + date + "','localtime')) and recorddate < datetime(date('" + date + "', 'localtime', '+1 day'))", null);
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToLast()) {
            do {
                int drinkAmount = cursor.getInt(2);
                int mainicon;
                String[] datetime = cursor.getString(1).split(":");

                // set mainIcon Image
                if(drinkAmount < 250)
                    mainicon = R.drawable.ic_glass0;
                else if(drinkAmount < 330)
                    mainicon = R.drawable.ic_glass01;
                else if(drinkAmount < 500)
                    mainicon = R.drawable.ic_glass06;
                else if(drinkAmount < 750)
                    mainicon = R.drawable.ic_glass05;
                else if(drinkAmount < 1000)
                    mainicon = R.drawable.ic_glass07;
                else
                    mainicon = R.drawable.ic_glass04;

                // add ListItem
                Drink drink = new Drink(cursor.getInt(0), Integer.toString(drinkAmount) + "ml", datetime[0] + ":" + datetime[1], ContextCompat.getDrawable(getContext(), mainicon));
                adapter.addDrinkItem(drink);
            } while (cursor.moveToPrevious());
            cursor.close();
        }

        // if no Cursor Exist
        TextView tv_message = (TextView) rootView.findViewById(R.id.dailyrecord_tv_message);
        if(cursor.getCount() == 0)
            tv_message.setVisibility(View.VISIBLE);
        else
            tv_message.setVisibility(View.INVISIBLE);
    }
}