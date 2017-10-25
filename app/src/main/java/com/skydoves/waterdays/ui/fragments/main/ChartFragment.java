package com.skydoves.waterdays.ui.fragments.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.skydoves.waterdays.R;
import com.skydoves.waterdays.WDApplication;
import com.skydoves.waterdays.persistence.sqlite.SqliteManager;
import com.skydoves.waterdays.ui.customViews.MyMarkerView;
import com.skydoves.waterdays.utils.DateUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by skydoves on 2016-10-15.
 * Updated by skydoves on 2017-08-17.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class ChartFragment extends Fragment implements OnChartValueSelectedListener {

    protected @Inject SqliteManager sqliteManager;

    protected @BindView(R.id.chart_mainchart) LineChart lineChart;

    private View rootView;
    private int dateCount = 0;

    public ChartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_chart, container, false);
        WDApplication.getComponent().inject(this);
        ButterKnife.bind(this, rootView);
        this.rootView = rootView;
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // set dateCount
        dateCount = -DateUtils.getDateDay(DateUtils.getFarDay(0), DateUtils.getDateFormat());
        initializeChart(DateUtils.getDateDay(DateUtils.getFarDay(0), DateUtils.getDateFormat()));
    }

    @OnClick({R.id.chart_ibtn_back, R.id.chart_ibtn_next})
    void DateMoveButton(View v) {
        int DayNum = DateUtils.getDateDay(DateUtils.getFarDay(0), DateUtils.getDateFormat());
        switch (v.getId()){
            case R.id.chart_ibtn_back :
                dateCount -= 7;
                initializeChart(6);
                break;

            case R.id.chart_ibtn_next :
                if(dateCount == -DayNum)
                    Toast.makeText(getContext(), "다음주의 기록은 볼 수 없습니다.", Toast.LENGTH_SHORT).show();
                else {
                    dateCount += 7;
                    if(dateCount == -DayNum)
                       initializeChart(DayNum);
                    else
                       initializeChart(6);
                }
                break;
        }
    }

    /**
     * initialize chartView
     * @param dayCount chart
     */
    private void initializeChart(int dayCount) {
        float TotalAmount=0, Max=0, sumCount=0;
        ArrayList<Entry> entries = new ArrayList<>();
        for(int i=0; i<=dayCount; i++) {
            int daySum = sqliteManager.getDayDrinkAmount(DateUtils.getFarDay(dateCount+i));

            // get total sum
            TotalAmount += daySum;

            // get max
            if(i == 0) Max = daySum;
            else if(Max < daySum) Max = daySum;

            // count
            if(daySum != 0)
                sumCount++;

            // add entry
            entries.add(new Entry(daySum, i));
        }

        TextView tv_date = (TextView)rootView.findViewById(R.id.chart_tv_weekdate);
        tv_date.setText(DateUtils.getFarDay(dateCount) + " ~ " + DateUtils.getFarDay(dateCount+6));

        TextView tv_total = (TextView)rootView.findViewById(R.id.chart_tv_totaldrink);
        tv_total.setText(String.format("%.1f", TotalAmount/1000) + " L");

        TextView tv_average = (TextView)rootView.findViewById(R.id.chart_tv_averagedrink);
        if(sumCount != 0)
            tv_average.setText(String.format("%.1f", TotalAmount/1000/(sumCount)) + " L");
        else
            tv_average.setText(String.format("%.1f", TotalAmount/1000) + " L");

        ArrayList<String> labels = new ArrayList<>();
        labels.add("일");
        labels.add("월");
        labels.add("화");
        labels.add("수");
        labels.add("목");
        labels.add("금");
        labels.add("토");

        LineDataSet dataset = new LineDataSet(entries, "마신 물의 양");
        LineData data = new LineData(labels, dataset);
        lineChart.setData(data);
        lineChart.setOnChartValueSelectedListener(this);

        int[] computed = {(Color.TRANSPARENT)};
        String[] label = {("")};
        lineChart.setDescription("");
        lineChart.setDescriptionTextSize(16f);
        lineChart.setDescriptionColor(Color.WHITE);
        lineChart.getLegend().setEnabled(true);
        lineChart.getLegend().setWordWrapEnabled(true);
        lineChart.getLegend().setTextColor(Color.TRANSPARENT);
        lineChart.getLegend().setCustom(computed, label);

        lineChart.setDrawGridBackground(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisRight().setTextColor(Color.TRANSPARENT);
        lineChart.getXAxis().setDrawGridLines(false);

        lineChart.setPinchZoom(false);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.animateY(1700);

        MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);
        lineChart.setMarkerView(mv);

        // X - axis settings
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextSize(14f);
        xAxis.setSpaceBetweenLabels(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(ColorTemplate.getHoloBlue());

        // Y - axis settings
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setTextSize(14f);
        leftAxis.setStartAtZero(true);
        leftAxis.setSpaceTop(45);
        leftAxis.setValueFormatter(new YAxisValueFormatter());

        // set max Y-Axis & chart message
        TextView tv_chartMessage = (TextView)rootView.findViewById(R.id.chart_tv_message);
        if(TotalAmount > 0)
            tv_chartMessage.setVisibility(View.INVISIBLE);
        else
            tv_chartMessage.setVisibility(View.VISIBLE);

        // dataSet settings
        dataset.setDrawFilled(true);
        dataset.setCircleSize(5f);
        dataset.setValueTextSize(13f);
        dataset.setValueTextColor(Color.WHITE);
        dataset.enableDashedHighlightLine(10f, 5f, 0f);
        dataset.setValueFormatter(new DataSetValueFormatter());
    }

    /**
     * YAxis : Water Y-Value Formatter
     */
    private class YAxisValueFormatter implements com.github.mikephil.charting.formatter.YAxisValueFormatter {
        @Override
        public String getFormattedValue(float value, YAxis yAxis) {
            return Math.round(value)+"ml";
        }
    }

    /**
     * Water DataSet-Value Formatter
     */
    private class DataSetValueFormatter implements ValueFormatter {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return Math.round(value)+"";
        }
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        TextView tv_sdTitle = (TextView)rootView.findViewById(R.id.chart_tv03);
        String dName = DateUtils.getIndexOfDayName(e.getXIndex());
        tv_sdTitle.setText(dName);

        TextView tv_selectedday = (TextView)rootView.findViewById(R.id.chart_tv_selectedday);
        tv_selectedday.setText(String.format("%.0f", e.getVal()) + " ml");
    }

    @Override
    public void onNothingSelected() {

    }
}