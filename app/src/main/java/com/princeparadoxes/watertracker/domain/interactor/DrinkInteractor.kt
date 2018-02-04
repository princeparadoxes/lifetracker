package com.princeparadoxes.watertracker.domain.interactor

import com.princeparadoxes.watertracker.domain.entity.Drink
import com.princeparadoxes.watertracker.domain.entity.StatisticType
import com.princeparadoxes.watertracker.domain.entity.StatisticModel
import io.reactivex.Observable

class DrinkInteractor(
        private val drinkInputGateway: DrinkInputGateway
) : DrinkOutputGateway {

    override fun addWater(ml: Int): Observable<Drink> {
        return drinkInputGateway.addWater(ml)
    }

    override fun getDaySum(): Observable<Int> {
        return drinkInputGateway.getDaySum()
    }

    override fun getLast(): Observable<Drink> {
        return drinkInputGateway.getLast();
    }

    override fun getStatisticByPeriod(statisticType: StatisticType): Observable<StatisticModel> {
        return drinkInputGateway.getStatisticByPeriod(statisticType)
    }

}