package com.skydoves.waterdays.ui.activities.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.skydoves.waterdays.R;
import com.skydoves.waterdays.compose.BaseActivity;
import com.skydoves.waterdays.compose.qualifiers.RequirePresenter;
import com.skydoves.waterdays.presenters.SetGoalPresenter;
import com.skydoves.waterdays.ui.activities.main.MainActivity;
import com.skydoves.waterdays.viewTypes.SetGoalActivityView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@RequirePresenter(SetGoalPresenter.class)
public class SetGoalActivity extends BaseActivity<SetGoalPresenter, SetGoalActivityView> implements SetGoalActivityView {

    protected @BindView(R.id.setgoal_edt_goal) EditText editText_goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goal);
        initBaseView(this);
    }

    @Override
    protected void initBaseView(SetGoalActivityView setGoalActivityView) {
        super.initBaseView(setGoalActivityView);
    }

    @Override
    public void initializeUI() {
        editText_goal.setText(presenter.getWaterGoal());
    }

    @OnClick(R.id.setgoal_btn_setgoal)
    void onClickSetGoal(View v) {
        presenter.onClickSetGoal(editText_goal.getText().toString());
    }

    @Override
    public void intentMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        presenter.setOldbe();
    }

    @Override
    public void onSetSuccess() {
        Toast.makeText(this, "목표치를 설정했습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onSetFailure() {
        Toast.makeText(this, "올바른 목표치를 설정해 주세요!", Toast.LENGTH_SHORT).show();
    }
}