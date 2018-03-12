package com.princeparadoxes.watertracker.presentation.screen.statistic

import com.princeparadoxes.watertracker.base.BaseViewModel
import com.princeparadoxes.watertracker.domain.entity.Drink
import com.princeparadoxes.watertracker.domain.entity.StatisticModel
import com.princeparadoxes.watertracker.domain.entity.StatisticType
import com.princeparadoxes.watertracker.domain.interactor.DrinkOutputGateway
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

class StatisticViewModel(private val drinkOutputGateway: DrinkOutputGateway) : BaseViewModel() {

    private val viewState: ViewState = ViewState()

    init {
        unsubscribeOnClear(viewState.changeDrinksPeriodSubject
                .switchMap { drinkOutputGateway.getDrinksByPeriod(it) }
                .subscribe({ viewState.drinksByPeriodSubject.onNext(it) }, { Timber.e(it) }))
    }

    fun changeDrinkPeriod(statisticType: StatisticType) {
        viewState.changeDrinksPeriodSubject.onNext(statisticType)
    }

    fun observeDaySum(): Observable<Int> {
        return drinkOutputGateway.getDaySum()
                .onErrorReturn { 0 }
    }

    fun observeLastDrink(): Observable<Int> {
        return drinkOutputGateway.getLast()
                .map { it.size }
                .onErrorReturn { 0 }
    }

    fun observeStatistic(): Observable<List<StatisticModel>> {
        return Observable.just(StatisticType.values().asList().filter { it != StatisticType.DAY })
                .map { it.map { drinkOutputGateway.observeStatistic(it) } }
                .flatMap { Observable.combineLatest(it, { it.map { it as StatisticModel } }) }
                .map { it.toList() }
    }

    fun observeDetailStatistic() : Observable<List<Int>>{
        return viewState.changeDrinksPeriodSubject
                .switchMap { drinkOutputGateway.observeDetailStatistic(it) }
    }

    fun observeDrinksByPeriod(): Observable<List<Drink>> {
        return viewState.drinksByPeriodSubject
    }

    private inner class ViewState {
        val changeDrinksPeriodSubject = PublishSubject.create<StatisticType>()!!
        val drinksByPeriodSubject = BehaviorSubject.create<List<Drink>>()!!
    }

}
