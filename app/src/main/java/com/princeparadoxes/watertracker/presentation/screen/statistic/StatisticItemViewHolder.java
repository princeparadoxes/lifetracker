package com.princeparadoxes.watertracker.presentation.screen.statistic;

import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
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
import com.princeparadoxes.watertracker.R;
import com.princeparadoxes.watertracker.domain.entity.StatisticModel;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StatisticItemViewHolder extends BindableViewHolder<StatisticModel,
        StatisticItemViewHolder.StatisticTypeItemListener> implements OnChartValueSelectedListener {

    //    @BindView(R.id.statistic_type_item_container)
//    ViewGroup mContainer;
    @BindView(R.id.statistic_type_item_chart)
    PieChart mPieChart;
    @BindView(R.id.statistic_type_item_name)
    TextView mTextView;
    @BindView(R.id.statistic_type_item_logo)
    ImageView mLogo;

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
        // disable rotation of the chart by touch
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
        mTextView.setText(item.getStatisticType().getText());
        setChartData(item);
        mLogo.setImageResource(item.getStatisticType().getIcon());
    }

    private void setChartData(StatisticModel statisticModel) {
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        if (statisticModel.getValue() >= statisticModel.getNormValue()) {
            entries.add(new PieEntry(statisticModel.getNormValue(), "", null));
            entries.add(new PieEntry(0, "", null));
        } else {
            entries.add(new PieEntry(statisticModel.getValue(), "", null));
            entries.add(new PieEntry(statisticModel.getNormValue() - statisticModel.getValue(), "", null));
        }

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
    }

    public void onChanged() {
        ViewCompat.animate(mLogo).alpha(0.0f).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        ViewCompat.animate(mPieChart).alpha(1.0f).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        ViewCompat.animate(mTextView).alpha(1.0f).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        mPieChart.invalidate();
        mPieChart.animateY(600, Easing.EasingOption.EaseInOutQuad);
    }

    public void onUnchanged() {
        ViewCompat.animate(mLogo).alpha(1.0f).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        ViewCompat.animate(mPieChart).alpha(0.0f).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        ViewCompat.animate(mTextView).alpha(0.0f).setInterpolator(new AccelerateDecelerateInterpolator()).start();
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