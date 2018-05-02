package com.princeparadoxes.watertracker.data.source.sp

import android.content.SharedPreferences

import com.princeparadoxes.watertracker.ApplicationScope
import com.princeparadoxes.watertracker.domain.entity.Gender

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

    var gender: Gender
        get() = Gender.values()[mSharedPreferences.getInt(GENDER, Gender.NOT_SPECIFIED.ordinal)]
        set(value) = mSharedPreferences.edit().putInt(GENDER, value.ordinal).apply()

    var weight: Float
        get() = mSharedPreferences.getFloat(WEIGHT, 0F)
        set(value) = mSharedPreferences.edit().putFloat(WEIGHT, value).apply()

    companion object {

        private val PREFIX = ProjectPreferences::class.java.name

        private val CURRENT_DAY_NORM = PREFIX + "current.day.norm"
        private val START_PROMO_SHOWED = PREFIX + "start.promo.showed"
        private val GENDER = PREFIX + "gender"
        private val WEIGHT = PREFIX + "weight"
    }

}
