package com.princeparadoxes.watertracker.ui.screen.main.statistic;

import com.princeparadoxes.watertracker.data.model.StatisticType;

/**
 * Created by as3co on 17.05.2017.
 */

public class StatisticModel {
    private StatisticType mStatisticType;
    private float mValue;
    private float mNormValue;

    public StatisticModel(StatisticType statisticType, float value, float normValue) {
        mStatisticType = statisticType;
        mValue = value;
        mNormValue = normValue;
    }

    public StatisticType getStatisticType() {
        return mStatisticType;
    }

    public float getValue() {
        return mValue;
    }

    public float getNormValue() {
        return mNormValue;
    }
}
