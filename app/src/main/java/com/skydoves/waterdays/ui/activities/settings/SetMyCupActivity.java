package com.skydoves.waterdays.ui.activities.settings;

import android.os.Bundle;
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

public class SetMyCupActivity extends AppCompatActivity {

    // Systems
    PreferenceManager systems;

    @BindView(R.id.setmycup_edt_mycup)
    EditText edt_myCup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_my_cup);
        ButterKnife.bind(this);

        // Initialize Systems
        systems = new PreferenceManager(this);

        // Initialize settings
        edt_myCup.setText(systems.getString("MyCup", "250"));
    }

    @OnClick(R.id.setmycup_btn_setcup)
    public void Click_setMyCup(View v){
        if(!edt_myCup.getText().toString().equals("")) {
            systems.putString("MyCup", edt_myCup.getText().toString());
            Toast.makeText(this, "내 컵의 용량이 설정되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
            Toast.makeText(this, "올바른 용량을 입력해주세요.", Toast.LENGTH_SHORT).show();
    }
}