package com.skydoves.waterdays;

import android.app.Application;
import android.support.annotation.NonNull;

import com.skydoves.waterdays.persistence.preference.PreferenceManager;
import com.skydoves.waterdays.persistence.sqlite.SqliteManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@Module
public final class ApplicationModule {
    private final Application application;

    public ApplicationModule(final @NonNull Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    @NonNull PreferenceManager providePreferenceManager() {
        return new PreferenceManager(this.application);
    }

    @Provides
    @Singleton
    @NonNull SqliteManager provideSqliteManager() {
        return new SqliteManager(this.application, SqliteManager.DATABASE_NAME, null, SqliteManager.DATABASE_VERSION);
    }
}
