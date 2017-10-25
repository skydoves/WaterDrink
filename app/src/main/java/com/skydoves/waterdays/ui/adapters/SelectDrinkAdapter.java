package com.skydoves.waterdays.ui.adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;

import com.skydoves.waterdays.R;
import com.skydoves.waterdays.models.Capacity;
import com.skydoves.waterdays.ui.viewholders.BaseViewHolder;
import com.skydoves.waterdays.ui.viewholders.SelectDrinkViewHolder;

import java.util.ArrayList;

/**
 * Developed by skydoves on 2017-09-22.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class SelectDrinkAdapter extends BaseAdapter {

    private SelectDrinkViewHolder.Delegate delegate;

    public SelectDrinkAdapter(SelectDrinkViewHolder.Delegate delegate) {
        this.delegate = delegate;
        addSection(new ArrayList<Capacity>());
        notifyDataSetChanged();
    }

    public void addCapacityItem(Capacity capacity) {
        sections().get(0).add(capacity);
        notifyDataSetChanged();
    }

    public void removeDrinkItem(Capacity capacity) {
        sections().get(0).remove(capacity);
        notifyDataSetChanged();
    }

    @Override
    protected int layout(@NonNull SectionRow sectionRow) {
        return R.layout.item_selectdrink;
    }

    @NonNull
    @Override
    protected BaseViewHolder viewHolder(@LayoutRes int layout, @NonNull View view) {
        return new SelectDrinkViewHolder(view, delegate);
    }
}