package com.skydoves.waterdays.ui.viewholders;

/**
 * Developed by skydoves on 2017-08-20.
 * Copyright (c) 2017 skydoves rights reserved.
 */

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skydoves.waterdays.compose.ActivityLifeCycleType;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, ActivityLifeCycleType {

  private final View view;
  private final @NonNull
  PublishSubject<ActivityEvent> lifecycle = PublishSubject.create();

  public BaseViewHolder(final @NonNull View view) {
    super(view);
    this.view = view;

    view.setOnClickListener(this);
    view.setOnLongClickListener(this);
  }

  /**
   * check dataType
   */
  abstract public void bindData(final @NonNull Object data) throws Exception;

  @Override
  public @NonNull
  Observable<ActivityEvent> lifecycle() {
    return lifecycle;
  }

  public void lifecycleEvent(final @NonNull ActivityEvent event) {
    lifecycle.onNext(event);

    if (ActivityEvent.DESTROY.equals(event)) {
      destroy();
    }
  }

  public void destroy() {

  }

  protected @NonNull
  View view() {
    return view;
  }

  protected @NonNull
  Context context() {
    return view.getContext();
  }
}