package com.skydoves.waterdays.ui.activities.settings;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.skydoves.waterdays.R;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class NFCActivity extends AppCompatActivity {

    // NFC System
    boolean mWriteMode = false;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mNfcPendingIntent;

    @BindView(R.id.nfc_sticker)
    RippleBackground rippleBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        ButterKnife.bind(this);

        // Start animation
        rippleBackground.startRippleAnimation();
    }

    // Check NFC isEnabled
    private boolean CheckNFCEnabled()
    {
        NfcAdapter nfcAdpt = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdpt!=null) {
            if (!nfcAdpt.isEnabled())
                return false;
        }
        return true;
    }

    // NFC Write Button
    @OnClick(R.id.nfc_btn_setnfcdata)
    void NFCWrite(View v)
    {
        // Check NFC is Enabled
        if(CheckNFCEnabled()) {
            mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
            mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, NFCActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

            // Enable Tag Write Mode
            enableTagWriteMode();
            rippleBackground.setVisibility(View.VISIBLE);
        }
        else
        {
            Intent setnfc = new Intent(Settings.ACTION_NFC_SETTINGS);
            startActivity(setnfc);
            Toast.makeText(this, "NFC 기능과 Android Beam을 켜주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    // Enable Tag write Mode
    private void enableTagWriteMode() {
        mWriteMode = true;
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter[] mWriteTagFilters = new IntentFilter[] { tagDetected };
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
    }

    // Disable Tag write Mode
    private void disableTagWriteMode() {
        mWriteMode = false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO skydoves - Auto-generated method stub
        // Tag writing mode
        if (mWriteMode && NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NdefRecord record = NdefRecord.createMime("waterdays_nfc/NFC", ((TextView)findViewById(R.id.nfc_edt01)).getText().toString().getBytes());
            NdefMessage message = new NdefMessage(new NdefRecord[] { record });

            // Detected Tag
            if (writeTag(message, detectedTag)) {
                disableTagWriteMode();
                rippleBackground.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "NFC 태그에 데이터를 작성했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Write NFC Tag Data
    public boolean writeTag(NdefMessage message, Tag tag) {
        // TODO skydoves - WRITE NFCDATA TAG
        int size = message.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    Toast.makeText(getApplicationContext(), "Error: tag not writable", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    Toast.makeText(getApplicationContext(), "Error: tag too small", Toast.LENGTH_SHORT).show();
                    return false;
                }
                ndef.writeNdefMessage(message);
                return true;
            } else {
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        return true;
                    } catch (IOException e) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }
}
