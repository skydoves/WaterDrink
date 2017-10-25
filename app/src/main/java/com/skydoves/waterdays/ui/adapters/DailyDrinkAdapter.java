package com.skydoves.waterdays.ui.adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;

import com.skydoves.waterdays.R;
import com.skydoves.waterdays.models.Drink;
import com.skydoves.waterdays.ui.viewholders.BaseViewHolder;
import com.skydoves.waterdays.ui.viewholders.DailyDrinkViewHolder;

import java.util.ArrayList;

/**
 * Developed by skydoves on 2017-08-20.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class DailyDrinkAdapter extends BaseAdapter {

    private DailyDrinkViewHolder.Delegate delegate;

    public DailyDrinkAdapter(DailyDrinkViewHolder.Delegate delegate) {
        this.delegate = delegate;
        addSection(new ArrayList<Drink>());
        notifyDataSetChanged();
    }

    public void addDrinkItem(Drink drink) {
        sections().get(0).add(drink);
        notifyDataSetChanged();
    }

    public void updateDrinkItem(int position, Drink drink) {
        sections().get(0).set(position, drink);
        notifyDataSetChanged();
        // notifyItemInserted(position);
    }

    public int getPosition(Drink drink) {
        return sections().get(0).indexOf(drink);
    }

    public void remove(Drink drink) {
        sections().get(0).remove(drink);
        notifyDataSetChanged();
    }

    public void clear() {
        if(sections().get(0) != null)
            sections().get(0).clear();
        notifyDataSetChanged();
    }

    @Override
    protected int layout(@NonNull SectionRow sectionRow) {
        return R.layout.item_dailyrecord;
    }

    @NonNull
    @Override
    protected BaseViewHolder viewHolder(@LayoutRes int layout, @NonNull View view) {
        return new DailyDrinkViewHolder(view, delegate);
    }
}