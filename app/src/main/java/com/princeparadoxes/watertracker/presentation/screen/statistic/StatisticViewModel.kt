package com.princeparadoxes.watertracker.presentation.screen.statistic

import android.arch.lifecycle.ViewModel
import com.princeparadoxes.watertracker.domain.entity.StatisticType
import com.princeparadoxes.watertracker.domain.interactor.DrinkOutputGateway
import io.reactivex.Observable
import timber.log.Timber

class StatisticViewModel(private val drinkOutputGateway: DrinkOutputGateway) : ViewModel() {

    fun getDayNorm(): Observable<Int> {
        return drinkOutputGateway.getDaySum()
                .onErrorReturn { 0 }
    }

    fun getLast(): Observable<Int> {
        return drinkOutputGateway.getLast()
                .map { it.size }
                .onErrorReturn { 0 }
    }

    fun getStatistic(): Observable<List<StatisticModel>> {
        return Observable.just(StatisticType.values().asList())
                .map { it.map { drinkOutputGateway.getStatisticByPeriod(it) } }
                .flatMap { Observable.combineLatest(it, { it.map { it as StatisticModel } }) }
                .map { it.toList() }
    }

}
