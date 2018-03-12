package com.princeparadoxes.watertracker.presentation.screen.statistic

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.princeparadoxes.watertracker.R
import com.princeparadoxes.watertracker.domain.entity.StatisticModel
import com.princeparadoxes.watertracker.utils.toCalendar

import java.util.ArrayList

class StatisticAdapter : RecyclerView.Adapter<StatisticItemViewHolder>() {

    private val data = ArrayList<StatisticModel>()
    private lateinit var inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticItemViewHolder {
        if (!this::inflater.isInitialized) inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.statistic_type_view, parent, false)
        return StatisticItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatisticItemViewHolder, position: Int) {
        val w = 3L;
        w.toCalendar()
        holder.bindView(position, data[position], null)
    }

    override fun getItemCount(): Int = data.size

    fun getItem(position: Int) = data[position]

    fun setData(data: List<StatisticModel>): StatisticAdapter {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
        return this
    }
}
