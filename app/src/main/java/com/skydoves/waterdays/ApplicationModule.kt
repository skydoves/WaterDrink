package com.skydoves.waterdays

import android.app.Application
import com.skydoves.waterdays.persistence.preference.PreferenceManager
import com.skydoves.waterdays.persistence.sqlite.SqliteManager
import com.skydoves.waterdays.utils.AlarmUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@Module
class ApplicationModule(private val application: Application) {

  @Provides
  @Singleton
  internal fun providePreferenceManager(): PreferenceManager {
    return PreferenceManager(this.application)
  }

  @Provides
  @Singleton
  internal fun provideSqliteManager(): SqliteManager {
    return SqliteManager(this.application,
        SqliteManager.DATABASE_NAME,
        null,
        SqliteManager.DATABASE_VERSION)
  }

  @Provides
  @Singleton
  internal fun provideAlarmUtils(): AlarmUtils {
    return AlarmUtils(this.application)
  }
}
