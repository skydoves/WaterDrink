package com.skydoves.waterdays.ui.activities.intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.skydoves.waterdays.R;
import com.skydoves.waterdays.WDApplication;
import com.skydoves.waterdays.compose.BaseView;
import com.skydoves.waterdays.persistence.preference.PreferenceKeys;
import com.skydoves.waterdays.persistence.preference.PreferenceManager;
import com.skydoves.waterdays.ui.activities.main.MainActivity;
import com.skydoves.waterdays.ui.activities.settings.SetWeightActivity;
import com.skydoves.waterdays.ui.fragments.intro.SlideFragment;

import javax.inject.Inject;

/**
 * Developed by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class StartActivity extends AppIntro implements BaseView {

    protected @Inject PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WDApplication.getComponent().inject(this);

        if(!preferenceManager.getBoolean(PreferenceKeys.NEWBE.first, PreferenceKeys.NEWBE.second)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        initializeUI();
    }

    @Override
    public void initializeUI() {
        addSlide(SlideFragment.newInstance(R.layout.intro1));
        addSlide(SlideFragment.newInstance(R.layout.intro2));
        addSlide(SlideFragment.newInstance(R.layout.intro3));
        addSlide(SlideFragment.newInstance(R.layout.intro4));
        setDoneText(getString(R.string.start));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        ActivityStart();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        ActivityStart();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }

    private void ActivityStart() {
        startActivity( new Intent(this, SetWeightActivity.class));
        finish();
    }
}