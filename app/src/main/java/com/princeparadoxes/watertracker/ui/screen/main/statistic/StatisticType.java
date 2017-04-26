package com.princeparadoxes.watertracker.ui.screen.main.statistic;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.princeparadoxes.watertracker.R;

public enum StatisticType {

    DAY(R.string.statistic_type_day, R.drawable.ball),
    WEEK(R.string.statistic_type_week, R.drawable.ball),
    MONTH(R.string.statistic_type_month, R.drawable.ball),
    YEAR(R.string.statistic_type_year, R.drawable.ball);

    @StringRes
    private final int mName;
    @DrawableRes
    private final int mIcon;

    StatisticType(@StringRes int name, @DrawableRes int icon) {
        mName = name;
        mIcon = icon;
    }

    public int getName() {
        return mName;
    }

    public int getIcon() {
        return mIcon;
    }
}
