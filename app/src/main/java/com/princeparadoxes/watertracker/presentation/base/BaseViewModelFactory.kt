package com.princeparadoxes.watertracker.presentation.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

abstract class BaseViewModelFactory<T : ViewModel>() : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(clazz())) {
            return viewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    abstract fun viewModel() : T
    abstract fun clazz() : Class<T>
}