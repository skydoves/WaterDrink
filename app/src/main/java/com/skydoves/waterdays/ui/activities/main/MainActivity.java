package com.skydoves.waterdays.ui.activities.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.gigamole.navigationtabbar.ntb.NavigationTabBar;
import com.skydoves.waterdays.R;
import com.skydoves.waterdays.services.receivers.AlarmBootReceiver;
import com.skydoves.waterdays.services.receivers.LocalWeatherReceiver;
import com.skydoves.waterdays.persistence.preference.PreferenceManager;
import com.skydoves.waterdays.persistence.sqlite.SqliteManager;
import com.skydoves.waterdays.ui.fragments.main.AlarmFragment;
import com.skydoves.waterdays.ui.fragments.main.ChartFragment;
import com.skydoves.waterdays.ui.fragments.main.DailyFragment;
import com.skydoves.waterdays.ui.fragments.main.EnvironmentFragment;
import com.skydoves.waterdays.ui.fragments.main.MainWaterFragment;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class MainActivity extends AppCompatActivity  {

    // Context & Systems
    static public Context mContext;
    private PreferenceManager systems;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private NavigationTabBar navigationTabBar;

    // NFC System //
    private NfcAdapter nAdapter;
    private PendingIntent pIntent;
    private IntentFilter[] filters;

    // Alarm System
    private GregorianCalendar mCalendar;
    private AlarmManager mManager;
    private final int requestC = 99000000;

    // DB
    private final String TAG = "MainActivity";
    private SQLiteDatabase db;
    private SqliteManager sqliteManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Context & System
        mContext = this;
        systems = new PreferenceManager(this);

        // Initialize ViewPager
        Initialize(2);

        // Initialize DB
        sqliteManager = new SqliteManager(this, SqliteManager.DATABASE_NAME, null, SqliteManager.DATABASE_VERSION);

        // Check NFC Available
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "이 앱은 NFC 기능을 탑재한 디바이스에서만 작동합니다.", Toast.LENGTH_LONG).show();
            finish();
        }

        // Set NFC Adapter
        nAdapter = NfcAdapter.getDefaultAdapter(this);
        getNFCData(getIntent());

        // Auto Weather Alarm
        WeatherAlarm();

        // Set Boot Receiver
        ComponentName receiver = new ComponentName(this, AlarmBootReceiver.class);
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    /*===================================================
                         Initialize
     ===================================================*/
    //region # Initialize ViewPager Fragment & Navigation TabBar
    private void Initialize(final int pageNum) {
        // TODO skydoves - MainActivity Init : Auto-generated method stub
        // Initialize ViewPager Fragment
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        final ViewPager viewPager = (ViewPager) findViewById(R.id.mainactivity_viewpager);
        viewPager.setAdapter(mSectionsPagerAdapter);

        // Initialize Navigation TabBar
        final String[] colors = getResources().getStringArray(R.array.default_preview);
        navigationTabBar = (NavigationTabBar) findViewById(R.id.mainactivity_navi);
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

        // Set Navigation TabBar
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

    // Show Badge
    public void ShowBadge(final int position)
    {
        // Show Badge
        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                final NavigationTabBar.Model model = navigationTabBar.getModels().get(position);
                navigationTabBar.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        model.showBadge();
                    }
                }, 100);
            }
        }, 500);
    }

    //endregion


    /*===================================================
                      Fragment Setting
     ===================================================*/
    //region # Fragment Setting
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new MainWaterFragment();
            switch (position) {
                case 0:
                    fragment = new AlarmFragment();
                    break;
                case 1:
                    fragment = new DailyFragment();
                    break;
                case 2:
                    fragment = new MainWaterFragment();
                    break;
                case 3:
                    fragment = new ChartFragment();
                    break;
                case 4:
                    fragment = new EnvironmentFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public int getItemPosition(Object item) {
            return POSITION_NONE;
        }
    }

    // Update Fragment
    public void UpdateFragments()
    {
        mSectionsPagerAdapter.notifyDataSetChanged();
    }
    //endregion

    /*===================================================
                          NFC System
     ===================================================*/
    //region
    // get NFC Message from Tag
    private void getNFCData(Intent intent) {
        // TODO skydoves - Get NFC DATA : Auto-generated method
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            // handle intent Message
            if (rawMsgs != null) {
                NdefMessage[] messages = new NdefMessage[rawMsgs.length];

                for (int i = 0; i < rawMsgs.length; i++)
                    messages[i] = (NdefMessage) rawMsgs[i];
                byte[] payload = messages[0].getRecords()[0].getPayload();

                // add a record
                sqliteManager.addRecord(new String(payload));
                // Notify Data Change
                UpdateFragments();
                // Show Bade
                ((MainActivity) MainActivity.mContext).ShowBadge(1);
            }
        }
    }

    // onResume set Intent-Filter
    @Override
    protected void onResume() {
        // TODO skydoves - Auto-generated method stub
        super.onResume();
        // New Intent Filter
        IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            filter.addDataType("waterdays_nfc/*");
        } catch (Exception e) {

        }

        // New Intent - FLAG_ACTIVITY_SINGLE_TOP
        Intent i = new Intent(this, getClass());
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pIntent = PendingIntent.getActivity(this, 0, i, 0);

        // Toss Intent
        filters = new IntentFilter[] { filter, };
        nAdapter.enableForegroundDispatch(this, pIntent, filters, null);
    }

    // onNewIntent - get P-I
    @Override
    protected void onNewIntent(Intent intent) {
        // TODO skydoves - Auto-generated method stub
        super.onNewIntent(intent);
        setIntent(intent);
        getNFCData(getIntent());
    }
    //endregion

     /*===================================================
              Auto Weather & Recommendation Alarm
     ===================================================*/
    //region
    // Set Auto Weather Alarm
    private void WeatherAlarm()
    {
        // Set Next Alarm
        if(!systems.getBoolean("setWeatherAlarm", false)) {
            mCalendar = new GregorianCalendar();
            mManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            mManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), 1200 * 1000, pendingIntent(requestC));
            systems.putBoolean("setWeatherAlarm", true);
        }
    }

    // # Pending Intent # //
    private PendingIntent pendingIntent(int requestcode) {
        // TODO skydoves - PendingIntent with Handling requestcode
        Intent intent = new Intent(mContext, LocalWeatherReceiver.class);
        intent.putExtra("requestcode", requestcode);
        PendingIntent sender = PendingIntent.getBroadcast(mContext, requestcode, intent, 0);
        return sender;
    }
    //endregion

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Close DB
        if(db != null)
          db.close();
    }
}
