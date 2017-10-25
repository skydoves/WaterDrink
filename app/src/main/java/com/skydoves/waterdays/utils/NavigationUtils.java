package com.skydoves.waterdays.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.gigamole.navigationtabbar.ntb.NavigationTabBar;
import com.skydoves.waterdays.R;

import java.util.ArrayList;

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class NavigationUtils {
    public static ArrayList<NavigationTabBar.Model> getNavigationModels(Context mContext) {
        final String[] colors = mContext.getResources().getStringArray(R.array.default_preview);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(mContext, R.drawable.ic_bell),
                        Color.parseColor(colors[0]))
                        .title("알람 설정")
                        .badgeTitle("new")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(mContext, R.drawable.ic_note),
                        Color.parseColor(colors[1]))
                        .title("데일리 기록")
                        .badgeTitle("new")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(mContext, R.drawable.ic_drop),
                        Color.parseColor(colors[2]))
                        .title("수분 섭취량")
                        .badgeTitle("new")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(mContext, R.drawable.ic_chart),
                        Color.parseColor(colors[3]))
                        .title("통계 확인")
                        .badgeTitle("new")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(mContext, R.drawable.ic_setting),
                        Color.parseColor(colors[4]))
                        .title("환경 설정")
                        .badgeTitle("new")
                        .build()
        );
        return models;
    }

    public static void setComponents(Context context, ViewPager viewPager, NavigationTabBar navigationTabBar) {
        navigationTabBar.setModels(NavigationUtils.getNavigationModels(context));
        navigationTabBar.setViewPager(viewPager, 2);
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {
            }
        });
    }
}
