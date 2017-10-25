package com.skydoves.waterdays.models;

import android.graphics.drawable.Drawable;

/**
 * Developed by skydoves on 2017-09-21.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class Capacity {
    private Drawable image;
    private int amount;

    public Capacity(int amount) {
        this.amount = amount;
    }

    public Capacity(Drawable image, int amount) {
        this.image = image;
        this.amount = amount;
    }

    public Drawable getImage() {
        return this.image;
    }

    public int getAmount() {
        return this.amount;
    }
}
