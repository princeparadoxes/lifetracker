package com.princeparadoxes.watertracker.presentation.screen.tablet.controls

import com.princeparadoxes.watertracker.presentation.base.BaseViewModel
import com.princeparadoxes.watertracker.domain.entity.Drink
import com.princeparadoxes.watertracker.domain.entity.StatisticModel
import com.princeparadoxes.watertracker.domain.entity.StatisticType
import com.princeparadoxes.watertracker.domain.interactor.DrinkOutputPort
import com.princeparadoxes.watertracker.presentation.base.BaseViewModelFactory
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit

class TabletControlsViewModel(private val drinkOutputPort: DrinkOutputPort) : BaseViewModel() {

    private val viewState: ViewState = ViewState()


    fun observeDaySum(): Observable<Int> {
        return drinkOutputPort.getDaySum()
    }

    fun observeLastDrink(): Observable<Int> {
        return drinkOutputPort.getLast()
                .map { it.size }
    }

    fun observeDeleteWater(clickObservable: Observable<Any>): Observable<Int> {
        return clickObservable
                .throttleFirst(1, TimeUnit.SECONDS)
                .switchMap { drinkOutputPort.removeLastDrink() }
    }

    fun observeReport(clickObservable: Observable<Any>): Observable<Any> {
        return clickObservable
                .throttleFirst(1, TimeUnit.SECONDS)
    }

    fun observeSetting(clickObservable: Observable<Any>): Observable<Any> {
        return clickObservable
                .throttleFirst(1, TimeUnit.SECONDS)
    }

    private inner class ViewState {
    }

    class Factory(private val drinkOutputPort: DrinkOutputPort) : BaseViewModelFactory<TabletControlsViewModel>() {

        override fun clazz() = TabletControlsViewModel::class.java
        override fun viewModel() = TabletControlsViewModel(drinkOutputPort)

    }

}
