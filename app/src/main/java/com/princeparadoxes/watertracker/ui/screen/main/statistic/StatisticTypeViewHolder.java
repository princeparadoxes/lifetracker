package com.princeparadoxes.watertracker.ui.screen.main.statistic;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.danil.recyclerbindableadapter.library.view.BindableViewHolder;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.princeparadoxes.watertracker.R;
import com.princeparadoxes.watertracker.data.db.repository.DBDrinkRepository;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatisticTypeViewHolder extends BindableViewHolder<StatisticModel,
        StatisticTypeViewHolder.StatisticTypeItemListener> implements OnChartValueSelectedListener {

    @BindView(R.id.statistic_type_item_chart)
    PieChart mPieChart;
    @BindView(R.id.statistic_type_item_name)
    TextView mTextView;

    protected String[] mParties = new String[]{
            "Party A", "Party B"
    };

    public StatisticTypeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        initChart();
    }

    private void initChart() {
//        mPieChart.setUsePercentValues(false);
//        mPieChart.getDescription().setEnabled(false);
//        mPieChart.setDragDecelerationFrictionCoef(0.95f);

        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);

//        mPieChart.setTransparentCircleColor(Color.WHITE);
//        mPieChart.setTransparentCircleAlpha(110);

        mPieChart.setHoleRadius(58f);
        mPieChart.setTransparentCircleRadius(61f);

        mPieChart.setDrawCenterText(false);

        mPieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mPieChart.setRotationEnabled(false);
        mPieChart.setHighlightPerTapEnabled(true);
        mPieChart.setDrawEntryLabels(false);

        // add a selection listener
        mPieChart.setOnChartValueSelectedListener(this);

        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mPieChart.spin(2000, 0, 360);

        // entry label styling
//        mPieChart.setEntryLabelColor(Color.WHITE);
//        mPieChart.setEntryLabelTextSize(12f);
    }

    @Override
    public void bindView(int position, StatisticModel item, StatisticTypeItemListener actionListener) {
        super.bindView(position, item, actionListener);
//        int iconTint = ContextCompat.getColor(itemView.getContext(), android.R.color.darker_gray);
//        Glide.with(itemView.getContext())
//                .load(item.getIcon())
//                .listener(new StatisticTypeAdapter.TintOnLoad(imageView, iconTint))
//                .into(imageView);
        mTextView.setText(item.getmStatisticType().getName());

        setData( item);
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

    private void setData(StatisticModel statisticModel) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.

        /*
        for (int i = 0; i < 2; i++) {
            entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5),
                    mParties[i % mParties.length],null));
        }
         */

        float a = 100;
        float b = statisticModel.getmValue();

        entries.add(new PieEntry((float) a,
                mParties[0 % mParties.length],null));

        entries.add(new PieEntry((float) b,
                mParties[1 % mParties.length],null));

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
//        data.setValueFormatter(new PercentFormatter());
//        data.setValueTextSize(0f);
        data.setValueTextColor(Color.TRANSPARENT);
//        data.setValueTypeface(mTfLight);
        mPieChart.setData(data);

        // undo all highlights
//        mPieChart.highlightValues(null);

        mPieChart.invalidate();
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
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