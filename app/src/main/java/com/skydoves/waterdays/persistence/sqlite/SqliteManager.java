package com.skydoves.waterdays.persistence.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_RECORD = "CREATE TABLE " + TABLE_RECORD + "(pk_recordid integer primary key autoincrement, recorddate " +
                "DATETIME DEFAULT (datetime('now','localtime')), amount varchar(4));";
        db.execSQL(CREATE_TABLE_RECORD);

        String CREATE_TABLE_ALARM = "CREATE TABLE " + TABLE_ALARM + "(requestcode integer primary key, daylist varchar(20), " +
                "starttime varchar(20), endtime varchar(20), interval integer);";
        db.execSQL(CREATE_TABLE_ALARM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARM);
        onCreate(db);
    }

    public SqliteManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void addRecord(String amount) {
        String query_addRecord = "Insert Into RecordList (amount) Values('"+  amount +"');";
        getWritableDatabase().execSQL(query_addRecord);
        Timber.d("SUCCESS Record Inserted : " + amount);
    }

    public void deleteRecord(int index) {
        String query_addRecord = "Delete from RecordList Where pk_recordid = '" + index + "'";
        getWritableDatabase().execSQL(query_addRecord);
        Timber.d("SUCCESS Record Deleted : " + index);
    }

    public int getDayDrinkAmount(String datetime) {
        int TotalAmount = 0;
        Cursor cursor = getReadableDatabase().rawQuery("select * from RecordList where recorddate >= datetime(date('"+datetime+"','localtime')) " +
                "and recorddate < datetime(date('"+datetime+"', 'localtime', '+1 day'))" , null);
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            do {
                TotalAmount += cursor.getInt(2);
            } while (cursor.moveToNext());
        }
        return TotalAmount;
    }

    public void addAlarm(int requestcode, String daylist, String starttime, String endtime, int interval) {
        String query_addRecord = "Insert Into AlarmList Values("+requestcode+",'" + daylist +"','" + starttime + "','" + endtime + "', " + interval +");";
        getWritableDatabase().execSQL(query_addRecord);
        Timber.d("SUCCESS Record Inserted : " + requestcode);
    }

    public void deleteAlarm(int requestcode) {
        String query_addRecord = "Delete from AlarmList Where requestcode = '" + requestcode + "'";
        getWritableDatabase().execSQL(query_addRecord);
        Timber.d("SUCCESS Record Deleted : " + requestcode);
    }

    @Override
    public synchronized void close() {
        super.close();
    }
}