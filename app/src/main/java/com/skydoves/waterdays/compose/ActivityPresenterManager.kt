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

package com.skydoves.waterdays.compose

import android.content.Context
import android.os.Bundle
import com.skydoves.waterdays.utils.BundleUtils
import java.lang.reflect.InvocationTargetException
import java.util.HashMap
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@Suppress("UNCHECKED_CAST")
class ActivityPresenterManager {
  private val presenters = HashMap<String, BasePresenter<*>>()

  fun <T : BasePresenter<*>> fetch(context: Context,
                                   presenterClass: Class<T>,
                                   savedInstanceState: Bundle?): T {
    val id = fetchId(savedInstanceState)
    var activityPresenter: BasePresenter<*>? = this.presenters[id]

    if (activityPresenter == null) {
      activityPresenter = create(context, presenterClass.kotlin, savedInstanceState, id!!)
    }

    return activityPresenter as T
  }

  fun save(activityPresenter: BasePresenter<*>, envelope: Bundle) {
    envelope.putString(PRESENTER_ID_KEY, findIdForPresenter(activityPresenter))
    val state = Bundle()
    envelope.putBundle(PRESENTER_STATE_KEY, state)
  }

  private fun <T : BasePresenter<*>> create(context: Context, presenterClass: KClass<T>,
                                            savedInstanceState: Bundle?, id: String): BasePresenter<*> {

    val activityPresenter: BasePresenter<*>

    try {
      activityPresenter = presenterClass.createInstance()
    } catch (exception: IllegalAccessException) {
      throw RuntimeException(exception)
    } catch (exception: InvocationTargetException) {
      throw RuntimeException(exception)
    } catch (exception: InstantiationException) {
      throw RuntimeException(exception)
    } catch (exception: NoSuchMethodException) {
      throw RuntimeException(exception)
    }

    this.presenters[id] = activityPresenter
    activityPresenter.onCreate(context, BundleUtils.maybeGetBundle(savedInstanceState, PRESENTER_STATE_KEY))
    return activityPresenter
  }

  fun destroy(activityPresenter: BasePresenter<*>) {
    activityPresenter.onDestroy()

    val iterator = this.presenters.entries.iterator()
    while (iterator.hasNext()) {
      val entry = iterator.next()
      if (activityPresenter == entry.value) {
        iterator.remove()
      }
    }
  }

  private fun fetchId(savedInstanceState: Bundle?): String? {
    return if (savedInstanceState != null)
      savedInstanceState.getString(PRESENTER_STATE_KEY)
    else
      UUID.randomUUID().toString()
  }

  private fun findIdForPresenter(activityPresenter: BasePresenter<*>): String {
    for ((key, value) in this.presenters) {
      if (activityPresenter == value) {
        return key
      }
    }

    throw RuntimeException("cannot find presenter in map!")
  }

  companion object {
    private const val PRESENTER_ID_KEY = "presenter_id"
    private const val PRESENTER_STATE_KEY = "presenter_state"

    val instance = ActivityPresenterManager()
  }
}
