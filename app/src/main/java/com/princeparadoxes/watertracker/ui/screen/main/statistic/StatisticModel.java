package com.princeparadoxes.watertracker.ui.screen.main.statistic;

import com.princeparadoxes.watertracker.data.model.StatisticType;

/**
 * Created by as3co on 17.05.2017.
 */

public class StatisticModel {
    private StatisticType mStatisticType;
    private float mValue;

    public StatisticModel(StatisticType mStatisticType, float mValue) {
        this.mStatisticType = mStatisticType;
        this.mValue = mValue;
    }

    public StatisticType getmStatisticType() {
        return mStatisticType;
    }

    public float getValue() {
        return mValue;
    }
}
