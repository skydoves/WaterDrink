package com.skydoves.waterdays.ui.fragments.main;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.skydoves.waterdays.R;
import com.skydoves.waterdays.persistence.preference.PreferenceManager;
import com.skydoves.waterdays.persistence.sqlite.SqliteManager;
import com.skydoves.waterdays.ui.activities.main.MainActivity;
import com.skydoves.waterdays.utils.DateUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class DailyFragment extends Fragment {

    // Systems
    private Context mContext;
    private View rootView;
    private PreferenceManager systems;

    private SqliteManager sqliteManager;

    private int dateCount = 0;

    ArrayList<Listviewitem> data;
    ListviewAdapter adapter;

    public DailyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO skydoves - Auto-generated method stub
        View rootView = inflater.inflate(R.layout.layout_dailyrecord, container, false);
        this.rootView = rootView;
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // getContext
        mContext = getContext();

        sqliteManager = new SqliteManager(mContext, SqliteManager.DATABASE_NAME, null, SqliteManager.DATABASE_VERSION);

        systems = new PreferenceManager(mContext);

        // Initialize
        Initialize();
    }


    /*===================================================
                          Initialize
     ===================================================*/
    private void Initialize()
    {
        // TODO skydoves - Daily Record Init : Auto-generated method stub
        // ListView
        ListView listView=(ListView)rootView.findViewById(R.id.dailyrecord_listview);
        data=new ArrayList();

        // Set Adapter and Click Listner //
        adapter=new ListviewAdapter(mContext, R.layout.item_dailyrecord, data);
        listView.setAdapter(adapter);

        // add Listview Items
        addItems(DateUtils.getFarDay(0));
    }

    // Button Click : Daily Button @Back, @Next
    @OnClick({R.id.dailyrecord_ibtn_back, R.id.dailyrecord_ibtn_next})
    public void DateMoveButton(View v)
    {
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
                    Toast.makeText(mContext, "내일의 기록은 볼 수 없습니다.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    // add ListView Items
    private void addItems(String date)
    {
        // set TextView : Today Date
        TextView tv_todayDate = (TextView)rootView.findViewById(R.id.dailyrecord_tv_todaydate);
        tv_todayDate.setText(date);

        // append day of week Label
        if(dateCount == 0)
            tv_todayDate.append(" (오늘)");
        else
        {
            int dayofweek = DateUtils.getDayofWeek(date, "yyyy-MM-dd");
            switch (dayofweek){
                case 1 :
                    tv_todayDate.append(" (일)");
                    break;
                case 2 :
                    tv_todayDate.append(" (월)");
                    break;
                case  3 :
                    tv_todayDate.append(" (화)");
                    break;
                case 4 :
                    tv_todayDate.append(" (수)");
                    break;
                case 5 :
                    tv_todayDate.append(" (목)");
                    break;
                case 6 :
                    tv_todayDate.append(" (금)");
                    break;
                case 7 :
                    tv_todayDate.append(" (토)");
                    break;
            }
        }

        // clear
        data.clear();

        // add items
        try {
            Cursor cursor = sqliteManager.getReadableDatabase().rawQuery("select * from RecordList where recorddate >= datetime(date('" + date + "','localtime')) and recorddate < datetime(date('" + date + "', 'localtime', '+1 day'))", null);
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToLast()) {
                do {
                    int drinkamout = cursor.getInt(2);
                    int mainicon = R.drawable.img_waterdrop;
                    String[] datetime = cursor.getString(1).split(":");

                    // set mainicon Image
                    if(drinkamout < 250)
                        mainicon = R.drawable.ic_glass0;
                    else if(drinkamout < 330)
                        mainicon = R.drawable.ic_glass01;
                    else if(drinkamout < 500)
                        mainicon = R.drawable.ic_glass06;
                    else if(drinkamout < 750)
                        mainicon = R.drawable.ic_glass05;
                    else if(drinkamout < 1000)
                        mainicon = R.drawable.ic_glass07;
                    else
                        mainicon = R.drawable.ic_glass04;

                    // add ListItem
                    Listviewitem listviewitem = new Listviewitem(cursor.getInt(0), Integer.toString(drinkamout) + "ml", datetime[0] + ":" + datetime[1], mainicon);
                    data.add(listviewitem);
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
        catch (Exception e){

        }

        // notify
        adapter.notifyDataSetChanged();
    }

    /*===================================================
                         ListView Item
     ===================================================*/
    //region
    private class Listviewitem {
        private int index;
        private String date;
        private String amount;
        private int image;

        public int getindex(){return index;}
        public String getdate(){return date;}
        public String getamount(){return amount;}
        public int getimage(){return image;}

        public Listviewitem(int index, String amount, String date, int image){
            this.index = index;
            this.amount = amount;
            this.date=date;
            this.image = image;
        }
    }


    /*===================================================
                        ListView Adapter
     ===================================================*/
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
        public String getItem(int position){return data.get(position).getdate();}

        @Override
        public long getItemId(int position){return position;}

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){

            if(convertView==null)
                convertView=inflater.inflate(layout,parent,false);

            Listviewitem listviewitem=data.get(position);

            // Set item Views //
            // Textview : date
            TextView tv_item_date = (TextView)convertView.findViewById(R.id.item_dailyrecord_tv_todaydate);
            tv_item_date.setText(listviewitem.getdate());

            // Textview : amount
            TextView tv_item_amount = (TextView)convertView.findViewById(R.id.item_dailyrecord_tv_drinkamount);
            tv_item_amount.setText(listviewitem.getamount());

            // ImageView : mainicon
            ImageView imv_item_mainicon = (ImageView)convertView.findViewById(R.id.item_dailyrecord_imv_main);
            BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(mContext, listviewitem.getimage());
            Bitmap imv_bitmap = drawable.getBitmap();
            imv_item_mainicon.setImageBitmap(imv_bitmap);

            // Image Button : delete button
            ImageButton ibtn_item_delete = (ImageButton)convertView.findViewById(R.id.item_dailyrecord_btn_delete);
            ibtn_item_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
                    alertDlg.setTitle("알림");

                    // Yes - delete
                    alertDlg.setPositiveButton("예", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick( DialogInterface dialog, int which )
                        {
                            // remove a Record
                            sqliteManager.deleteRecord(data.get(position).getindex());
                            data.remove(position);

                            // update
                            ((MainActivity)MainActivity.mContext).UpdateFragments();
                        }
                    });

                    // No - cancel
                    alertDlg.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alertDlg.setMessage(String.format("해당 기록을 지우시겠습니까?"));
                    alertDlg.show();
                }
            });

            return convertView;
        }
    }
    //endregion
}
