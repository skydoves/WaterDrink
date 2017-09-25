package com.skydoves.waterdays.ui.activities.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;

import com.gigamole.navigationtabbar.ntb.NavigationTabBar;
import com.skydoves.waterdays.R;
import com.skydoves.waterdays.compose.BaseActivity;
import com.skydoves.waterdays.compose.qualifiers.RequirePresenter;
import com.skydoves.waterdays.consts.IntentExtras;
import com.skydoves.waterdays.events.rx.RxUpdateMainEvent;
import com.skydoves.waterdays.presenters.MainPresenter;
import com.skydoves.waterdays.services.receivers.AlarmBootReceiver;
import com.skydoves.waterdays.services.receivers.LocalWeatherReceiver;
import com.skydoves.waterdays.ui.adapters.SectionsPagerAdapter;
import com.skydoves.waterdays.utils.NavigationUtils;
import com.skydoves.waterdays.viewTypes.MainActivityView;

import java.util.GregorianCalendar;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@RequirePresenter(MainPresenter.class)
public class MainActivity extends BaseActivity<MainPresenter, MainActivityView> implements MainActivityView {

    protected @BindView(R.id.mainactivity_navi) NavigationTabBar navigationTabBar;

    private NfcAdapter nAdapter;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBaseView(this);

        nAdapter = NfcAdapter.getDefaultAdapter(this);
        getNFCData(getIntent());

        // auto weather alarm
        WeatherAlarm();

        // set boot receiver
        ComponentName receiver = new ComponentName(this, AlarmBootReceiver.class);
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        RxUpdateMainEvent.getInstance().getObservable()
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(flag -> {
                    if(!flag) showBadge(1);
                    mSectionsPagerAdapter.notifyDataSetChanged();
                });
    }

    @Override
    protected void initBaseView(MainActivityView mainActivityView) {
        super.initBaseView(mainActivityView);
    }

    @Override
    public void initializeUI() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        final ViewPager viewPager = (ViewPager) findViewById(R.id.mainactivity_viewpager);
        viewPager.setAdapter(mSectionsPagerAdapter);
        NavigationUtils.setComponents(getBaseContext(), viewPager, navigationTabBar);
    }

    /**
     * show badge with delay
     * @param position
     */
    public void showBadge(final int position) {
        navigationTabBar.postDelayed(() -> {
            final NavigationTabBar.Model model = navigationTabBar.getModels().get(position);
            navigationTabBar.postDelayed(model::showBadge, 100);
        }, 200);
    }

    private void getNFCData(Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                NdefMessage[] messages = new NdefMessage[rawMsgs.length];

                for (int i = 0; i < rawMsgs.length; i++)
                    messages[i] = (NdefMessage) rawMsgs[i];
                byte[] payload = messages[0].getRecords()[0].getPayload();

                presenter.addRecord(new String(payload));
                mSectionsPagerAdapter.notifyDataSetChanged();
                showBadge(1);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            filter.addDataType("waterdays_nfc/*");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent i = new Intent(this, getClass());
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);

        IntentFilter[] filters = new IntentFilter[] { filter, };
        nAdapter.enableForegroundDispatch(this, pIntent, filters, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getNFCData(getIntent());
    }

    private void WeatherAlarm() {
        if(!presenter.getWeatherAlarm()) {
            GregorianCalendar mCalendar = new GregorianCalendar();
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), 1200 * 1000, pendingIntent(IntentExtras.ALARM_PENDING_REQUEST_CODE));
            presenter.setWeatherAlarm(true);
        }
    }

    private PendingIntent pendingIntent(int requestCode) {
        Intent intent = new Intent(this, LocalWeatherReceiver.class);
        intent.putExtra(IntentExtras.ALARM_PENDING_REQUEST, requestCode);
        PendingIntent sender = PendingIntent.getBroadcast(this, requestCode, intent, 0);
        return sender;
    }
}