package com.skydoves.waterdays.ui.fragments.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class SlideFragment : Fragment() {
  private var layoutResId: Int = 0

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (arguments != null && arguments!!.containsKey(ARG_LAYOUT_RES_ID)) {
      layoutResId = arguments!!.getInt(ARG_LAYOUT_RES_ID)
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(layoutResId, container, false)
  }

  companion object {
    private const val ARG_LAYOUT_RES_ID = "layoutResId"

    fun newInstance(layoutResId: Int): SlideFragment {
      val sampleSlide = SlideFragment()
      val args = Bundle()
      args.putInt(ARG_LAYOUT_RES_ID, layoutResId)
      sampleSlide.arguments = args
      return sampleSlide
    }
  }
}
