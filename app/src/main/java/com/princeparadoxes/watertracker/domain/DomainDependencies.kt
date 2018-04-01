package com.princeparadoxes.watertracker.domain

import com.princeparadoxes.watertracker.domain.interactor.DrinkInputPort
import com.princeparadoxes.watertracker.domain.interactor.DrinkOutputPort
import com.princeparadoxes.watertracker.domain.interactor.settings.DayNormUseCase

interface DomainDependencies {
    fun provideDrinkOutputGateway(): DrinkOutputPort
    fun provideDayNormUseCase(): DayNormUseCase
}
