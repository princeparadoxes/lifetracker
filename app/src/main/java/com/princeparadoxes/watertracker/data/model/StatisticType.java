package com.princeparadoxes.watertracker.data.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.princeparadoxes.watertracker.R;

public enum StatisticType {

    DAY(R.string.statistic_type_day, R.drawable.ic_day, 1),
    WEEK(R.string.statistic_type_week, R.drawable.ic_week, 7),
    MONTH(R.string.statistic_type_month, R.drawable.ic_month, 31),
    YEAR(R.string.statistic_type_year, R.drawable.ic_year, 365);

    @StringRes
    private final int mName;
    @DrawableRes
    private final int mIcon;
    private final int mCountDays;

    StatisticType(@StringRes int name, @DrawableRes int icon, int countDays) {
        mName = name;
        mIcon = icon;
        mCountDays = countDays;
    }

    public int getName() {
        return mName;
    }

    public int getIcon() {
        return mIcon;
    }

    public int getCountDays() {
        return mCountDays;
    }
}
