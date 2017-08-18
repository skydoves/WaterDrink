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

    protected @BindView(R.id.nfc_sticker) RippleBackground rippleBackground;

    boolean mWriteMode = false;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mNfcPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        ButterKnife.bind(this);

        rippleBackground.startRippleAnimation();
    }

    private boolean CheckNFCEnabled() {
        NfcAdapter nfcAdpt = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdpt!=null) {
            if (!nfcAdpt.isEnabled())
                return false;
        }
        return true;
    }

    @OnClick(R.id.nfc_btn_setnfcdata)
    void NFCWrite(View v) {
        if(CheckNFCEnabled()) {
            mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
            mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, NFCActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            enableTagWriteMode();
            rippleBackground.setVisibility(View.VISIBLE);
        } else {
            Intent setnfc = new Intent(Settings.ACTION_NFC_SETTINGS);
            startActivity(setnfc);
            Toast.makeText(this, getString(R.string.msg_require_nfc), Toast.LENGTH_SHORT).show();
        }
    }

    private void enableTagWriteMode() {
        mWriteMode = true;
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter[] mWriteTagFilters = new IntentFilter[] { tagDetected };
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
    }

    private void disableTagWriteMode() {
        mWriteMode = false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (mWriteMode && NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NdefRecord record = NdefRecord.createMime("waterdays_nfc/NFC", ((TextView)findViewById(R.id.nfc_edt01)).getText().toString().getBytes());
            NdefMessage message = new NdefMessage(new NdefRecord[] { record });

            // detected tag
            if (writeTag(message, detectedTag)) {
                disableTagWriteMode();
                rippleBackground.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "NFC 태그에 데이터를 작성했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean writeTag(NdefMessage message, Tag tag) {
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