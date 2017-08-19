package com.skydoves.waterdays;

import com.skydoves.waterdays.presenters.AlarmScreenPresenter;
import com.skydoves.waterdays.presenters.MainPresenter;
import com.skydoves.waterdays.presenters.MakeAlarmPresenter;
import com.skydoves.waterdays.presenters.SetGoalPresenter;
import com.skydoves.waterdays.presenters.SetLocalPresenter;
import com.skydoves.waterdays.ui.activities.intro.StartActivity;

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public interface ApplicationGraph {
    void inject(StartActivity __);
    void inject(SetGoalPresenter __);
    void inject(SetLocalPresenter __);
    void inject(AlarmScreenPresenter __);
    void inject(MakeAlarmPresenter __);
    void inject(MainPresenter __);
}
