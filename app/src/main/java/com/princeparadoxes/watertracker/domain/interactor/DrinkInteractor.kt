package com.princeparadoxes.watertracker.domain.interactor

import com.princeparadoxes.watertracker.domain.entity.Drink
import com.princeparadoxes.watertracker.domain.entity.StatisticModel
import com.princeparadoxes.watertracker.domain.entity.StatisticType
import com.princeparadoxes.watertracker.domain.entity.Gender
import com.princeparadoxes.watertracker.utils.toCalendar
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlin.math.roundToInt

class DrinkInteractor(
        private val drinkInputPort: DrinkInputPort
) : DrinkOutputPort {

    val removeDrinkSubject = PublishSubject.create<Any>()

    override fun addWater(ml: Int): Observable<Drink> {
        return drinkInputPort.addWater(ml)
    }

    override fun getDaySum(): Observable<Int> {
        return drinkInputPort.getDaySum()
    }

    override fun getLast(): Observable<Drink> {
        return drinkInputPort.getLast();
    }

    override fun removeLastDrink(): Observable<Int> {
        return drinkInputPort.removeLastDrink()
                .doOnNext { if (it > 0) removeDrinkSubject.onNext(Any()) }
    }

    override fun calcDatNorm(gender: Gender, weightInKg: Float) : Int {
        return (gender.mlByKg * weightInKg).roundToInt()
    }

    override fun observeRemoveDrinks(): Observable<Int> {
        return removeDrinkSubject
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapSingle { drinkInputPort.getDaySum().firstOrError() }
    }

    override fun observeStatistic(statisticType: StatisticType): Observable<StatisticModel> {
        return drinkInputPort.getStatisticByPeriod(statisticType)
    }

    override fun getDrinksByPeriod(statisticType: StatisticType): Observable<List<Drink>> {
        return drinkInputPort.getDrinksByPeriod(statisticType)
    }

    override fun observeDetailStatistic(statisticType: StatisticType): Observable<List<Int>> {
        return drinkInputPort.getDrinksByPeriod(statisticType).compose({ detailBy(it, statisticType) })
    }

    private fun detailBy(upstream: Observable<List<Drink>>, statisticType: StatisticType): Observable<List<Int>> {
        return upstream.flatMapSingle { collect(it, statisticType) }
                .map { IntArray(statisticType.countDays).apply { fill(0) } to it }
                .map { it.first.mapIndexed { index, i -> i + it.second.getOrDefault(index, 0) } }
    }

    private fun collect(drinks: List<Drink>, statisticType: StatisticType): Single<Map<Int, Int>> {
        return Observable.fromIterable(drinks)
                .groupBy { it.timestamp.toCalendar().get(statisticType.calendarField) }
                .flatMap { group -> group.compose(sum()).map { group.key to it } }
                .toList()
                .map { it.toMap() }
    }

    private fun sum(): ObservableTransformer<Drink, Int> {
        return ObservableTransformer { it.toList().map { it.map { it.size }.sum() }.toObservable() }
    }

}