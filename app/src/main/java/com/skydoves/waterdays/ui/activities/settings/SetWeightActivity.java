package com.skydoves.waterdays.ui.activities.settings;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.skydoves.waterdays.R;
import com.skydoves.waterdays.persistence.preference.PreferenceManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class SetWeightActivity extends AppCompatActivity {

    protected @BindView(R.id.setweight_edt_weight) EditText editText_weight;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setweight);
        ButterKnife.bind(this);

        preferenceManager = new PreferenceManager(this);
        editText_weight.setText(String.valueOf(preferenceManager.getInt("userWeight", 60)));
    }

    @OnClick({R.id.setweight_btn_getrecommend, R.id.setweight_btn_setweight})
    void ClickBtn(View v) {
        if(!editText_weight.getText().toString().equals("")) {
            switch (v.getId()) {
                case R.id.setweight_btn_getrecommend:
                    String Reh = preferenceManager.getString("Reh", "60");
                    Snackbar.make(editText_weight, "추천 수분 섭취량은 " + (Integer.parseInt(editText_weight.getText().toString()) * 30 + (500 - Integer.parseInt(Reh) * 3)) + "ml 입니다.",
                            Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show();
                    break;

                case R.id.setweight_btn_setweight:
                    if(preferenceManager.getString("WaterGoal", "null").equals("null")) {
                        Intent intent = new Intent(this, SetGoalActivity.class);
                        startActivity(intent);
                    }
                    Toast.makeText(this, "체중을 설정하였습니다.", Toast.LENGTH_SHORT).show();
                    preferenceManager.putInt("userWeight", Integer.parseInt(editText_weight.getText().toString()));
                    finish();
                    break;
            }
        }
        else
            Toast.makeText(this, "올바른 체중값을 입력해 주세요!", Toast.LENGTH_SHORT).show();
    }
}
