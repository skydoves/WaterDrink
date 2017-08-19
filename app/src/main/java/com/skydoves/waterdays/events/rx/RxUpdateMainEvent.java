package com.skydoves.waterdays.events.rx;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class RxUpdateMainEvent {

    private static RxUpdateMainEvent instance;
    private PublishSubject<Boolean> subject;

    private RxUpdateMainEvent() {
        subject = PublishSubject.create();
    }

    public static RxUpdateMainEvent getInstance() {
        if(instance == null){
            instance = new RxUpdateMainEvent();
        }
        return instance;
    }

    public void sendEvent() {
        subject.onNext(true);
    }

    public void updateBadge() {
        subject.onNext(false);
    }

    public Observable<Boolean> getObservable() {
        return subject;
    }
}
