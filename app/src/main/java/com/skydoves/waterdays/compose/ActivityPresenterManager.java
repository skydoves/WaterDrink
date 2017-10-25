package com.skydoves.waterdays.compose;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skydoves.waterdays.utils.BundleUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Developed by skydoves on 2017-08-19.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@SuppressWarnings("unchecked")
public class ActivityPresenterManager {
    private static final String PRESENTER_ID_KEY = "presenter_id";
    private static final String PRESENTER_STATE_KEY = "presenter_state";

    private static final ActivityPresenterManager instance = new ActivityPresenterManager();
    private Map<String, BasePresenter> presenters = new HashMap<>();

    public static @NonNull ActivityPresenterManager getInstance() {
        return instance;
    }

    public <T extends BasePresenter> T fetch(final @NonNull Context context, final @NonNull Class<T> presenterClass,
                                             final @Nullable Bundle savedInstanceState) {
        final String id = fetchId(savedInstanceState);
        BasePresenter activityPresenter = this.presenters.get(id);

        if (activityPresenter == null) {
            activityPresenter = create(context, presenterClass, savedInstanceState, id);
        }

        return (T) activityPresenter;
    }

    public void save(final @NonNull BasePresenter activityPresenter, final @NonNull Bundle envelope) {
        envelope.putString(PRESENTER_ID_KEY, findIdForPresenter(activityPresenter));
        final Bundle state = new Bundle();
        envelope.putBundle(PRESENTER_STATE_KEY, state);
    }

    private <T extends BasePresenter> BasePresenter create(final @NonNull Context context, final @NonNull Class<T> presenterClass,
                                                           final @Nullable Bundle savedInstanceState, final @NonNull String id) {

        final BasePresenter activityPresenter;

        try {
            final Constructor constructor = presenterClass.getConstructor();
            activityPresenter = (BasePresenter) constructor.newInstance();
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        } catch (InvocationTargetException exception) {
            throw new RuntimeException(exception);
        } catch (InstantiationException exception) {
            throw new RuntimeException(exception);
        } catch (NoSuchMethodException exception) {
            throw new RuntimeException(exception);
        }

        this.presenters.put(id, activityPresenter);
        activityPresenter.onCreate(context, BundleUtils.maybeGetBundle(savedInstanceState, PRESENTER_STATE_KEY));
        return activityPresenter;
    }

    protected void destroy(final @NonNull BasePresenter activityPresenter) {
        activityPresenter.onDestroy();

        final Iterator<Map.Entry<String, BasePresenter>> iterator = this.presenters.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<String, BasePresenter> entry = iterator.next();
            if (activityPresenter.equals(entry.getValue())) {
                iterator.remove();
            }
        }
    }

    private String fetchId(final @Nullable Bundle savedInstanceState) {
        return savedInstanceState != null ?
                savedInstanceState.getString(PRESENTER_STATE_KEY) :
                UUID.randomUUID().toString();
    }

    private String findIdForPresenter(final @NonNull BasePresenter activityPresenter) {
        for (final Map.Entry<String, BasePresenter> entry : this.presenters.entrySet()) {
            if (activityPresenter.equals(entry.getValue())) {
                return entry.getKey();
            }
        }

        throw new RuntimeException("cannot find presenter in map!");
    }
}