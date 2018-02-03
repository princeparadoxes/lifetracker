package com.princeparadoxes.watertracker.data.source.db

import com.princeparadoxes.watertracker.ApplicationScope
import com.princeparadoxes.watertracker.data.repository.DrinkRepository
import com.princeparadoxes.watertracker.data.source.db.service.DrinkDatabaseService
import com.princeparadoxes.watertracker.data.source.sp.ProjectPreferenceModule
import com.princeparadoxes.watertracker.data.source.sp.ProjectPreferences

import dagger.Module
import dagger.Provides

@Module(includes = arrayOf(ProjectPreferenceModule::class))
class DatabaseModule {

    @ApplicationScope
    @Provides
    internal fun provideDrinkDatabaseService(): DrinkDatabaseService {
        return DrinkDatabaseService()
    }

}
