package com.skydoves.waterdays.models;

import android.graphics.drawable.Drawable;

/**
 * Developed by skydoves on 2017-08-20.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class Drink {
    private int index;
    private String date;
    private String amount;
    private Drawable image;

    public Drink(int index, String amount, String date, Drawable image) {
        this.index = index;
        this.amount = amount;
        this.date = date;
        this.image = image;
    }

    public int getIndex() {
        return index;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    public Drawable getImage() {
        return image;
    }
}
