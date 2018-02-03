package com.princeparadoxes.watertracker.domain

import com.princeparadoxes.watertracker.domain.interactor.DrinkInputGateway
import com.princeparadoxes.watertracker.domain.interactor.DrinkOutputGateway

interface DomainDependencies {
    fun provideDrinkInputGateway(): DrinkInputGateway
    fun provideDrinkOutputGateway(): DrinkOutputGateway
}
