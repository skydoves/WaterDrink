package com.skydoves.waterdays.persistence.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.skydoves.waterdays.models.Capacity;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class SqliteManager extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "waterdays.db";

    private static final String TABLE_RECORD = "RecordList";
    private static final String TABLE_ALARM = "AlarmList";
    private static final String TABLE_CAPACITY = "capacityList";

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_RECORD = "CREATE TABLE " + TABLE_RECORD + "(pk_recordid integer primary key autoincrement, recorddate " +
                "DATETIME DEFAULT (datetime('now','localtime')), amount varchar(4));";
        db.execSQL(CREATE_TABLE_RECORD);

        String CREATE_TABLE_ALARM = "CREATE TABLE " + TABLE_ALARM + "(requestcode integer primary key, daylist varchar(20), " +
                "starttime varchar(20), endtime varchar(20), interval integer);";
        db.execSQL(CREATE_TABLE_ALARM);

        String CREATE_TABLE_CAPACITY = "CREATE TABLE " + TABLE_CAPACITY + "(capacity integer primary key)";
        db.execSQL(CREATE_TABLE_CAPACITY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAPACITY);
        onCreate(db);
    }

    public SqliteManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void addCapacity(Capacity capacity) {
        String query_addCapacity = "Insert Into " + TABLE_CAPACITY +" (capacity) Values("+  capacity.getAmount() +");";
        getWritableDatabase().execSQL(query_addCapacity);
        Timber.d("SUCCESS Capacity Inserted : " + capacity.getAmount());
    }

    public void deleteCapacity(Capacity capacity) {
        String query_deleteCapacity = "Delete from " + TABLE_CAPACITY + " Where capacity = " + capacity.getAmount() + "";
        getWritableDatabase().execSQL(query_deleteCapacity);
        Timber.d("SUCCESS Capacity Deleted : " + capacity.getAmount());
    }

    public List<Capacity> getCapacityList() {
        List<Capacity> capacities = new ArrayList<>();
        Cursor cursor  = getReadableDatabase().rawQuery("select *from " + TABLE_CAPACITY + " order by capacity asc", null);
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            do {
                Capacity capacity = new Capacity(cursor.getInt(0));
                capacities.add(capacity);
            } while (cursor.moveToNext());
        }
        return capacities;
    }

    public void addRecord(String amount) {
        String query_addRecord = "Insert Into " + TABLE_RECORD +" (amount) Values('"+  amount +"');";
        getWritableDatabase().execSQL(query_addRecord);
        Timber.d("SUCCESS Record Inserted : " + amount);
    }

    public void deleteRecord(int index) {
        String query_addRecord = "Delete from " + TABLE_RECORD +" Where pk_recordid = '" + index + "'";
        getWritableDatabase().execSQL(query_addRecord);
        Timber.d("SUCCESS Record Deleted : " + index);
    }

    public void updateRecordAmount(int index, int amount) {
        String query_updateAmount = "Update " + TABLE_RECORD + " set amount = '" + amount + "' Where pk_recordid = '" + index + "'";
        getWritableDatabase().execSQL(query_updateAmount);
        Timber.d("SUCCESS Record Updated : " + amount);
    }

    public int getDayDrinkAmount(String datetime) {
        int TotalAmount = 0;
        Cursor cursor = getReadableDatabase().rawQuery("select * from " + TABLE_RECORD + " where recorddate >= datetime(date('"+datetime+"','localtime')) " +
                "and recorddate < datetime(date('"+datetime+"', 'localtime', '+1 day'))" , null);
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            do {
                TotalAmount += cursor.getInt(2);
            } while (cursor.moveToNext());
        }
        return TotalAmount;
    }

    public void addAlarm(int requestcode, String daylist, String starttime, String endtime, int interval) {
        String query_addRecord = "Insert Into "  + TABLE_ALARM + " Values("+requestcode+",'" + daylist +"','" + starttime + "','" + endtime + "', " + interval +");";
        getWritableDatabase().execSQL(query_addRecord);
        Timber.d("SUCCESS Alarm Inserted : " + requestcode);
    }

    public void deleteAlarm(int requestcode) {
        String query_addRecord = "Delete from " + TABLE_ALARM + " Where requestcode = '" + requestcode + "'";
        getWritableDatabase().execSQL(query_addRecord);
        Timber.d("SUCCESS Alarm Deleted : " + requestcode);
    }

    @Override
    public synchronized void close() {
        super.close();
    }
}