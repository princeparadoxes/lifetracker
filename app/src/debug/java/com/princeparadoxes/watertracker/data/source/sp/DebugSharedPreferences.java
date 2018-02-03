package com.princeparadoxes.watertracker.data.source.sp;

import android.content.SharedPreferences;

public class DebugSharedPreferences {

    private static final String PREFIX = DebugSharedPreferences.class.getName();

    private final SharedPreferences mSharedPreferences;

    public DebugSharedPreferences(SharedPreferences sharedPreferences) {
        this.mSharedPreferences = sharedPreferences;
    }


}
