package com.princeparadoxes.watertracker.data.repository

import com.princeparadoxes.watertracker.data.mapper.DrinkMapper
import com.princeparadoxes.watertracker.data.source.db.model.DrinkSchema
import com.princeparadoxes.watertracker.data.source.db.service.DrinkDatabaseService
import com.princeparadoxes.watertracker.data.source.sp.ProjectPreferences
import com.princeparadoxes.watertracker.domain.entity.Drink
import com.princeparadoxes.watertracker.domain.entity.StatisticType
import com.princeparadoxes.watertracker.domain.interactor.DrinkInputGateway
import com.princeparadoxes.watertracker.domain.entity.StatisticModel
import io.reactivex.Observable

class DrinkRepository(
        private val mDrinkDatabaseService: DrinkDatabaseService,
        private val mProjectPreferences: ProjectPreferences
) : DrinkInputGateway {

    override fun getDaySum(): Observable<Int> {
        return mDrinkDatabaseService.getStatisticByPeriod(StatisticType.DAY)
    }

    override fun addWater(ml: Int): Observable<Drink> {
        return Observable.just(DrinkSchema(ml, System.currentTimeMillis()))
                .map({ mDrinkDatabaseService.add(it) })
                .map({ DrinkMapper.mapFromDrinkSchema(it) })
    }

    override fun getLast(): Observable<Drink> {
        return mDrinkDatabaseService.getLast()
                .map { DrinkMapper.mapFromDrinkSchema(it) }
    }

    override fun getStatisticByPeriod(statisticType: StatisticType): Observable<StatisticModel> {
        return mDrinkDatabaseService.getStatisticByPeriod(statisticType)
                .map { result ->
                    val normValue = statisticType.countDays * mProjectPreferences.currentDayNorm
                    StatisticModel(statisticType, result.toFloat(), normValue.toFloat())
                }
    }

    override fun getDrinksByPeriod(statisticType: StatisticType): Observable<List<Drink>> {
        return mDrinkDatabaseService.getDrinksByPeriod(statisticType)
                .map { it.map { DrinkMapper.mapFromDrinkSchema(it) } }
    }

    fun del(timestamp: Long): Observable<Boolean> {
        return Observable.just(mDrinkDatabaseService.deleteAllWithTimestamp(timestamp)!!)
    }
}
