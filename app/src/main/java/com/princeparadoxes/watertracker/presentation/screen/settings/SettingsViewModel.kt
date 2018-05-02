package com.princeparadoxes.watertracker.presentation.screen.settings

import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent
import com.princeparadoxes.watertracker.domain.entity.Gender
import com.princeparadoxes.watertracker.domain.interactor.settings.DayNormUseCase
import com.princeparadoxes.watertracker.domain.interactor.settings.UserUseCase
import com.princeparadoxes.watertracker.presentation.base.BaseViewModel
import com.princeparadoxes.watertracker.presentation.base.BaseViewModelFactory
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class SettingsViewModel(private val dayNormUseCase: DayNormUseCase,
                        private val userUseCase: UserUseCase) : BaseViewModel() {

    private val viewState = ViewState()

    init {
    }

    fun observeCalculate(clicks: Observable<Any>): Observable<Int> {
        return clicks
                .throttleFirst(1, TimeUnit.SECONDS)
                .switchMap { dayNormUseCase.calcDayNorm(viewState.gender, viewState.weight).toObservable() }
    }

    fun observeSave(clicks: Observable<Any>): Observable<Any> {
        return clicks
                .throttleFirst(1, TimeUnit.SECONDS)
                .switchMap { dayNormUseCase.updateDayNorm(viewState.dayNorm).toObservable() }
                .switchMap { userUseCase.updateWeight(viewState.weight).toObservable() }
                .switchMap { userUseCase.updateGender(viewState.gender).toObservable() }
    }

    fun observeView(weightTextObservable: Observable<TextViewAfterTextChangeEvent>,
                    dayNormTextObservable: Observable<TextViewAfterTextChangeEvent>,
                    femaleClickObservable: Observable<Any>,
                    maleClickObservable: Observable<Any>): Observable<ViewState> {

        val genderObservable = femaleClickObservable.map { Gender.FEMALE }
                .mergeWith(maleClickObservable.map { Gender.MALE })
                .mergeWith(userUseCase.observeGender())
                .distinctUntilChanged()
                .doOnNext { viewState.gender = it }

        val weightObservable = weightTextObservable
                .map { it.editable()?.toString()?.toFloatOrNull() ?: 0F }
                .mergeWith(userUseCase.observeWeight())
                .distinctUntilChanged()
                .doOnNext { viewState.weight = it }

        val dayNormObservable = dayNormTextObservable
                .map { it.editable()?.toString()?.toIntOrNull() ?: 0 }
                .mergeWith(dayNormUseCase.observeDayNorm())
                .distinctUntilChanged()
                .doOnNext { viewState.dayNorm = it }

        return Observable.merge(weightObservable, dayNormObservable, genderObservable)
                .map { viewState }
    }

    class Factory(private val dayNormUseCase: DayNormUseCase,
                  private val userUseCase: UserUseCase) : BaseViewModelFactory<SettingsViewModel>() {

        override fun clazz() = SettingsViewModel::class.java
        override fun viewModel() = SettingsViewModel(dayNormUseCase, userUseCase)

    }

    class ViewState {
        var gender: Gender = Gender.NOT_SPECIFIED
        var weight: Float = 0F
        var dayNorm: Int = 0
    }

}
