package com.princeparadoxes.watertracker.data.repository

import com.princeparadoxes.watertracker.data.mapper.DrinkMapper
import com.princeparadoxes.watertracker.data.source.db.model.DbDrink
import com.princeparadoxes.watertracker.data.source.db.service.DrinkDatabaseService
import com.princeparadoxes.watertracker.data.source.sp.ProjectPreferences
import com.princeparadoxes.watertracker.domain.entity.Drink
import com.princeparadoxes.watertracker.domain.entity.StatisticType
import com.princeparadoxes.watertracker.domain.interactor.DrinkInputGateway
import com.princeparadoxes.watertracker.presentation.screen.statistic.StatisticModel
import io.reactivex.Observable

class DrinkRepository(
        private val mDrinkDatabaseService: DrinkDatabaseService,
        private val mProjectPreferences: ProjectPreferences
) : DrinkInputGateway {


    override fun getDaySum(): Observable<Int> {
        return Observable.just(mDrinkDatabaseService.dayStatistic!!.toInt())
    }

    override fun addWater(ml: Int): Observable<Drink> {
        return Observable.just(DbDrink(ml, System.currentTimeMillis()))
                .map({ mDrinkDatabaseService.add(it) })
                .map({ DrinkMapper.mapFromDBDrink(it) })
    }

    val lastByDay: Observable<Float>
        get() = Observable.fromCallable({ mDrinkDatabaseService.lastByDay })

    fun getSumByPeriod(statisticType: StatisticType): Observable<StatisticModel> {
        return Observable.just(mDrinkDatabaseService.getAllByPeriod(statisticType)!!)
                .map { result ->
                    val normValue = statisticType.countDays * mProjectPreferences.currentDayNorm
                    StatisticModel(statisticType, result!!, normValue.toFloat())
                }
    }

    fun del(timestamp: Long): Observable<Boolean> {
        return Observable.just(mDrinkDatabaseService.deleteAllWithTimestamp(timestamp)!!)
    }
}
