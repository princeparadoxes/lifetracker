package com.princeparadoxes.watertracker.presentation.screen.statistic;

import com.princeparadoxes.watertracker.domain.entity.StatisticType;

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

    public int getName() {
        return getStatisticType().getText();
    }

    public int getIcon() {
        return getStatisticType().getIcon();
    }

    public int getCountDays() {
        return getStatisticType().getCountDays();
    }

    @Override
    public String toString() {
        return "StatisticModel{" +
                "mStatisticType=" + mStatisticType +
                ", mValue=" + mValue +
                ", mNormValue=" + mNormValue +
                '}';
    }
}
