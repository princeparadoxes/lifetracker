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



    companion object {

        private val PREFIX = ProjectPreferences::class.java.name

        private val CURRENT_DAY_NORM = PREFIX + "current.day.norm"
    }

}
