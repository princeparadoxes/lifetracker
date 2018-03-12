package com.princeparadoxes.watertracker.presentation.screen.statistic

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.princeparadoxes.watertracker.R

class StatisticChartView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val chartView by lazy { findViewById(R.id.statistic_cart_view_chart) as BarChart }

    private val accentColor by lazy { ContextCompat.getColor(context, R.color.accent) }
    private val accentAlphaColor by lazy { ContextCompat.getColor(context, R.color.accent_alpha) }

    init {
        LayoutInflater.from(context).inflate(R.layout.statistic_chart_view, this)
        chartView.description = null
        chartView.legend.isEnabled = false
        chartView.xAxis.apply {
            setDrawGridLines(false)
            textColor = Color.TRANSPARENT
            axisLineColor = Color.TRANSPARENT
        }
        chartView.axisLeft.apply {
            setDrawGridLines(false)
            textColor = Color.WHITE
            axisMaximum = 2000F
            axisMinimum = 0F
            axisLineColor = Color.TRANSPARENT
        }
        chartView.axisRight.apply {
            setDrawGridLines(false)
            textColor = Color.TRANSPARENT
            axisMaximum = 2000F
            axisMinimum = 0F
            axisLineColor = Color.TRANSPARENT
        }
    }

    fun bindView(xValues: FloatArray, yValues: FloatArray) {
        if (xValues.size != yValues.size) {
            throw IllegalArgumentException("xValues length must be equals yValues length")
        }
        val entries = xValues.indices.map { BarEntry(xValues[it], yValues[it]) }
        val dataSet = BarDataSet(entries, "")
        dataSet.setDrawIcons(false)
        dataSet.valueTextColor = Color.TRANSPARENT
        dataSet.color = Color.WHITE

        val data = BarData(dataSet)
        data.isHighlightEnabled = false
        chartView.data = data
        chartView.invalidate()
    }

}
