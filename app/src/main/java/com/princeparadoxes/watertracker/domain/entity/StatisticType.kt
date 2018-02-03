package com.princeparadoxes.watertracker.domain.entity

import com.princeparadoxes.watertracker.R

enum class StatisticType(
        val text: Int,
        val icon: Int,
        val countDays: Int
) {

    DAY(R.string.statistic_type_day, R.drawable.ic_day, 1),
    WEEK(R.string.statistic_type_week, R.drawable.ic_week, 7),
    MONTH(R.string.statistic_type_month, R.drawable.ic_month, 31),
    YEAR(R.string.statistic_type_year, R.drawable.ic_year, 365)
}
