package com.princeparadoxes.watertracker.data.sp;

import android.content.SharedPreferences;

import com.princeparadoxes.watertracker.ApplicationScope;

import javax.inject.Inject;

@ApplicationScope
public class ProjectPreferences {

    private static final String PREFIX = ProjectPreferences.class.getName();

    private final SharedPreferences mSharedPreferences;

    @Inject
    public ProjectPreferences(SharedPreferences sharedPreferences) {
        this.mSharedPreferences = sharedPreferences;
    }

}
