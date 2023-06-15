package com.princeparadoxes.watertracker.data.source.sp

import android.app.Application
import android.content.SharedPreferences

import com.princeparadoxes.watertracker.ApplicationScope

import dagger.Module
import dagger.Provides

import android.content.Context.MODE_PRIVATE

@Module
class ProjectPreferenceModule {

    @Provides
    @ApplicationScope
    internal fun provideSharedPreferences(app: Application): SharedPreferences {
        return app.getSharedPreferences(DEFAULT_SHARED_PREFERENCE_FILE_NAME, MODE_PRIVATE)
    }

    companion object {

        private const val DEFAULT_SHARED_PREFERENCE_FILE_NAME = "SharedPreferencesDefault"
    }

}
