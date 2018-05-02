package com.princeparadoxes.watertracker.domain.interactor.settings

import com.princeparadoxes.watertracker.domain.entity.Gender
import io.reactivex.Observable
import io.reactivex.Single

interface UserUseCase {

    fun observeGender(): Observable<Gender>

    fun observeWeight(): Observable<Float>

    fun updateGender(gender: Gender): Single<Gender>

    fun updateWeight(weight: Float): Single<Float>

}