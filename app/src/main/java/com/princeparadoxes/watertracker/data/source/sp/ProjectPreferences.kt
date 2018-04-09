package com.princeparadoxes.watertracker.data.source.sp

import android.content.SharedPreferences

import com.princeparadoxes.watertracker.ApplicationScope

import javax.inject.Inject

@ApplicationScope
class ProjectPreferences @Inject
constructor(private val mSharedPreferences: SharedPreferences) {

    var currentDayNorm: Int
        get() = mSharedPreferences.getInt(CURRENT_DAY_NORM, 2000)
        set(dayNorm) = mSharedPreferences.edit().putInt(CURRENT_DAY_NORM, dayNorm).apply()


    var isStartPromoShowed: Boolean
        get() = mSharedPreferences.getBoolean(START_PROMO_SHOWED, false)
        set(value) = mSharedPreferences.edit().putBoolean(START_PROMO_SHOWED, value).apply()

    companion object {

        private val PREFIX = ProjectPreferences::class.java.name

        private val CURRENT_DAY_NORM = PREFIX + "current.day.norm"
        private val START_PROMO_SHOWED = PREFIX + "start.promo.showed"
    }

}
