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

package com.skydoves.waterdays.services.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import com.skydoves.waterdays.consts.IntentExtras;
import com.skydoves.waterdays.persistence.sqlite.SqliteManager;
import com.skydoves.waterdays.utils.AlarmUtils;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

/**
 * Created by skydoves on 2016-10-15. Updated by skydoves on 2017-08-17. Copyright (c) 2017 skydoves
 * rights reserved.
 */
public class AlarmBootReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    if (Objects.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED)) {
      SqliteManager sqliteManager =
          new SqliteManager(
              context, SqliteManager.DATABASE_NAME, null, SqliteManager.DATABASE_VERSION);
      AlarmUtils systems_alarm = new AlarmUtils(context);
      GregorianCalendar mCalendar = new GregorianCalendar();
      AlarmManager mManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

      try {
        Cursor cursor =
            sqliteManager.getReadableDatabase().rawQuery("select * from AlarmList", null);
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
          do {
            int requestCode = cursor.getInt(0);
            systems_alarm.setAlarm(requestCode);

            mCalendar.set(
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH),
                7,
                0);
            mCalendar.add(Calendar.DATE, 1);
            mManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                mCalendar.getTimeInMillis(),
                1200 * 1000,
                pendingIntent(context, IntentExtras.ALARM_PENDING_REQUEST_CODE));
          } while (cursor.moveToNext());
          cursor.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private PendingIntent pendingIntent(Context mContext, int requestCode) {
    Intent intent = new Intent(mContext, LocalWeatherReceiver.class);
    intent.putExtra(IntentExtras.ALARM_PENDING_REQUEST, requestCode);
    return PendingIntent.getBroadcast(
        mContext, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }
}
