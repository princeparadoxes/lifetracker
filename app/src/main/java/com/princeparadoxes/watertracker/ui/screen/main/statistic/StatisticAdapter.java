package com.princeparadoxes.watertracker.ui.screen.main.statistic;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.princeparadoxes.watertracker.R;

import java.util.ArrayList;
import java.util.List;

public class StatisticAdapter extends RecyclerView.Adapter<StatisticItemViewHolder> {

    private List<StatisticModel> data = new ArrayList<>();

    public StatisticAdapter() {
    }

    @Override
    public StatisticItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.statistic_type_view, parent, false);
        return new StatisticItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StatisticItemViewHolder holder, int position) {
        holder.bindView(position, data.get(position), null);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public StatisticAdapter setData(List<StatisticModel> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
        return this;
    }
}
