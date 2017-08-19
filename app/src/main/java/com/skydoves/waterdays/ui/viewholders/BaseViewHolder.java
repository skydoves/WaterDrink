package com.skydoves.waterdays.ui.viewholders;

/**
 * Developed by skydoves on 2017-08-20.
 * Copyright (c) 2017 skydoves rights reserved.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.skydoves.waterdays.compose.ActivityLifeCycleType;
import com.trello.rxlifecycle2.android.ActivityEvent;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ActivityLifeCycleType {

    private final View view;
    private final @NonNull PublishSubject<ActivityEvent> lifecycle = PublishSubject.create();
    private Unbinder unbinder;

    public BaseViewHolder(final @NonNull View view) {
        super(view);
        this.view = view;
        unbinder = ButterKnife.bind(this, view);

        view.setOnClickListener(this);
    }

    /**
     * check dataType
     */
    abstract public void bindData(final @NonNull Object data) throws Exception;

    @Override
    public @NonNull
    Observable<ActivityEvent> lifecycle() {
        return lifecycle;
    }

    public void lifecycleEvent(final @NonNull ActivityEvent event) {
        lifecycle.onNext(event);

        if (ActivityEvent.DESTROY.equals(event)) {
            destroy();
        }
    }

    public void destroy() {
        unbinder.unbind();
    }

    protected @NonNull View view() {
        return view;
    }

    protected @NonNull Context context() {
        return view.getContext();
    }
}