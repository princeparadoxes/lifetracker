package com.princeparadoxes.watertracker.domain.interactor.settings

import com.princeparadoxes.watertracker.domain.entity.Gender
import io.reactivex.Observable
import io.reactivex.Single

interface DayNormUseCase {

    fun calcDayNorm(gender: Gender, weightInKg: Float): Single<Int>

    fun observeDayNorm(): Observable<Int>

    fun updateDayNorm(dayNorm: Int): Single<Int>

}