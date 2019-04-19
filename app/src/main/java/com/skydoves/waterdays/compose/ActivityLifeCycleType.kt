package com.skydoves.waterdays.compose

import com.trello.rxlifecycle2.android.ActivityEvent

import io.reactivex.Observable

/**
 * Developed by skydoves on 2017-08-20.
 * Copyright (c) 2017 skydoves rights reserved.
 */

interface ActivityLifeCycleType {
  fun lifecycle(): Observable<ActivityEvent>
}