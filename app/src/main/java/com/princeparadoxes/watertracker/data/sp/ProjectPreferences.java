package com.princeparadoxes.watertracker.data.sp;

import android.content.SharedPreferences;

import com.princeparadoxes.watertracker.ApplicationScope;

import javax.inject.Inject;

@ApplicationScope
public class ProjectPreferences {

    private static final String PREFIX = ProjectPreferences.class.getName();

    private static final String CURRENT_DAY_NORM = PREFIX + "current.day.norm";

    private final SharedPreferences mSharedPreferences;

    @Inject
    public ProjectPreferences(SharedPreferences sharedPreferences) {
        this.mSharedPreferences = sharedPreferences;
    }

    public int getCurrentDayNorm() {
        return mSharedPreferences.getInt(CURRENT_DAY_NORM, 2000);
    }

    public int setCurrentDayNorm(int currentDayNorm) {
        mSharedPreferences.edit().putInt(CURRENT_DAY_NORM, currentDayNorm).apply();
        return currentDayNorm;
    }

}
