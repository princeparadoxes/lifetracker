package com.princeparadoxes.watertracker.domain.entity

data class StatisticModel(
        val statisticType: StatisticType,
        val value: Float,
        val normValue: Float
)
