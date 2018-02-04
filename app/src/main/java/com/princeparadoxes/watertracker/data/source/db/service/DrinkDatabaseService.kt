package com.princeparadoxes.watertracker.data.source.db.service

import com.princeparadoxes.watertracker.data.source.db.model.DrinkSchema
import com.princeparadoxes.watertracker.domain.entity.StatisticType
import io.reactivex.Observable
import io.realm.Realm
import java.util.*

class DrinkDatabaseService {

    fun getLast(): Observable<DrinkSchema> {
        return Realm.getDefaultInstance()
                .where(DrinkSchema::class.java)
                .findAll()
                .asFlowable()
                .map { it.last()!! }
                .toObservable()
    }

    fun add(drinkSchema: DrinkSchema): DrinkSchema {
        Realm.getDefaultInstance().executeTransaction { realm -> realm.copyToRealm(drinkSchema) }
        return drinkSchema
    }

    fun getAllByPeriod(statisticType: StatisticType): Observable<Int> {
        val currentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        currentTime.timeZone = TimeZone.getDefault()
        currentTime.set(Calendar.HOUR, 0)
        currentTime.set(Calendar.MINUTE, 0)
        currentTime.set(Calendar.SECOND, 0)
        currentTime.set(Calendar.MILLISECOND, 0)

        when (statisticType) {
            StatisticType.DAY -> {
            }
            StatisticType.WEEK -> currentTime.add(Calendar.WEEK_OF_YEAR, -1)
            StatisticType.MONTH -> currentTime.add(Calendar.MONTH, -1)
            StatisticType.YEAR -> currentTime.add(Calendar.YEAR, -1)
        }

        val timeInMillis = currentTime.timeInMillis

        return Realm.getDefaultInstance()
                .where(DrinkSchema::class.java)
                .greaterThan("timestamp", timeInMillis)
                .findAll()
                .asFlowable()
                .map { it.map { it.size }.sum() }
                .toObservable()
    }

    fun deleteAllWithTimestamp(timestamp: Long): Boolean? {
        Realm.getDefaultInstance().beginTransaction()
        val dbDrinks = Realm.getDefaultInstance().where(DrinkSchema::class.java)
                .equalTo("timestamp", timestamp)
                .findAll()

        val result = dbDrinks.deleteAllFromRealm()
        Realm.getDefaultInstance().commitTransaction()
        return result
    }
}
