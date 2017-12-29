package com.princeparadoxes.watertracker.ui.screen.main.statistic;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.princeparadoxes.watertracker.R;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StatisticChartView extends LinearLayout {

    @BindView(R.id.statistic_cart_view_chart)
    LineChart mChartView;

    @BindColor(R.color.accent)
    int mAccentColor;
    @BindColor(R.color.accent_alpha)
    int mAccentAlphaColor;

    public StatisticChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.statistic_chart_view, this);
        ButterKnife.bind(this);
        // add a selection listener
        mChartView.setDescription(null);
        // Hide the legend
        mChartView.getLegend().setEnabled(false);
        mChartView.getAxisLeft().setDrawGridLines(false);
        mChartView.getAxisRight().setDrawGridLines(false);
        mChartView.getXAxis().setDrawGridLines(false);

    }

    public void bindView(@NonNull float[] xValues, @NonNull float[] yValues) {
        if (xValues.length != yValues.length) {
            throw new IllegalArgumentException("xValues length must be equals yValues length");
        }
        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < xValues.length; i++) {
            entries.add(new Entry(xValues[i], yValues[i]));
        }
        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setDrawIcons(false);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setColor(Color.WHITE);
        dataSet.setCircleColor(Color.WHITE);

        LineData data = new LineData(dataSet);
        mChartView.setData(data);
        mChartView.invalidate();
    }

}
