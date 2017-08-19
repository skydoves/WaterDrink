package com.skydoves.waterdays.ui.activities.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.skydoves.waterdays.R;
import com.skydoves.waterdays.compose.BaseActivity;
import com.skydoves.waterdays.compose.qualifiers.RequirePresenter;
import com.skydoves.waterdays.consts.LocalNames;
import com.skydoves.waterdays.presenters.SetLocalPresenter;
import com.skydoves.waterdays.viewTypes.SetLocalActivityView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@RequirePresenter(SetLocalPresenter.class)
public class SetLocalActivity extends BaseActivity<SetLocalPresenter, SetLocalActivityView> implements SetLocalActivityView {

    protected @BindView(R.id.setlocal_tv_location) TextView location;

    private int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_local);
        initBaseView(this);
    }

    @Override
    protected void initBaseView(SetLocalActivityView setLocalView) {
        super.initBaseView(setLocalView);
    }

    @Override
    public void initializeUI() {
        location.setText(LocalNames.getLocalName(presenter.getLocalIndex()));
    }

    @OnClick(R.id.setlocal_btn_changelocal)
    public void btn_selectLocal(View v) {
        selectDialogOption();
    }

    @OnClick(R.id.setlocal_btn_setlocal)
    public void btn_setLocal(View v) {
        if(this.index != -1) {
            presenter.setLocalIndex(this.index);
            Toast.makeText(this, "지역이 변경되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
            Toast.makeText(this, "지역이 선택되지 않았습니다.", Toast.LENGTH_SHORT).show();
    }

    private void selectDialogOption() {
        final String items[] = {"서울특별시", "경기도", "강원도", "경상남도", "경상북도", "광주광역시", "대구광역시" , "대전광역시", "부산광역시", "울산광역시", "인천광역시", "전라남도", "전라북도", "충청북도", "충청남도", "제주특별자치도"};
        final int lastIndex = this.index;
        this.index = 0;
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setSingleChoiceItems(items, 0,
                (DialogInterface dialog, int whichButton) ->
                        this.index = whichButton).setPositiveButton("선택하기",
                (DialogInterface dialog, int whichButton) ->
                        location.setText(LocalNames.getLocalName(this.index))).setNegativeButton("취소",
                (DialogInterface dialog, int whichButton) -> this.index = lastIndex).show();
    }
}
