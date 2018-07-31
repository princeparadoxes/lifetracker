package com.princeparadoxes.watertracker.data.repository

import com.princeparadoxes.watertracker.data.source.sp.ProjectPreferences
import com.princeparadoxes.watertracker.domain.entity.Gender
import com.princeparadoxes.watertracker.domain.interactor.settings.SettingsInputPort
import io.reactivex.Observable
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

    override fun isNeedShowReportPromo(): Single<Boolean> {
        return Single.fromCallable { !preferences.isReportPromoShowed }
    }

    override fun onReportPromoShowed(): Single<Any> {
        return Single.fromCallable { Any().also {  preferences.isReportPromoShowed = true} }
    }

    override fun getGender(): Single<Gender> {
        return Single.fromCallable { preferences.gender }
    }

    override fun updateGender(gender: Gender): Single<Gender> {
        return Single.fromCallable { gender.also { preferences.gender = it } }
    }

    override fun getWeight(): Single<Float> {
        return Single.fromCallable { preferences.weight }
    }

    override fun updateWeight(weight: Float): Single<Float> {
        return Single.fromCallable { weight.also { preferences.weight = it } }
    }
}
