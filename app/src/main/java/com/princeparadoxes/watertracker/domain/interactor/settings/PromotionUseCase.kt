package com.princeparadoxes.watertracker.domain.interactor.settings

import com.princeparadoxes.watertracker.domain.entity.Gender
import io.reactivex.Observable
import io.reactivex.Single

interface PromotionUseCase {

    fun isNeedShowStartPromo(): Single<Boolean>

    fun onStartPromoShowed(): Single<Any>

}