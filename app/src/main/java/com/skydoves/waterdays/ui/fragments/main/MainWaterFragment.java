package com.skydoves.waterdays.ui.fragments.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jorgecastillo.FillableLoader;
import com.skydoves.waterdays.R;
import com.skydoves.waterdays.utils.DateUtils;
import com.skydoves.waterdays.utils.FillableLoaderPaths;
import com.skydoves.waterdays.services.receivers.LocalWeather;
import com.skydoves.waterdays.persistence.preference.PreferenceManager;
import com.skydoves.waterdays.persistence.sqlite.SqliteManager;
import com.skydoves.waterdays.ui.activities.main.SelectDrinkActivity;
import com.skydoves.waterdays.utils.NetworkUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class MainWaterFragment extends Fragment {

    // Systems
    private View rootView;
    private Context mContext;
    private PreferenceManager systems;

    private SqliteManager sqliteManager;

    private FillableLoader fillableLoader;

    public MainWaterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO skydoves - Auto-generated method stub
        View rootView = inflater.inflate(R.layout.layout_todaywaterdrink, container, false);
        this.rootView = rootView;
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        // getContext & Init Systems
        mContext = getContext();
        systems = new PreferenceManager(mContext);
        sqliteManager = new SqliteManager(mContext, SqliteManager.DATABASE_NAME, null, SqliteManager.DATABASE_VERSION);

        // Initialize Fragment
        Initialize();
    }

    /*===================================================
                          Initialize
     ===================================================*/
    private void Initialize()
    {
        // TODO skydoves - Today's WaterDrink Init : Auto-generated method stub
        try {
            // get Today Drink Amount
            float dAmount = sqliteManager.getDayDrinkAmount(DateUtils.getFarDay(0));
            float dGoal = Integer.parseInt(systems.getString("WaterGoal", "0"));

            // TextView - Goal
            TextView tv_goal = (TextView) rootView.findViewById(R.id.drinkamount_tv_goal);
            tv_goal.setText((int) dGoal + " ml");

            // TextView - Drunk
            TextView tv_drink = (TextView) rootView.findViewById(R.id.drinkamount_tv_drunk);
            tv_drink.setText((int) dAmount + " ml");

            // TextView - Require Drinking Amount
            TextView tv_remain = (TextView) rootView.findViewById(R.id.drinkamount_tv_requireamount);
            if (dAmount < dGoal)
                tv_remain.setText((int) (dGoal - dAmount) + " ml");
            else
                tv_remain.setText(0 + " ml");

            // TextView - Today Reh
            TextView tv_rh = (TextView)rootView.findViewById(R.id.drinkamount_tv_rh);
            tv_rh.setText(systems.getString("Reh", "60") + "%");

            // TextView - Today Drunk Percentage
            TextView tv_percentage = (TextView) rootView.findViewById(R.id.drinkamount_percentage);
            if ((dAmount / dGoal) * 100 < 100)
                tv_percentage.setText((int) ((dAmount / dGoal) * 100) + "%");
            else
                tv_percentage.setText("100%");

            // Initialize fillableLoader
            fillableLoader = (FillableLoader) rootView.findViewById(R.id.drinkamount_fillableLoader);
            fillableLoader.setSvgPath(FillableLoaderPaths.SVG_WATERDROP);
            fillableLoader.start();
            fillableLoader.setPercentage((dAmount / dGoal) * 100);
        }
        catch (Exception e){

        }
    }


    // Button Click : Refresh, Fab Button //
    @OnClick({R.id.drinkamount_refresh, R.id.drinkamount_fab})
    void Click_fab(View v) {

        ViewCompat.animate(v)
                .setDuration(150)
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setInterpolator(new CycleInterpolator(0.5f))
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(final View view) {
                    }
                    @Override
                    public void onAnimationEnd(final View v) {

                        switch (v.getId())
                        {
                            case R.id.drinkamount_refresh :
                                if(NetworkUtils.isNetworkAvailable(mContext)){
                                    try {
                                        // Get Local Weather : Reh
                                        LocalWeather localWeather = new LocalWeather(mContext);
                                        String Reh = localWeather.execute().get();

                                        // Change Textview
                                        TextView tv_rh = (TextView)rootView.findViewById(R.id.drinkamount_tv_rh);
                                        tv_rh.setText(Reh + "%");

                                        // Save Reh Data
                                        systems.putString("Reh", Reh);
                                    }
                                    catch (Exception e){

                                    }
                                }
                                else
                                    Toast.makeText(mContext, "네트워크를 확인해 주세요!", Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.drinkamount_fab :
                                Intent intent = new Intent(mContext, SelectDrinkActivity.class);
                                startActivity(intent);
                                break;
                        }
                    }
                    @Override
                    public void onAnimationCancel(final View view) {
                    }
                })
                .withLayer()
                .start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
