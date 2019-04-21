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

package com.skydoves.waterdays

import androidx.multidex.MultiDexApplication
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
  @Component(modules = [ApplicationModule::class])
  interface ApplicationComponent : ApplicationGraph

  companion object {
    lateinit var component: ApplicationComponent
  }
}
