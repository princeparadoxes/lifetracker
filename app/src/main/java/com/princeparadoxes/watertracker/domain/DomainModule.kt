package com.princeparadoxes.watertracker.domain

import com.princeparadoxes.watertracker.ApplicationScope
import com.princeparadoxes.watertracker.data.repository.DrinkRepository
import com.princeparadoxes.watertracker.data.source.db.DatabaseModule
import com.princeparadoxes.watertracker.data.source.db.service.DrinkDatabaseService
import com.princeparadoxes.watertracker.data.source.sp.ProjectPreferenceModule
import com.princeparadoxes.watertracker.data.source.sp.ProjectPreferences
import com.princeparadoxes.watertracker.domain.interactor.DrinkInputGateway
import com.princeparadoxes.watertracker.domain.interactor.DrinkInteractor
import com.princeparadoxes.watertracker.domain.interactor.DrinkOutputGateway

import dagger.Module
import dagger.Provides

@Module(includes = [(ProjectPreferenceModule::class), (DatabaseModule::class)])
class DomainModule {

    @ApplicationScope
    @Provides
    internal fun provideDrinkOutputGateway(drinkInputGateway: DrinkInputGateway): DrinkOutputGateway {
        return DrinkInteractor(drinkInputGateway)
    }

}
