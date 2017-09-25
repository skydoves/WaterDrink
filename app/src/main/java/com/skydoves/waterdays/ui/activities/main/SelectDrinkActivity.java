package com.skydoves.waterdays.ui.activities.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.github.skydoves.ElasticAction;
import com.jakewharton.rxbinding2.view.RxView;
import com.skydoves.waterdays.R;
import com.skydoves.waterdays.compose.BaseActivity;
import com.skydoves.waterdays.compose.qualifiers.RequirePresenter;
import com.skydoves.waterdays.consts.CapacityDrawable;
import com.skydoves.waterdays.models.Capacity;
import com.skydoves.waterdays.presenters.SelectDrinkPresenter;
import com.skydoves.waterdays.ui.adapters.SelectDrinkAdapter;
import com.skydoves.waterdays.ui.viewholders.SelectDrinkViewHolder;
import com.skydoves.waterdays.viewTypes.SelectDrinkActivityView;

import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-09-22.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@RequirePresenter(SelectDrinkPresenter.class)
public class SelectDrinkActivity extends BaseActivity<SelectDrinkPresenter, SelectDrinkActivityView> implements SelectDrinkActivityView {

    protected @BindView(R.id.selectdrink_rcyv) RecyclerView recyclerView;

    private SelectDrinkAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_drink);
        initBaseView(this);

        RxView.clicks(findViewById(R.id.selectdrink_btn_add))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> addCapacity());

        View view = findViewById(R.id.icon_question);
        RxView.clicks(view)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> Snackbar.make(view, getString(R.string.msg_press_long), Snackbar.LENGTH_LONG).setActionTextColor(Color.WHITE).show());

        RxView.clicks(findViewById(R.id.selectdrink_btn_close))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> finish());
    }

    @Override
    protected void initBaseView(SelectDrinkActivityView selectDrinkActivityView) {
        super.initBaseView(selectDrinkActivityView);
    }

    @Override
    public void initializeUI() {
        adapter = new SelectDrinkAdapter(delegate);
        recyclerView.setAdapter(adapter);

        List<Capacity> capacities = presenter.getCapacityItemList();
        if(capacities != null) {
            for(Capacity capacity : capacities) {
                int amount = capacity.getAmount();
                Drawable drawable = ContextCompat.getDrawable(getBaseContext(), CapacityDrawable.getLayout(amount));
                adapter.addCapacityItem(new Capacity(drawable, amount));
            }

            if(capacities.size() == 0)
                Toast.makeText(getBaseContext(), R.string.msg_require_capacity, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * recyclerView onTouch listeners delegate
     */
    SelectDrinkViewHolder.Delegate delegate = new SelectDrinkViewHolder.Delegate() {
        @Override
        public void onClick(View view, Capacity capacity) {
            int duration = 200;
            ElasticAction.doAction(view, duration, 0.9f, 0.9f);

            new Handler().postDelayed(() -> {
                presenter.addRecrodItem(capacity.getAmount());
                finish();
            }, duration);
        }

        @Override
        public void onLongClick(View view, Capacity capacity) {
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
            alertDlg.setTitle(getString(R.string.title_alert));

            // yes - delete
            alertDlg.setPositiveButton(getString(R.string.yes), (DialogInterface dialog, int which) -> {
                presenter.deleteCapacity(capacity);
                adapter.removeDrinkItem(capacity);
                Toast.makeText(getBaseContext(), capacity.getAmount() + "ml " + getString(R.string.msg_delete_capacity), Toast.LENGTH_SHORT).show();
            });

            // no - cancel
            alertDlg.setNegativeButton(getString(R.string.no), (DialogInterface dialog, int which) -> dialog.dismiss());
            alertDlg.setMessage(String.format(getString(R.string.msg_ask_remove_capacity)));
            alertDlg.show();
        }
    };

    /**
     * add a new water capacity cup
     */
    private void addCapacity() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getString(R.string.title_add_capacity));
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setRawInputType(Configuration.KEYBOARD_12KEY);
        alert.setView(input);
        alert.setPositiveButton(getString(R.string.yes),(DialogInterface dialog, int whichButton) -> {
            try {
                int amount = Integer.parseInt(input.getText().toString());
                if(amount > 0 && amount < 3000) {
                    Capacity capacity = new Capacity(ContextCompat.getDrawable(getBaseContext(), CapacityDrawable.getLayout(amount)), amount);
                    presenter.addCapacity(capacity);
                    adapter.addCapacityItem(capacity);
                } else
                    Toast.makeText(getBaseContext(), R.string.msg_invalid_input, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        alert.setNegativeButton(getString(R.string.no), (DialogInterface dialog, int whichButton) -> {

        });
        alert.show();
        InputMethodManager mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.showSoftInputFromInputMethod(input.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED);
    }
}