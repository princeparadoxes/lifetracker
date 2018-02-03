package com.princeparadoxes.watertracker.domain.interactor

import com.princeparadoxes.watertracker.domain.entity.Drink
import io.reactivex.Observable

interface DrinkOutputGateway {

    fun addWater(ml: Int): Observable<Drink>
    fun getDaySum(): Observable<Int>

}