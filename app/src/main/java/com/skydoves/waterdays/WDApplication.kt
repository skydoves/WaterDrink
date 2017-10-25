package com.skydoves.waterdays

import android.support.multidex.MultiDexApplication
import dagger.Component
import javax.inject.Singleton

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class WDApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        component = DaggerWDApplication_ApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

    @Singleton
    @Component(modules = arrayOf(ApplicationModule::class))
    interface ApplicationComponent : ApplicationGraph

    companion object {
        lateinit var component: ApplicationComponent
            set
    }
}
