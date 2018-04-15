package com.princeparadoxes.watertracker.presentation.screen.statistic

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import com.danil.recyclerbindableadapter.library.view.BindableViewHolder
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.princeparadoxes.watertracker.R
import com.princeparadoxes.watertracker.domain.entity.StatisticModel
import java.util.*

class StatisticItemViewHolder(itemView: View) : BindableViewHolder<StatisticModel, StatisticItemViewHolder.StatisticTypeItemListener>(itemView), OnChartValueSelectedListener {

    private val mPieChart by lazy { itemView.findViewById(R.id.statistic_type_item_chart) as PieChart }
    private val mTextView by lazy { itemView.findViewById(R.id.statistic_type_item_name) as TextView }
    private val mLogo by lazy { itemView.findViewById(R.id.statistic_type_item_logo) as ImageView }

    private val accentColor by lazy { ContextCompat.getColor(itemView.context, R.color.accent) }
    private val accentAlphaColor by lazy { ContextCompat.getColor(itemView.context, R.color.accent_alpha) }

    init {
        initChart()
    }

    private fun initChart() {
        mPieChart.rotationAngle = 0f
        // center hole
        mPieChart.isDrawHoleEnabled = true
        mPieChart.setHoleColor(Color.WHITE)
        mPieChart.holeRadius = 58f
        mPieChart.transparentCircleRadius = 61f
        mPieChart.setDrawCenterText(false)
        // disable rotation of the chart by touch
        mPieChart.isRotationEnabled = false
        mPieChart.isHighlightPerTapEnabled = true
        mPieChart.setDrawEntryLabels(false)
        // add a selection listener
        mPieChart.setOnChartValueSelectedListener(this)
        // Hide the description
        mPieChart.description = null
        // Hide the legend
        mPieChart.legend.isEnabled = false
    }

    override fun bindView(position: Int, item: StatisticModel, actionListener: StatisticTypeItemListener?) {
        super.bindView(position, item, actionListener)
        mTextView.setText(item.statisticType.text)
        setChartData(item)
        mLogo.setImageResource(item.statisticType.icon)
    }

    private fun setChartData(statisticModel: StatisticModel) {
        val entries = ArrayList<PieEntry>()

        if (statisticModel.value >= statisticModel.normValue) {
            entries.add(PieEntry(statisticModel.normValue, "", null))
            entries.add(PieEntry(0f, "", null))
        } else {
            entries.add(PieEntry(statisticModel.value, "", null))
            entries.add(PieEntry(statisticModel.normValue - statisticModel.value, "", null))
        }

        val dataSet = PieDataSet(entries, "")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 1f

        val colors = ArrayList<Int>()
        colors.add(accentColor)
        colors.add(accentAlphaColor)
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueTextColor(Color.TRANSPARENT)
        mPieChart.data = data
    }

    fun onChanged() {
        ViewCompat.animate(mLogo).alpha(0.0f).setInterpolator(AccelerateDecelerateInterpolator()).start()
        ViewCompat.animate(mPieChart).alpha(1.0f).setInterpolator(AccelerateDecelerateInterpolator()).start()
        ViewCompat.animate(mTextView).alpha(1.0f).setInterpolator(AccelerateDecelerateInterpolator()).start()
        mPieChart.invalidate()
        mPieChart.animateY(600, Easing.EasingOption.EaseInOutQuad)
    }

    fun onUnchanged() {
        ViewCompat.animate(mLogo).alpha(1.0f).setInterpolator(AccelerateDecelerateInterpolator()).start()
        ViewCompat.animate(mPieChart).alpha(0.0f).setInterpolator(AccelerateDecelerateInterpolator()).start()
        ViewCompat.animate(mTextView).alpha(0.0f).setInterpolator(AccelerateDecelerateInterpolator()).start()
    }

    override fun onValueSelected(e: Entry, h: Highlight) {

    }

    override fun onNothingSelected() {

    }

    interface StatisticTypeItemListener : BindableViewHolder.ActionListener<StatisticModel>
}