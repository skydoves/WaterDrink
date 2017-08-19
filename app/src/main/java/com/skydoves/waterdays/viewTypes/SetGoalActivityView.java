package com.skydoves.waterdays.viewTypes;

import com.skydoves.waterdays.compose.BaseView;

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public interface SetGoalActivityView extends BaseView {
    void intentMain();
    void onSetSuccess();
    void onSetFailure();
}
