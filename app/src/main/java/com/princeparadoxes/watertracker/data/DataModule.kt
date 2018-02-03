package com.princeparadoxes.watertracker.data

import com.princeparadoxes.watertracker.ApplicationScope
import com.princeparadoxes.watertracker.data.repository.DrinkRepository
import com.princeparadoxes.watertracker.data.source.db.DatabaseModule
import com.princeparadoxes.watertracker.data.source.db.service.DrinkDatabaseService
import com.princeparadoxes.watertracker.data.source.sp.ProjectPreferenceModule
import com.princeparadoxes.watertracker.data.source.sp.ProjectPreferences
import com.princeparadoxes.watertracker.domain.interactor.DrinkInputGateway
import com.princeparadoxes.watertracker.domain.interactor.DrinkInteractor

import dagger.Module
import dagger.Provides

@Module(includes = [(ProjectPreferenceModule::class), (DatabaseModule::class)])
class DataModule {

    @ApplicationScope
    @Provides
    internal fun provideDrinkRepository(drinkDatabaseService: DrinkDatabaseService,
                                        projectPreferences: ProjectPreferences): DrinkRepository {
        return DrinkRepository(drinkDatabaseService, projectPreferences)
    }

    @ApplicationScope
    @Provides
    internal fun provideDrinkInputGateway(drinkRepository: DrinkRepository): DrinkInputGateway {
        return drinkRepository
    }

}
