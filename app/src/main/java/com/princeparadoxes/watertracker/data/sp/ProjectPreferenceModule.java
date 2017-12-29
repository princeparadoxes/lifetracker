package com.princeparadoxes.watertracker.data.sp;

import android.app.Application;
import android.content.SharedPreferences;

import com.princeparadoxes.watertracker.ApplicationScope;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.MODE_PRIVATE;

@Module
public class ProjectPreferenceModule {

    private static final String DEFAULT_SHARED_PREFERENCE_FILE_NAME = "SharedPreferencesDefault";

    @Provides
    @ApplicationScope
    SharedPreferences provideSharedPreferences(Application app) {
        return app.getSharedPreferences(DEFAULT_SHARED_PREFERENCE_FILE_NAME, MODE_PRIVATE);
    }

}
