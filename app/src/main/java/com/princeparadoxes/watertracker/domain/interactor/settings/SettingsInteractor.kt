package com.princeparadoxes.watertracker.domain.interactor.settings

import com.princeparadoxes.watertracker.domain.entity.Gender
import com.princeparadoxes.watertracker.utils.safeSubscribe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import kotlin.math.roundToInt

class SettingsInteractor(
        private val settingsInputPort: SettingsInputPort
) : DayNormUseCase {

    private val dayNormSubject = BehaviorSubject.create<Int>()

    init {
        settingsInputPort.getDayNorm()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .safeSubscribe { dayNormSubject.onNext(it) }
    }

    override fun calcDayNorm(gender: Gender, weightInKg: Float): Single<Int> {
        return Single.fromCallable { (gender.mlByKg * weightInKg).roundToInt() }
    }

    override fun observeDayNorm(): Observable<Int> {
        return dayNormSubject
    }

    override fun updateDayNorm(dayNorm: Int): Single<Int> {
        return settingsInputPort.updateDayNorm(dayNorm)
                .doOnSuccess { dayNormSubject.onNext(it) }
    }


}