package com.skydoves.waterdays.ui.fragments.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.skydoves.waterdays.R;
import com.skydoves.waterdays.events.rx.RxUpdateMainEvent;
import com.skydoves.waterdays.persistence.sqlite.SqliteManager;
import com.skydoves.waterdays.ui.activities.main.MakeAlarmActivity;
import com.skydoves.waterdays.utils.AlarmUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class AlarmFragment extends Fragment {

    private View rootView;

    private AlarmUtils alarmUtils;
    private SqliteManager sqliteManager;

    private ArrayList<Listviewitem> data;
    private ListviewAdapter adapter;

    public AlarmFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_setnotification, container, false);
        ButterKnife.bind(this, rootView);
        this.rootView = rootView;
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sqliteManager = new SqliteManager(getContext(), SqliteManager.DATABASE_NAME, null, SqliteManager.DATABASE_VERSION);
        alarmUtils = new AlarmUtils(getContext());


        InitializeUI();
    }

    private void InitializeUI() {
        ListView listView=(ListView)rootView.findViewById(R.id.setnotification_listview);
        this.data = new ArrayList();

        adapter = new ListviewAdapter(getContext(), R.layout.item_alarmrecord, data);
        listView.setAdapter(adapter);

        Cursor cursor = sqliteManager.getReadableDatabase().rawQuery("select * from AlarmList", null);
        try{
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToLast()) {
                do {
                    //  get columns
                    int requestcode = cursor.getInt(0);
                    String days = cursor.getString(1);
                    String starttime = cursor.getString(2);
                    String endtime = cursor.getString(3);
                    String interval = cursor.getString(4);

                    String sday = "";
                    if(days.contains("0"))
                        sday += "일 ";
                    if(days.contains("1"))
                        sday += "월 ";
                    if(days.contains("2"))
                        sday += "화 ";
                    if(days.contains("3"))
                        sday += "수 ";
                    if(days.contains("4"))
                        sday += "목 ";
                    if(days.contains("5"))
                        sday += "금 ";
                    if(days.contains("6"))
                        sday += "토 ";

                    Listviewitem listviewitem = new Listviewitem(requestcode, sday, starttime + " ~ " + endtime, "간격 : " + interval + "시간");
                    data.add(listviewitem);
                } while (cursor.moveToPrevious());
            }

            if(cursor.getCount() == 0)
                rootView.findViewById(R.id.setnotification_tv_message).setVisibility(View.VISIBLE);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            cursor.close();
        }
    }

    @OnClick(R.id.setnotification_fab)
    void Click_Fab_Add(View v) {
        ViewCompat.animate(v)
                .setDuration(150)
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setInterpolator(new CycleInterpolator(0.5f))
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {
                    }
                    @Override
                    public void onAnimationEnd(final View v) {
                        Intent intent = new Intent(getContext(), MakeAlarmActivity.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onAnimationCancel(final View view) {
                    }
                })
                .withLayer()
                .start();
    }


    private class Listviewitem {
        private int requestcode;
        private String days;
        private String times;
        private String interval;

        public int getrequestcode(){return requestcode;}
        public String getdays(){return days;}
        public String gettimes(){return times;}
        public String getinterval(){return interval;}

        public Listviewitem(int index, String amount, String date, String interval){
            this.requestcode = index;
            this.days = amount;
            this.times=date;
            this.interval = interval;
        }
    }

    private class ListviewAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ArrayList<Listviewitem> data;
        private int layout;

        public ListviewAdapter(Context context, int layout, ArrayList<Listviewitem> data){
            this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.data=data;
            this.layout=layout;
        }

        @Override
        public int getCount(){return data.size();}

        @Override
        public String getItem(int position){return data.get(position).getdays();}

        @Override
        public long getItemId(int position){return position;}

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){

            if(convertView==null)
                convertView=inflater.inflate(layout,parent,false);

            Listviewitem listviewitem=data.get(position);

            TextView tv_item_days = (TextView)convertView.findViewById(R.id.item_alarmrecord_tv_days);
            tv_item_days.setText(listviewitem.getdays());

            TextView tv_item_times = (TextView)convertView.findViewById(R.id.item_alarmrecord_tv_times);
            tv_item_times.setText(listviewitem.gettimes());

            TextView tv_item_interval = (TextView)convertView.findViewById(R.id.item_alarmrecord_tv_interval);
            tv_item_interval.setText(listviewitem.getinterval());

            ImageButton ibtn_item_delete = (ImageButton)convertView.findViewById(R.id.item_alarmrecord_btn_delete);
            ibtn_item_delete.setOnClickListener(view -> {
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
                alertDlg.setTitle("알람");

                alertDlg.setPositiveButton("예", (DialogInterface dialog, int which) -> {
                    try {
                        alarmUtils.cancelAlarm(data.get(position).getrequestcode());
                        sqliteManager.deleteAlarm(data.get(position).getrequestcode());
                        data.remove(position);

                        Toast.makeText(getContext(), "알람이 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                        RxUpdateMainEvent.getInstance().sendEvent();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                });

                alertDlg.setNegativeButton("아니오", ((dialog, which) -> dialog.dismiss()));
                alertDlg.setMessage(String.format("해당 알람을 지우시겠습니까?"));
                alertDlg.show();
            });

            return convertView;
        }
    }
}