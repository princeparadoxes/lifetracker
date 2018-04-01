package com.princeparadoxes.watertracker.data.repository

import com.princeparadoxes.watertracker.data.mapper.DrinkMapper
import com.princeparadoxes.watertracker.data.source.db.model.DrinkSchema
import com.princeparadoxes.watertracker.data.source.db.service.DrinkDatabaseService
import com.princeparadoxes.watertracker.data.source.sp.ProjectPreferences
import com.princeparadoxes.watertracker.domain.entity.Drink
import com.princeparadoxes.watertracker.domain.entity.StatisticModel
import com.princeparadoxes.watertracker.domain.entity.StatisticType
import com.princeparadoxes.watertracker.domain.interactor.DrinkInputPort
import io.reactivex.Observable

class DrinkRepository(
        private val drinkDatabaseService: DrinkDatabaseService,
        private val preferences: ProjectPreferences
) : DrinkInputPort {

    override fun getDaySum(): Observable<Int> {
        return drinkDatabaseService.getStatisticByPeriod(StatisticType.DAY)
    }

    override fun addWater(ml: Int): Observable<Drink> {
        return Observable.just(DrinkSchema(ml, System.currentTimeMillis()))
                .map({ drinkDatabaseService.add(it) })
                .map({ DrinkMapper.mapFromDrinkSchema(it) })
    }

    override fun getLast(): Observable<Drink> {
        return drinkDatabaseService.getLast()
                .map { DrinkMapper.mapFromDrinkSchema(it) }
    }

    override fun getStatisticByPeriod(statisticType: StatisticType): Observable<StatisticModel> {
        return drinkDatabaseService.getStatisticByPeriod(statisticType)
                .map { result ->
                    val normValue = statisticType.countDays * preferences.currentDayNorm
                    StatisticModel(statisticType, result.toFloat(), normValue.toFloat())
                }
    }

    override fun getDrinksByPeriod(statisticType: StatisticType): Observable<List<Drink>> {
        return drinkDatabaseService.getDrinksByPeriod(statisticType)
                .map { it.map { DrinkMapper.mapFromDrinkSchema(it) } }
    }

    override fun removeLastDrink(): Observable<Int> {
        return Observable.just(drinkDatabaseService.removeLast())
    }

    fun del(timestamp: Long): Observable<Boolean> {
        return Observable.just(drinkDatabaseService.deleteAllWithTimestamp(timestamp)!!)
    }
}
