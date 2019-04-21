/*
 * Copyright (C) 2016 skydoves
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.waterdays.persistence.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.skydoves.waterdays.models.Capacity
import timber.log.Timber
import java.util.ArrayList

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class SqliteManager(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

  override fun onCreate(db: SQLiteDatabase) {
    val CREATE_TABLE_RECORD = "CREATE TABLE " + TABLE_RECORD + "(pk_recordid integer primary key autoincrement, recorddate " +
        "DATETIME DEFAULT (datetime('now','localtime')), amount varchar(4));"
    db.execSQL(CREATE_TABLE_RECORD)

    val CREATE_TABLE_ALARM = "CREATE TABLE " + TABLE_ALARM + "(requestcode integer primary key, daylist varchar(20), " +
        "starttime varchar(20), endtime varchar(20), interval integer);"
    db.execSQL(CREATE_TABLE_ALARM)

    val CREATE_TABLE_CAPACITY = "CREATE TABLE $TABLE_CAPACITY(capacity integer primary key)"
    db.execSQL(CREATE_TABLE_CAPACITY)
  }

  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    db.execSQL("DROP TABLE IF EXISTS $TABLE_RECORD")
    db.execSQL("DROP TABLE IF EXISTS $TABLE_ALARM")
    db.execSQL("DROP TABLE IF EXISTS $TABLE_CAPACITY")
    onCreate(db)
  }

  fun addCapacity(capacity: Capacity) {
    val query_addCapacity = "Insert Into " + TABLE_CAPACITY + " (capacity) Values(" + capacity.amount + ");"
    writableDatabase.execSQL(query_addCapacity)
    Timber.d("SUCCESS Capacity Inserted : %s", capacity.amount)
  }

  fun deleteCapacity(capacity: Capacity) {
    val query_deleteCapacity = "Delete from " + TABLE_CAPACITY + " Where capacity = " + capacity.amount + ""
    writableDatabase.execSQL(query_deleteCapacity)
    Timber.d("SUCCESS Capacity Deleted : %s", capacity.amount)
  }

  val capacityList: List<Capacity>
    get() {
      val capacities = ArrayList<Capacity>()
      val cursor = readableDatabase.rawQuery("select *from $TABLE_CAPACITY order by capacity asc", null)
      if (cursor != null && cursor.count > 0 && cursor.moveToFirst()) {
        do {
          val capacity = Capacity(cursor.getInt(0))
          capacities.add(capacity)
        } while (cursor.moveToNext())
      }
      return capacities
    }

  fun addRecord(amount: String) {
    val query_addRecord = "Insert Into $TABLE_RECORD (amount) Values('$amount');"
    writableDatabase.execSQL(query_addRecord)
    Timber.d("SUCCESS Record Inserted : %s", amount)
  }

  fun deleteRecord(index: Int) {
    val query_addRecord = "Delete from $TABLE_RECORD Where pk_recordid = '$index'"
    writableDatabase.execSQL(query_addRecord)
    Timber.d("SUCCESS Record Deleted : %s", index)
  }

  fun updateRecordAmount(index: Int, amount: Int) {
    val query_updateAmount = "Update $TABLE_RECORD set amount = '$amount' Where pk_recordid = '$index'"
    writableDatabase.execSQL(query_updateAmount)
    Timber.d("SUCCESS Record Updated : %s", amount)
  }

  fun getDayDrinkAmount(datetime: String): Int {
    var TotalAmount = 0
    val cursor = readableDatabase.rawQuery("select * from " + TABLE_RECORD + " where recorddate >= datetime(date('" + datetime + "','localtime')) " +
        "and recorddate < datetime(date('" + datetime + "', 'localtime', '+1 day'))", null)
    if (cursor != null && cursor.count > 0 && cursor.moveToFirst()) {
      do {
        TotalAmount += cursor.getInt(2)
      } while (cursor.moveToNext())
    }
    return TotalAmount
  }

  fun addAlarm(requestcode: Int, daylist: String, starttime: String, endtime: String, interval: Int) {
    val query_addRecord = "Insert Into $TABLE_ALARM Values($requestcode,'$daylist','$starttime','$endtime', $interval);"
    writableDatabase.execSQL(query_addRecord)
    Timber.d("SUCCESS Alarm Inserted : %s", requestcode)
  }

  fun deleteAlarm(requestcode: Int) {
    val query_addRecord = "Delete from $TABLE_ALARM Where requestcode = '$requestcode'"
    writableDatabase.execSQL(query_addRecord)
    Timber.d("SUCCESS Alarm Deleted : %s", requestcode)
  }

  @Synchronized
  override fun close() {
    super.close()
  }

  companion object {
    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "waterdays.db"

    private const val TABLE_RECORD = "RecordList"
    private const val TABLE_ALARM = "AlarmList"
    private const val TABLE_CAPACITY = "capacityList"
  }
}
