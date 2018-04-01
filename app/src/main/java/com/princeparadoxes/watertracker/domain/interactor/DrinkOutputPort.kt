package com.princeparadoxes.watertracker.domain.interactor

import com.princeparadoxes.watertracker.domain.entity.Drink
import com.princeparadoxes.watertracker.domain.entity.StatisticType
import com.princeparadoxes.watertracker.domain.entity.StatisticModel
import com.princeparadoxes.watertracker.domain.entity.Gender
import io.reactivex.Observable

interface DrinkOutputPort {

    fun addWater(ml: Int): Observable<Drink>
    fun getDaySum(): Observable<Int>
    fun getLast(): Observable<Drink>
    fun observeStatistic(statisticType: StatisticType): Observable<StatisticModel>
    fun observeDetailStatistic(statisticType: StatisticType): Observable<List<Int>>
    fun getDrinksByPeriod(statisticType: StatisticType): Observable<List<Drink>>
    fun removeLastDrink(): Observable<Int>
    fun observeRemoveDrinks(): Observable<Int>
    fun calcDatNorm(gender: Gender, weightInKg : Float) : Int

}