package com.skydoves.waterdays.compose.qualifiers;

import com.skydoves.waterdays.compose.BasePresenter;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePresenter {
  Class<? extends BasePresenter> value();
}
