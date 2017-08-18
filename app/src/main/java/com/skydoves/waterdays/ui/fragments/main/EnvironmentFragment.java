package com.skydoves.waterdays.ui.fragments.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.skydoves.ElasticAction;
import com.skydoves.waterdays.R;
import com.skydoves.waterdays.ui.activities.settings.NFCActivity;
import com.skydoves.waterdays.ui.activities.settings.SetGoalActivity;
import com.skydoves.waterdays.ui.activities.settings.SetLocalActivity;
import com.skydoves.waterdays.ui.activities.settings.SetMyCupActivity;
import com.skydoves.waterdays.ui.activities.settings.SetWeightActivity;
import com.skydoves.waterdays.ui.activities.settings.SettingActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class EnvironmentFragment extends Fragment {

    public EnvironmentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_settings, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick({R.id.settings_tv_nfc, R.id.settings_tv_recommend, R.id.settings_tv_goal, R.id.settings_tv_mycup, R.id.settings_tv_setlocation, R.id.settings_tv_setting})
    void MenuClick(View v) {
        int duration = 200;
        ElasticAction.doAction(v, duration, 0.9f, 0.9f);
        new Handler().postDelayed(() -> {
            switch (v.getId()) {
                case R.id.settings_tv_nfc:
                    Intent intent0 = new Intent(getContext(), NFCActivity.class);
                    startActivity(intent0);
                    break;

                case R.id.settings_tv_recommend :
                    Intent intent1 = new Intent(getContext(), SetWeightActivity.class);
                    startActivity(intent1);
                    break;

                case R.id.settings_tv_goal:
                    Intent intent2 = new Intent(getContext(), SetGoalActivity.class);
                    startActivity(intent2);
                    break;

                case R.id.settings_tv_mycup:
                    Intent intent3 = new Intent(getContext(), SetMyCupActivity.class);
                    startActivity(intent3);
                    break;

                case R.id.settings_tv_setlocation :
                    Intent intent4 = new Intent(getContext(), SetLocalActivity.class);
                    startActivity(intent4);
                    break;

                case R.id.settings_tv_setting :
                    Intent intent5 = new Intent(getContext(), SettingActivity.class);
                    startActivity(intent5);
                    break;
            }
        }, duration);
    }
}