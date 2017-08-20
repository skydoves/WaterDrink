package com.skydoves.waterdays.ui.fragments.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jorgecastillo.FillableLoader;
import com.skydoves.waterdays.R;
import com.skydoves.waterdays.WDApplication;
import com.skydoves.waterdays.persistence.preference.PreferenceManager;
import com.skydoves.waterdays.persistence.sqlite.SqliteManager;
import com.skydoves.waterdays.services.receivers.LocalWeather;
import com.skydoves.waterdays.ui.activities.main.SelectDrinkActivity;
import com.skydoves.waterdays.utils.DateUtils;
import com.skydoves.waterdays.utils.FillableLoaderPaths;
import com.skydoves.waterdays.utils.NetworkUtils;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class MainWaterFragment extends Fragment {

    protected @Inject PreferenceManager preferenceManager;
    protected @Inject SqliteManager sqliteManager;

    protected @BindView(R.id.drinkamount_fillableLoader) FillableLoader fillableLoader;

    private View rootView;
    private Context mContext;

    public MainWaterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_todaywaterdrink, container, false);
        WDApplication.getComponent().inject(this);
        ButterKnife.bind(this, rootView);
        this.rootView = rootView;
        this.mContext = getContext();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InitializeUI();
    }

    private void InitializeUI() {
        // get today's drink amount
        float dAmount = sqliteManager.getDayDrinkAmount(DateUtils.getFarDay(0));
        float dGoal = Integer.parseInt(preferenceManager.getString("WaterGoal", "0"));

        // textView - Goal
        TextView tv_goal = (TextView) rootView.findViewById(R.id.drinkamount_tv_goal);
        tv_goal.setText((int) dGoal + " ml");

        // textView - Drunk
        TextView tv_drink = (TextView) rootView.findViewById(R.id.drinkamount_tv_drunk);
        tv_drink.setText((int) dAmount + " ml");

        // textView - require drinking amount
        TextView tv_remain = (TextView) rootView.findViewById(R.id.drinkamount_tv_requireamount);
        if (dAmount < dGoal)
            tv_remain.setText((int) (dGoal - dAmount) + " ml");
        else
            tv_remain.setText(0 + " ml");

        // textView - today's Reh
        TextView tv_rh = (TextView)rootView.findViewById(R.id.drinkamount_tv_rh);
        tv_rh.setText(preferenceManager.getString("Reh", "60") + "%");

        // textView - today drunk percentage
        TextView tv_percentage = (TextView) rootView.findViewById(R.id.drinkamount_percentage);
        if ((dAmount / dGoal) * 100 < 100)
            tv_percentage.setText((int) ((dAmount / dGoal) * 100) + "%");
        else
            tv_percentage.setText("100%");

        // Initialize fillAbleLoader
        fillableLoader.setSvgPath(FillableLoaderPaths.SVG_WATERDROP);
        fillableLoader.start();
        fillableLoader.setPercentage((dAmount / dGoal) * 100);
    }

    @OnClick(R.id.drinkamount_refresh)
    void onRefresh() {
        if(NetworkUtils.isNetworkAvailable(mContext)){
            try {
                // get local weather based on Reh
                LocalWeather localWeather = new LocalWeather(mContext);
                String Reh = localWeather.execute().get();

                // refresh textView
                TextView tv_rh = (TextView)rootView.findViewById(R.id.drinkamount_tv_rh);
                tv_rh.setText(Reh + "%");

                // save Reh data
                preferenceManager.putString("Reh", Reh);
            }
            catch (ExecutionException e) {
                e.printStackTrace();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else
            Toast.makeText(mContext, "네트워크를 확인해 주세요!", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.drinkamount_fab)
    void onDrinkFab() {
        Intent intent = new Intent(mContext, SelectDrinkActivity.class);
        startActivity(intent);
    }
}
