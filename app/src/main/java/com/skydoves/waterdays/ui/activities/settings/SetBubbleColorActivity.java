package com.skydoves.waterdays.ui.activities.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.waterdays.R;
import com.skydoves.waterdays.WDApplication;
import com.skydoves.waterdays.persistence.preference.PreferenceKeys;
import com.skydoves.waterdays.persistence.preference.PreferenceManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetBubbleColorActivity extends AppCompatActivity {

    protected @Inject PreferenceManager preferenceManager;

    protected @BindView(R.id.colorPickerView) ColorPickerView colorPickerView;
    protected @BindView(R.id.bubble) ImageView bubble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_bubble_color);
        ButterKnife.bind(this);
        WDApplication.getComponent().inject(this);

        colorPickerView.setColorListener(colorListener);
    }

    ColorPickerView.ColorListener colorListener = color -> bubble.setColorFilter(color);

    @OnClick(R.id.confirm)
    public void setColorConfirm(View v) {
        preferenceManager.putString(PreferenceKeys.BUBBLE_COLOR.first, "#" + colorPickerView.getColorHtml());
        Toast.makeText(getBaseContext(), "물방울의 색상을 변경하였습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
