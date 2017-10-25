package com.skydoves.waterdays.ui.fragments.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.skydoves.waterdays.R;
import com.skydoves.waterdays.WDApplication;
import com.skydoves.waterdays.consts.CapacityDrawable;
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

    /**
     * daily drink viewHolder delegate
     */
    private DailyDrinkViewHolder.Delegate delegate = new DailyDrinkViewHolder.Delegate() {
        @Override
        public void onClick(View view, Drink drink) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle(getString(R.string.title_edit_capacity));
            final EditText input = new EditText(getContext());
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setRawInputType(Configuration.KEYBOARD_12KEY);
            alert.setView(input);
            alert.setPositiveButton(getString(R.string.yes),(DialogInterface dialog, int whichButton) -> {
                try {
                    int amount = Integer.parseInt(input.getText().toString());
                    if(amount > 0 && amount < 3000) {
                        sqliteManager.updateRecordAmount(drink.getIndex(), amount);
                        Drink drink_edited = new Drink(drink.getIndex(), amount+"ml", drink.getDate(), ContextCompat.getDrawable(getContext(), CapacityDrawable.getLayout(amount)));
                        int position = adapter.getPosition(drink);
                        if(position != -1) {
                            adapter.updateDrinkItem(position, drink_edited);
                            RxUpdateMainEvent.getInstance().sendEvent();
                            Toast.makeText(getContext(), R.string.msg_edited_capacity, Toast.LENGTH_SHORT).show();
                        }
                    } else
                        Toast.makeText(getContext(), R.string.msg_invalid_input, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            alert.setNegativeButton(getString(R.string.no), (DialogInterface dialog, int whichButton) -> {
            });
            alert.show();
            InputMethodManager mgr = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.showSoftInputFromInputMethod(input.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED);
        }

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

    /**
     * add items
     * @param date now date value
     */
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
                mainicon = CapacityDrawable.getLayout(drinkAmount);

                // add listItem
                Drink drink = new Drink(cursor.getInt(0), Integer.toString(drinkAmount) + "ml", datetime[0] + ":" + datetime[1], ContextCompat.getDrawable(getContext(), mainicon));
                adapter.addDrinkItem(drink);
            } while (cursor.moveToPrevious());
            cursor.close();
        }

        // if no cursor exist
        TextView tv_message = (TextView) rootView.findViewById(R.id.dailyrecord_tv_message);
        if(cursor.getCount() == 0)
            tv_message.setVisibility(View.VISIBLE);
        else
            tv_message.setVisibility(View.INVISIBLE);
    }
}