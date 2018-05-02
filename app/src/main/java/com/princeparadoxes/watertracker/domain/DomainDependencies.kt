package com.princeparadoxes.watertracker.domain

import com.princeparadoxes.watertracker.domain.interactor.DrinkOutputPort
import com.princeparadoxes.watertracker.domain.interactor.settings.DayNormUseCase
import com.princeparadoxes.watertracker.domain.interactor.settings.PromotionUseCase
import com.princeparadoxes.watertracker.domain.interactor.settings.UserUseCase

interface DomainDependencies {
    fun provideDrinkOutputGateway(): DrinkOutputPort
    fun provideDayNormUseCase(): DayNormUseCase
    fun provideUserUseCase(): UserUseCase
    fun providePromotionUseCase(): PromotionUseCase
}
