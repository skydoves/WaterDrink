package com.skydoves.waterdays.compose.qualifiers

import com.skydoves.waterdays.compose.BasePresenter
import java.lang.annotation.Inherited
import kotlin.reflect.KClass

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@Inherited
@Retention(AnnotationRetention.RUNTIME)
annotation class RequirePresenter(val value: KClass<out BasePresenter<*>>)
