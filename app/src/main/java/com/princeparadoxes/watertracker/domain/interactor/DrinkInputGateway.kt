package com.princeparadoxes.watertracker.domain.interactor

import com.princeparadoxes.watertracker.domain.entity.Drink
import io.reactivex.Observable

interface DrinkInputGateway {

    fun addWater(ml: Int): Observable<Drink>
    fun getDaySum(): Observable<Int>

}