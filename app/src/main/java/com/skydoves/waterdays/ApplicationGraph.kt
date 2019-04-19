package com.skydoves.waterdays

import com.skydoves.waterdays.presenters.AlarmScreenPresenter
import com.skydoves.waterdays.presenters.MainPresenter
import com.skydoves.waterdays.presenters.MakeAlarmPresenter
import com.skydoves.waterdays.presenters.SelectDrinkPresenter
import com.skydoves.waterdays.presenters.SetGoalPresenter
import com.skydoves.waterdays.presenters.SetLocalPresenter
import com.skydoves.waterdays.ui.activities.intro.StartActivity
import com.skydoves.waterdays.ui.activities.settings.SetBubbleColorActivity
import com.skydoves.waterdays.ui.fragments.main.AlarmFragment
import com.skydoves.waterdays.ui.fragments.main.ChartFragment
import com.skydoves.waterdays.ui.fragments.main.DailyFragment
import com.skydoves.waterdays.ui.fragments.main.MainWaterFragment
import com.skydoves.waterdays.utils.AlarmUtils

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

interface ApplicationGraph {
  fun inject(target_: StartActivity)

  fun inject(target_: SetGoalPresenter)

  fun inject(target_: SetLocalPresenter)

  fun inject(target_: AlarmScreenPresenter)

  fun inject(target_: MakeAlarmPresenter)

  fun inject(target_: MainPresenter)

  fun inject(target_: AlarmFragment)

  fun inject(target_: AlarmUtils)

  fun inject(target_: ChartFragment)

  fun inject(target_: MainWaterFragment)

  fun inject(target_: DailyFragment)

  fun inject(target_: SelectDrinkPresenter)

  fun inject(target_: SetBubbleColorActivity)
}
