package com.skydoves.waterdays.ui.viewholders;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.skydoves.waterdays.R;
import com.skydoves.waterdays.models.Drink;

import butterknife.BindView;

/**
 * Developed by skydoves on 2017-08-20.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class DailyDrinkViewHolder extends BaseViewHolder {

    protected @BindView(R.id.item_dailyrecord_tv_todaydate) TextView tv_item_date;
    protected @BindView(R.id.item_dailyrecord_tv_drinkamount) TextView tv_item_amount;
    protected @BindView(R.id.item_dailyrecord_imv_main) ImageView imv_item_mainicon;
    protected @BindView(R.id.item_dailyrecord_btn_delete) ImageButton ibtn_item_delete;

    private Drink drink;

    private Delegate delegate;

    public interface Delegate {
        void onConfirm(Drink drink);
    }

    public DailyDrinkViewHolder(final @NonNull View view, final @NonNull Delegate delegate) {
        super(view);
        this.delegate = delegate;
    }

    @Override
    public void bindData(@NonNull Object data) throws Exception {
        this.drink = (Drink) data;

        tv_item_date.setText(drink.getDate());
        tv_item_amount.setText(drink.getAmount());
        imv_item_mainicon.setImageDrawable(drink.getImage());
        ibtn_item_delete.setOnClickListener(view -> {
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
            alertDlg.setTitle("알람");

            // Yes - delete
            alertDlg.setPositiveButton("예", (DialogInterface dialog, int which) -> {
                delegate.onConfirm(drink);
            });

            // No - cancel
            alertDlg.setNegativeButton("아니오", (DialogInterface dialog, int which) -> dialog.dismiss());
            alertDlg.setMessage(String.format("해당 기록을 지우시겠습니까?"));
            alertDlg.show();
        });
    }

    @Override
    public void onClick(View v) {

    }
}