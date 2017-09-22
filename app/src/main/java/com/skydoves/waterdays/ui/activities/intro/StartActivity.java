package com.skydoves.waterdays.ui.activities.intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.skydoves.waterdays.R;
import com.skydoves.waterdays.WDApplication;
import com.skydoves.waterdays.compose.BaseView;
import com.skydoves.waterdays.models.Capacity;
import com.skydoves.waterdays.persistence.preference.PreferenceKeys;
import com.skydoves.waterdays.persistence.preference.PreferenceManager;
import com.skydoves.waterdays.persistence.sqlite.SqliteManager;
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
    protected @Inject SqliteManager sqliteManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WDApplication.getComponent().inject(this);

        if(!preferenceManager.getBoolean(PreferenceKeys.NEWBE.first, PreferenceKeys.NEWBE.second)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        if(!preferenceManager.getBoolean(PreferenceKeys.INIT_CAPACITY.first, PreferenceKeys.INIT_CAPACITY.second)) {
            sqliteManager.addCapacity(new Capacity(null, 125));
            sqliteManager.addCapacity(new Capacity(null, 250));
            sqliteManager.addCapacity(new Capacity(null, 350));
            sqliteManager.addCapacity(new Capacity(null, 500));
            sqliteManager.addCapacity(new Capacity(null, 750));
            sqliteManager.addCapacity(new Capacity(null, 1000));
            preferenceManager.putBoolean(PreferenceKeys.INIT_CAPACITY.first, true);
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