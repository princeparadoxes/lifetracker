package com.princeparadoxes.watertracker.domain

import com.princeparadoxes.watertracker.ApplicationScope
import com.princeparadoxes.watertracker.data.source.db.DatabaseModule
import com.princeparadoxes.watertracker.data.source.sp.ProjectPreferenceModule
import com.princeparadoxes.watertracker.domain.interactor.DrinkInputPort
import com.princeparadoxes.watertracker.domain.interactor.DrinkInteractor
import com.princeparadoxes.watertracker.domain.interactor.DrinkOutputPort
import com.princeparadoxes.watertracker.domain.interactor.settings.*
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [(ProjectPreferenceModule::class), (DatabaseModule::class)])
abstract class DomainBinderModule {

    @ApplicationScope
    @Binds
    abstract fun provideDayNormUseCase(settingsInteractor: SettingsInteractor) : DayNormUseCase

    @ApplicationScope
    @Binds
    abstract fun provideUserUseCase(settingsInteractor: SettingsInteractor) : UserUseCase

    @ApplicationScope
    @Binds
    abstract fun providePromotionUseCase(settingsInteractor: SettingsInteractor) : PromotionUseCase

}
