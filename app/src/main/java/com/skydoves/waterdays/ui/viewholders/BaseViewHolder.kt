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

package com.skydoves.waterdays.ui.viewholders

/** Developed by skydoves on 2017-08-20. Copyright (c) 2017 skydoves rights reserved.  */
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.waterdays.compose.ActivityLifeCycleType
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

abstract class BaseViewHolder(private val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener, ActivityLifeCycleType {
  private val lifecycle = PublishSubject.create<ActivityEvent>()

  init {
    view.setOnClickListener(this)
    view.setOnLongClickListener(this)
  }

  /** check dataType  */
  @Throws(Exception::class)
  abstract fun bindData(data: Any)

  override fun lifecycle(): Observable<ActivityEvent> {
    return lifecycle
  }

  fun lifecycleEvent(event: ActivityEvent) {
    lifecycle.onNext(event)

    if (ActivityEvent.DESTROY == event) {
      destroy()
    }
  }

  fun destroy() = Unit

  protected fun view(): View {
    return view
  }

  protected fun context(): Context {
    return view.context
  }
}
