package com.princeparadoxes.watertracker.domain.interactor.settings

import io.reactivex.Single

interface SettingsInputPort {

    fun getDayNorm(): Single<Int>
    fun updateDayNorm(dayNorm: Int): Single<Int>

}