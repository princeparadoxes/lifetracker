package com.princeparadoxes.watertracker.data.repository

import com.princeparadoxes.watertracker.data.source.sp.ProjectPreferences
import com.princeparadoxes.watertracker.domain.interactor.settings.SettingsInputPort
import io.reactivex.Single

class SettingsRepository(
        private val preferences: ProjectPreferences
) : SettingsInputPort {

    override fun getDayNorm(): Single<Int> {
        return Single.fromCallable { preferences.currentDayNorm }
    }

    override fun updateDayNorm(dayNorm: Int): Single<Int> {
        return Single.fromCallable { dayNorm.also { preferences.currentDayNorm = it } }
    }

    override fun isNeedShowStartPromo(): Single<Boolean> {
        return Single.fromCallable { !preferences.isStartPromoShowed }
    }

    override fun onStartPromoShowed(): Single<Any> {
        return Single.fromCallable { Any().also {  preferences.isStartPromoShowed = true} }
    }
}
