package com.princeparadoxes.watertracker.domain.interactor

import com.princeparadoxes.watertracker.domain.entity.Drink
import com.princeparadoxes.watertracker.domain.entity.StatisticType
import com.princeparadoxes.watertracker.presentation.screen.statistic.StatisticModel
import io.reactivex.Observable

interface DrinkOutputGateway {

    fun addWater(ml: Int): Observable<Drink>
    fun getDaySum(): Observable<Int>
    fun getLast(): Observable<Drink>
    fun getStatisticByPeriod(statisticType: StatisticType): Observable<StatisticModel>

}