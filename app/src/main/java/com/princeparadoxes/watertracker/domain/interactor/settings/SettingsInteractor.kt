package com.princeparadoxes.watertracker.domain.interactor.settings

import com.princeparadoxes.watertracker.domain.entity.Gender
import com.princeparadoxes.watertracker.safeSubscribe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import kotlin.math.roundToInt

class SettingsInteractor(
        private val settingsInputPort: SettingsInputPort
) : DayNormUseCase, PromotionUseCase, UserUseCase {

    //TODO перенести в Repository
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

    override fun isNeedShowStartPromo(): Single<Boolean> {
        return settingsInputPort.isNeedShowStartPromo()
    }

    override fun onStartPromoShowed(): Single<Any> {
        return settingsInputPort.onStartPromoShowed()
    }

    override fun isNeedShowReportPromo(): Single<Boolean> {
        return settingsInputPort.isNeedShowReportPromo()
    }

    override fun onReportPromoShowed(): Single<Any> {
        return settingsInputPort.onReportPromoShowed()
    }

    override fun observeGender(): Observable<Gender> {
       return settingsInputPort.getGender().toObservable()
    }

    override fun observeWeight(): Observable<Float> {
        return settingsInputPort.getWeight().toObservable()
    }

    override fun updateGender(gender: Gender): Single<Gender> {
        return settingsInputPort.updateGender(gender)
    }

    override fun updateWeight(weight: Float): Single<Float> {
        return settingsInputPort.updateWeight(weight)
    }
}