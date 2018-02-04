package com.princeparadoxes.watertracker.domain.interactor

import com.princeparadoxes.watertracker.domain.entity.Drink
import com.princeparadoxes.watertracker.domain.entity.StatisticType
import com.princeparadoxes.watertracker.domain.entity.StatisticModel
import io.reactivex.Observable

interface DrinkInputGateway {

    fun addWater(ml: Int): Observable<Drink>
    fun getDaySum(): Observable<Int>
    fun getLast(): Observable<Drink>
    fun getStatisticByPeriod(statisticType: StatisticType): Observable<StatisticModel>
    fun getDrinksByPeriod(statisticType: StatisticType): Observable<List<Drink>>

}