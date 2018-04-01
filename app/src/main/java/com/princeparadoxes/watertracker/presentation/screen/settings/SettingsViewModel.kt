package com.princeparadoxes.watertracker.presentation.screen.settings

import com.jakewharton.rxbinding2.InitialValueObservable
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent
import com.princeparadoxes.watertracker.R
import com.princeparadoxes.watertracker.base.BaseViewModel
import com.princeparadoxes.watertracker.base.BaseViewModelFactory
import com.princeparadoxes.watertracker.domain.entity.Gender
import com.princeparadoxes.watertracker.domain.interactor.settings.DayNormUseCase
import com.princeparadoxes.watertracker.utils.zipToPair
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class SettingsViewModel(private val dayNormUseCase: DayNormUseCase) : BaseViewModel() {


    init {
    }


    fun observeCalculate(clicks: Observable<Any>,
                         checkedChanges: InitialValueObservable<Int>,
                         texts: Observable<TextViewAfterTextChangeEvent>): Observable<Int> {
        return clicks.throttleFirst(1, TimeUnit.SECONDS)
                .switchMap {
                    checkGender(checkedChanges).zipToPair(checkWeight(texts))
                            .flatMapSingle { dayNormUseCase.calcDayNorm(it.first, it.second) }
                            .first(0)
                            .toObservable()
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

    private fun checkGender(checkedChanges: InitialValueObservable<Int>): Observable<Gender> {
        return checkedChanges
                .map {
                    when (it) {
                        R.id.settings_gender_group_female -> Gender.FEMALE
                        R.id.settings_gender_group_male -> Gender.MALE
                        else -> Gender.NOT_SPECIFIED
                    }
                }
    }

    private fun checkWeight(texts: Observable<TextViewAfterTextChangeEvent>): Observable<Float> {
        return texts.map { it.editable()?.toString()?.toFloatOrNull() ?: 0F }
    }


    class Factory(private val dayNormUseCase: DayNormUseCase) : BaseViewModelFactory<SettingsViewModel>() {

        override fun clazz() = SettingsViewModel::class.java
        override fun viewModel() = SettingsViewModel(dayNormUseCase)

    }

}
