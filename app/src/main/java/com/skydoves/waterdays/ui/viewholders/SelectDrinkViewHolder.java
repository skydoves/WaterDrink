package com.skydoves.waterdays.ui.viewholders;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.skydoves.waterdays.R;
import com.skydoves.waterdays.models.Capacity;

import butterknife.BindView;

/**
 * Developed by skydoves on 2017-09-22.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class SelectDrinkViewHolder extends BaseViewHolder {

    protected @BindView(R.id.item_selectdrink_img) ImageView icon;
    protected @BindView(R.id.item_selectdrink_tv) TextView value;

    private Capacity capacity;

    private Delegate delegate;

    public interface Delegate {
        void onClick(View view, Capacity capacity);
        void onLongClick(View view, Capacity capacity);
    }

    public SelectDrinkViewHolder(@NonNull View view, Delegate delegate) {
        super(view);
        this.delegate = delegate;
    }

    @Override
    public void bindData(@NonNull Object data) throws Exception {
        this.capacity = (Capacity) data;

        icon.setImageDrawable(capacity.getImage());
        value.setText(capacity.getAmount()+"ml");
    }

    @Override
    public void onClick(View v) {
        this.delegate.onClick(this.view(), capacity);
    }

    @Override
    public boolean onLongClick(View v) {
        this.delegate.onLongClick(this.view(), capacity);
        return true;
    }
}