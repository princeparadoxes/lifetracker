package com.princeparadoxes.watertracker.domain.entity

import com.princeparadoxes.watertracker.R
import java.util.*

enum class StatisticType(
        val text: Int,
        val icon: Int,
        val countDays: Int,
        var calendarField: Int
) {

    DAY(R.string.statistic_type_day, R.drawable.ic_day, 1, Calendar.DAY_OF_WEEK),
    WEEK(R.string.statistic_type_week, R.drawable.ic_week, 7, Calendar.DAY_OF_WEEK),
    MONTH(R.string.statistic_type_month, R.drawable.ic_month, 31, Calendar.DAY_OF_MONTH),
    YEAR(R.string.statistic_type_year, R.drawable.ic_year, 365, Calendar.DAY_OF_YEAR)
}
