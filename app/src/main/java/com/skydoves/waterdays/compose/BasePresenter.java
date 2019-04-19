package com.skydoves.waterdays.compose;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import timber.log.Timber;

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class BasePresenter<ViewType extends BaseView> {

  protected ViewType baseView;

  /**
   * get baseView
   *
   * @return baseView
   */
  protected ViewType getBaseView() {
    return baseView;
  }

  @CallSuper
  protected void setBaseView(ViewType baseView) {
    this.baseView = baseView;
  }

  @CallSuper
  protected void onCreate(final @NonNull Context context, final @Nullable Bundle savedInstanceState) {
    Timber.d("onCreate %s", this.toString());
  }

  @CallSuper
  protected void onDestroy() {
    Timber.d("onDestroy %s", this.toString());
  }
}