package com.skydoves.waterdays;

import android.support.multidex.MultiDexApplication;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class WDApplication extends MultiDexApplication {

    private static ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerWDApplication_ApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    @Singleton
    @Component(modules = ApplicationModule.class)
    public interface ApplicationComponent extends ApplicationGraph {

    }

    public static ApplicationComponent getComponent() {
        return component;
    }
}
