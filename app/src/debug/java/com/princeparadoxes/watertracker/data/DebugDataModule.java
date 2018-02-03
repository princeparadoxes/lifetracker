package com.princeparadoxes.watertracker.data;

import android.app.Application;
import android.content.SharedPreferences;

import com.princeparadoxes.watertracker.ApplicationScope;
import com.princeparadoxes.watertracker.DebugDrawerInitializer;
import com.princeparadoxes.watertracker.data.sp.DebugSharedPreferences;
import com.princeparadoxes.watertracker.data.sp.ProjectPreferences;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.MODE_PRIVATE;

@Module(includes = {DataModule.class})
public class DebugDataModule {

    private static final String DEBUG_SHARED_PREFERENCE_FILE_NAME = "debug_world_chess";

    @Provides
    @ApplicationScope
    DebugDrawerInitializer provideDebugDrawerInitializer(Application application,
                                                         ProjectPreferences preferences,
                                                         DebugSharedPreferences debugSharedPreferences) {
        return new DebugDrawerInitializer(application, preferences, debugSharedPreferences);
    }

    @Provides
    @Named("DebugSharedPreference")
    SharedPreferences provideDebugSharedPref(Application application) {
        return application.getSharedPreferences(DEBUG_SHARED_PREFERENCE_FILE_NAME, MODE_PRIVATE);
    }

    @Provides
    @ApplicationScope
    DebugSharedPreferences provideDebugSharedPreferences(@Named("DebugSharedPreference") SharedPreferences sharedPreferences) {
        return new DebugSharedPreferences(sharedPreferences);
    }

}
