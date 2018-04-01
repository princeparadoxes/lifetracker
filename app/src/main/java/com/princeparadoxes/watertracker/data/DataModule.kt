package com.princeparadoxes.watertracker.data

import com.princeparadoxes.watertracker.ApplicationScope
import com.princeparadoxes.watertracker.data.repository.DrinkRepository
import com.princeparadoxes.watertracker.data.repository.SettingsRepository
import com.princeparadoxes.watertracker.data.source.db.DatabaseModule
import com.princeparadoxes.watertracker.data.source.db.service.DrinkDatabaseService
import com.princeparadoxes.watertracker.data.source.sp.ProjectPreferenceModule
import com.princeparadoxes.watertracker.data.source.sp.ProjectPreferences
import com.princeparadoxes.watertracker.domain.interactor.DrinkInputPort
import com.princeparadoxes.watertracker.domain.interactor.settings.SettingsInputPort

import dagger.Module
import dagger.Provides

@Module(includes = [(ProjectPreferenceModule::class), (DatabaseModule::class)])
class DataModule {

    @ApplicationScope
    @Provides
    internal fun provideDrinkInputPort(drinkDatabaseService: DrinkDatabaseService,
                                       projectPreferences: ProjectPreferences): DrinkInputPort {
        return DrinkRepository(drinkDatabaseService, projectPreferences)
    }

    @ApplicationScope
    @Provides
    internal fun provideDrinkInputGateway(projectPreferences: ProjectPreferences): SettingsInputPort {
        return SettingsRepository(projectPreferences)
    }

}
