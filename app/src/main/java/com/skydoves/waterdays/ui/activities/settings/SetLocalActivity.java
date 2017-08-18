package com.skydoves.waterdays.ui.activities.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.skydoves.waterdays.R;
import com.skydoves.waterdays.consts.LocalNames;
import com.skydoves.waterdays.persistence.preference.PreferenceManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class SetLocalActivity extends AppCompatActivity {

    // Systems
    private PreferenceManager systems;
    private int index = -1;

    @BindView(R.id.setlocal_tv_location)
    TextView tv_setlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_local);
        ButterKnife.bind(this);

        // Initialize Systems
        systems = new PreferenceManager(this);

        // Initialize Location Label
        tv_setlocation.setText(LocalNames.getLocalName(systems.getInt("localIndex", 0)));
    }

    // Click Button : select Location
    @OnClick(R.id.setlocal_btn_changelocal)
    public void btn_selectLocal(View v) {
        DialogSelectOption();
    }

    // Click Button : set Location
    @OnClick(R.id.setlocal_btn_setlocal)
    public void btn_setLocal(View v) {
        if(index != -1) {
            systems.putInt("localIndex", index);
            Toast.makeText(this, "지역이 변경되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
            Toast.makeText(this, "지역이 선택되지 않았습니다.", Toast.LENGTH_SHORT).show();
    }

    // Show select Location Dialog
    private void DialogSelectOption() {
        final String items[] = {"서울특별시", "경기도", "강원도", "경상남도", "경상북도", "광주광역시", "대구광역시" , "대전광역시", "부산광역시", "울산광역시", "인천광역시", "전라남도", "전라북도", "충청북도", "충청남도", "제주특별자치도"};
        final int lastIndex = index;
        index = 0;
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        index = whichButton;
                    }
                }).setPositiveButton("선택하기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        tv_setlocation.setText(LocalNames.getLocalName(index));
                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        index = lastIndex;
                    }
                });
        ab.show();
    }
}
