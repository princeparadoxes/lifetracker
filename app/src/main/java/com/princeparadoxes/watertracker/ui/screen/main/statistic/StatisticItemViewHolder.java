package com.princeparadoxes.watertracker.ui.screen.main.statistic;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.danil.recyclerbindableadapter.library.view.BindableViewHolder;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.princeparadoxes.watertracker.R;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StatisticItemViewHolder extends BindableViewHolder<StatisticModel,
        StatisticItemViewHolder.StatisticTypeItemListener> implements OnChartValueSelectedListener {

    @BindView(R.id.statistic_type_item_chart)
    PieChart mPieChart;
    @BindView(R.id.statistic_type_item_name)
    TextView mTextView;

    @BindColor(R.color.accent)
    int mAccentColor;
    @BindColor(R.color.accent_alpha)
    int mAccentAlphaColor;

    public StatisticItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        initChart();
    }

    private void initChart() {
        mPieChart.setRotationAngle(0);
        // center hole
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);
        mPieChart.setHoleRadius(58f);
        mPieChart.setTransparentCircleRadius(61f);
        mPieChart.setDrawCenterText(false);
        // enable rotation of the chart by touch
        mPieChart.setRotationEnabled(false);
        mPieChart.setHighlightPerTapEnabled(true);
        mPieChart.setDrawEntryLabels(false);
        // add a selection listener
        mPieChart.setOnChartValueSelectedListener(this);
        // Hide the description
        mPieChart.setDescription(null);
        // Hide the legend
        mPieChart.getLegend().setEnabled(false);
    }

    @Override
    public void bindView(int position, StatisticModel item, StatisticTypeItemListener actionListener) {
        super.bindView(position, item, actionListener);
        mTextView.setText(item.getmStatisticType().getName());
        setChartData(item);
        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    }

    private void setChartData(StatisticModel statisticModel) {
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        float a = 100;
        float b = statisticModel.getValue();

        entries.add(new PieEntry(a, "", null));
        entries.add(new PieEntry(b, "", null));

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(1f);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(mAccentColor);
        colors.add(mAccentAlphaColor);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueTextColor(Color.TRANSPARENT);
        mPieChart.setData(data);
        mPieChart.invalidate();
    }

    public void showText() {
        int parentHeight = ((View) mPieChart.getParent()).getHeight();
        float scale = (parentHeight - mTextView.getHeight()) / (float) mPieChart.getHeight();
        mPieChart.setPivotX(mPieChart.getWidth() * 0.5f);
        mPieChart.setPivotY(0);
        mPieChart.animate().scaleX(scale)
                .withEndAction(() -> {
                    mTextView.setVisibility(View.VISIBLE);
                })
                .scaleY(scale).setDuration(200)
                .start();
    }

    public void hideText() {
        mTextView.setVisibility(View.INVISIBLE);
        mPieChart.animate().scaleX(1f).scaleY(1f)
                .setDuration(200)
                .start();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    public interface StatisticTypeItemListener extends BindableViewHolder.ActionListener<StatisticModel> {
    }
}