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

package com.skydoves.waterdays.ui.viewholders;

/** Developed by skydoves on 2017-08-20. Copyright (c) 2017 skydoves rights reserved. */
import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.skydoves.waterdays.compose.ActivityLifeCycleType;
import com.trello.rxlifecycle2.android.ActivityEvent;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener, View.OnLongClickListener, ActivityLifeCycleType {

  private final View view;
  private final @NonNull PublishSubject<ActivityEvent> lifecycle = PublishSubject.create();

  public BaseViewHolder(final @NonNull View view) {
    super(view);
    this.view = view;

    view.setOnClickListener(this);
    view.setOnLongClickListener(this);
  }

  /** check dataType */
  public abstract void bindData(final @NonNull Object data) throws Exception;

  @Override
  public @NonNull Observable<ActivityEvent> lifecycle() {
    return lifecycle;
  }

  public void lifecycleEvent(final @NonNull ActivityEvent event) {
    lifecycle.onNext(event);

    if (ActivityEvent.DESTROY.equals(event)) {
      destroy();
    }
  }

  public void destroy() {}

  protected @NonNull View view() {
    return view;
  }

  protected @NonNull Context context() {
    return view.getContext();
  }
}
