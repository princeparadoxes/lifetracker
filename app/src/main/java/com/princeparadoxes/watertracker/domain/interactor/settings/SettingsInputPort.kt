package com.princeparadoxes.watertracker.domain.interactor.settings

import com.princeparadoxes.watertracker.domain.entity.Gender
import io.reactivex.Observable
import io.reactivex.Single

interface SettingsInputPort {

    fun getDayNorm(): Single<Int>
    fun updateDayNorm(dayNorm: Int): Single<Int>
    fun isNeedShowStartPromo(): Single<Boolean>
    fun onStartPromoShowed(): Single<Any>
    fun updateGender(gender: Gender): Single<Gender>
    fun getGender(): Single<Gender>
    fun updateWeight(weight: Float): Single<Float>
    fun getWeight(): Single<Float>

}