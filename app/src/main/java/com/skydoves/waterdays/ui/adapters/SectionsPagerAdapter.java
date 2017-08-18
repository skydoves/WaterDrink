package com.skydoves.waterdays.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.skydoves.waterdays.ui.fragments.main.AlarmFragment;
import com.skydoves.waterdays.ui.fragments.main.ChartFragment;
import com.skydoves.waterdays.ui.fragments.main.DailyFragment;
import com.skydoves.waterdays.ui.fragments.main.EnvironmentFragment;
import com.skydoves.waterdays.ui.fragments.main.MainWaterFragment;

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    public static final int COUNT_PAGERS = 5;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new MainWaterFragment();
        switch (position) {
            case 0:
                fragment = new AlarmFragment();
                break;
            case 1:
                fragment = new DailyFragment();
                break;
            case 2:
                fragment = new MainWaterFragment();
                break;
            case 3:
                fragment = new ChartFragment();
                break;
            case 4:
                fragment = new EnvironmentFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return COUNT_PAGERS;
    }

    @Override
    public int getItemPosition(Object item) {
        return POSITION_NONE;
    }
}