package com.princeparadoxes.watertracker.ui.screen.main.statistic;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.princeparadoxes.watertracker.R;

import java.util.List;

public class StatisticTypeAdapter extends RecyclerView.Adapter<StatisticTypeViewHolder> {

    private RecyclerView parentRecycler;
    private List<StatisticModel> data;

    public StatisticTypeAdapter(List<StatisticModel> data) {
        this.data = data;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        parentRecycler = recyclerView;
    }

    @Override
    public StatisticTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.statistic_type_view, parent, false);
        return new StatisticTypeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StatisticTypeViewHolder holder, int position) {
        holder.bindView(position, data.get(position), null);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class TintOnLoad implements RequestListener<Integer, GlideDrawable> {

        private ImageView imageView;
        private int tintColor;

        public TintOnLoad(ImageView view, int tintColor) {
            this.imageView = view;
            this.tintColor = tintColor;
        }

        @Override
        public boolean onException(Exception e, Integer model, Target<GlideDrawable> target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, Integer model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            imageView.setColorFilter(tintColor);
            return false;
        }
    }
}
