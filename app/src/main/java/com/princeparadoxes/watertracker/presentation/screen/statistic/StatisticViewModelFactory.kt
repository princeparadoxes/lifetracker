package com.princeparadoxes.watertracker.presentation.screen.statistic

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.princeparadoxes.watertracker.domain.interactor.DrinkOutputPort

class StatisticViewModelFactory(
        private val drinkOutputPort: DrinkOutputPort
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticViewModel::class.java)) {
            return StatisticViewModel(drinkOutputPort) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
