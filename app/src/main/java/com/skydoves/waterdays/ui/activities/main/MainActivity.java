package com.skydoves.waterdays.ui.activities.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.gigamole.navigationtabbar.ntb.NavigationTabBar;
import com.skydoves.waterdays.R;
import com.skydoves.waterdays.consts.IntentExtras;
import com.skydoves.waterdays.persistence.preference.PreferenceManager;
import com.skydoves.waterdays.persistence.sqlite.SqliteManager;
import com.skydoves.waterdays.services.receivers.AlarmBootReceiver;
import com.skydoves.waterdays.services.receivers.LocalWeatherReceiver;
import com.skydoves.waterdays.ui.adapters.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class MainActivity extends AppCompatActivity  {

    protected @BindView(R.id.mainactivity_navi) NavigationTabBar navigationTabBar;

    public static Context mContext;

    private SqliteManager sqliteManager;
    private PreferenceManager preferenceManager;

    private NfcAdapter nAdapter;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;

        preferenceManager = new PreferenceManager(this);
        sqliteManager = new SqliteManager(this, SqliteManager.DATABASE_NAME, null, SqliteManager.DATABASE_VERSION);

        InitializeUI(2);

        // Check NFC Available
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "이 앱은 NFC 기능을 탑재한 디바이스에서만 작동합니다.", Toast.LENGTH_LONG).show();
            finish();
        }

        // set NFC adapter
        nAdapter = NfcAdapter.getDefaultAdapter(this);
        getNFCData(getIntent());

        // auto weather alarm
        WeatherAlarm();

        // set boot receiver
        ComponentName receiver = new ComponentName(this, AlarmBootReceiver.class);
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    private void InitializeUI(final int pageNum) {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        final ViewPager viewPager = (ViewPager) findViewById(R.id.mainactivity_viewpager);
        viewPager.setAdapter(mSectionsPagerAdapter);

        // initialize navigation tabBar
        final String[] colors = getResources().getStringArray(R.array.default_preview);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(this, R.drawable.ic_bell),
                        Color.parseColor(colors[0]))
                        .title("알림 설정")
                        .badgeTitle("new")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(this, R.drawable.ic_note),
                        Color.parseColor(colors[1]))
                        .title("데일리 기록")
                        .badgeTitle("new")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(this, R.drawable.ic_drop),
                        Color.parseColor(colors[2]))
                        .title("수분 섭취량")
                        .badgeTitle("new")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(this, R.drawable.ic_chart),
                        Color.parseColor(colors[3]))
                        .title("통계 확인")
                        .badgeTitle("new")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(this, R.drawable.ic_setting),
                        Color.parseColor(colors[4]))
                        .title("환경 설정")
                        .badgeTitle("new")
                        .build()
        );

        // add navigation models
        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, pageNum);
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {
            }
        });
    }

    /**
     * show badge with delay
     * @param position
     */
    public void showBadge(final int position) {
        navigationTabBar.postDelayed(() -> {
            final NavigationTabBar.Model model = navigationTabBar.getModels().get(position);
            navigationTabBar.postDelayed(model::showBadge, 100);
        }, 500);
    }

    private void getNFCData(Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            // handle intent message
            if (rawMsgs != null) {
                NdefMessage[] messages = new NdefMessage[rawMsgs.length];

                for (int i = 0; i < rawMsgs.length; i++)
                    messages[i] = (NdefMessage) rawMsgs[i];
                byte[] payload = messages[0].getRecords()[0].getPayload();

                sqliteManager.addRecord(new String(payload));
                UpdateFragments();

                // show badge
                ((MainActivity)MainActivity.mContext).showBadge(1);
            }
        }
    }

    public void UpdateFragments() {
        mSectionsPagerAdapter.notifyDataSetChanged();
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

        // make new intent
        Intent i = new Intent(this, getClass());
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);

        // toss pending intent
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
        if(!preferenceManager.getBoolean("setWeatherAlarm", false)) {
            GregorianCalendar mCalendar = new GregorianCalendar();
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), 1200 * 1000, pendingIntent(IntentExtras.ALARM_PENDING_REQUEST_CODE));
            preferenceManager.putBoolean("setWeatherAlarm", true);
        }
    }

    private PendingIntent pendingIntent(int requestCode) {
        Intent intent = new Intent(mContext, LocalWeatherReceiver.class);
        intent.putExtra(IntentExtras.ALARM_PENDING_REQUEST, requestCode);
        PendingIntent sender = PendingIntent.getBroadcast(mContext, requestCode, intent, 0);
        return sender;
    }
}
