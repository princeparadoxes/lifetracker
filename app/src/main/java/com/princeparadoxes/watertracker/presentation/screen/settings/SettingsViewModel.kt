package com.princeparadoxes.watertracker.presentation.screen.settings

import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent
import com.princeparadoxes.watertracker.R
import com.princeparadoxes.watertracker.domain.entity.Gender
import com.princeparadoxes.watertracker.domain.interactor.settings.DayNormUseCase
import com.princeparadoxes.watertracker.presentation.base.BaseViewModel
import com.princeparadoxes.watertracker.presentation.base.BaseViewModelFactory
import com.princeparadoxes.watertracker.zipToPair
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class SettingsViewModel(private val dayNormUseCase: DayNormUseCase) : BaseViewModel() {

    private var gender: Gender = Gender.NOT_SPECIFIED


    init {
    }


    fun observeCalculate(clicks: Observable<Any>,
                         texts: Observable<TextViewAfterTextChangeEvent>): Observable<Int> {
        return clicks
                .throttleFirst(1, TimeUnit.SECONDS)
                .switchMap {
                    Observable.just(gender).zipToPair(checkWeight(texts))
                            .flatMapSingle { dayNormUseCase.calcDayNorm(it.first, it.second) }
                }
    }

    fun observeSave(clicks: Observable<Any>,
                    texts: Observable<TextViewAfterTextChangeEvent>): Observable<Any> {
        return clicks.throttleFirst(1, TimeUnit.SECONDS)
                .switchMap {
                    texts.map { it.editable().toString().toIntOrNull() ?: 0 }
                            .first(0)
                            .flatMap { dayNormUseCase.updateDayNorm(it) }
                            .toObservable()
                }

    }

    fun observeChangeGender(femaleClickObservable: Observable<Any>,
                            maleClickObservable: Observable<Any>): Observable<Gender> {
        val femaleObservable = femaleClickObservable.map { Gender.FEMALE }
        val maleObservable = maleClickObservable.map { Gender.MALE }

        return femaleObservable.mergeWith(maleObservable)
                .doOnNext { gender = it }
    }

    private fun checkWeight(texts: Observable<TextViewAfterTextChangeEvent>): Observable<Float> {
        return texts.map { it.editable()?.toString()?.toFloatOrNull() ?: 0F }
    }


    class Factory(private val dayNormUseCase: DayNormUseCase) : BaseViewModelFactory<SettingsViewModel>() {

        override fun clazz() = SettingsViewModel::class.java
        override fun viewModel() = SettingsViewModel(dayNormUseCase)

    }

}
