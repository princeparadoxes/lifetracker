package com.princeparadoxes.watertracker.data.source.db.service

import com.princeparadoxes.watertracker.domain.entity.StatisticType
import com.princeparadoxes.watertracker.data.source.db.model.DbDrink
import io.realm.Realm
import java.util.*

class DrinkDatabaseService {

    val dayStatistic: Float?
        get() = getAllByPeriod(StatisticType.DAY)

    val lastByDay: Float?
        get() {
            val currentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            currentTime.timeZone = TimeZone.getDefault()
            currentTime.set(Calendar.HOUR, 0)
            currentTime.set(Calendar.MINUTE, 0)
            currentTime.set(Calendar.SECOND, 0)
            currentTime.set(Calendar.MILLISECOND, 0)

            val dayDrinks = Realm.getDefaultInstance()
                    .where(DbDrink::class.java)
                    .greaterThan("timestamp", currentTime.timeInMillis)
                    .findAllSorted("timestamp")

            return dayDrinks[dayDrinks.size - 1]!!.size.toFloat()
        }

    fun add(dbDrink: DbDrink): DbDrink {
        Realm.getDefaultInstance().executeTransaction { realm -> realm.copyToRealm(dbDrink) }
        return dbDrink
    }

    fun getAllByPeriod(statisticType: StatisticType): Float? {
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
            else -> throw IllegalArgumentException("Wrong statistic type")
        }

        val timeInMillis = currentTime.timeInMillis

        return Realm.getDefaultInstance()
                .where(DbDrink::class.java)
                .greaterThan("timestamp", timeInMillis)
                .findAll()
                .sum("size")
                .toFloat()
    }

    fun deleteAllWithTimestamp(timestamp: Long): Boolean? {
        Realm.getDefaultInstance().beginTransaction()
        val dbDrinks = Realm.getDefaultInstance().where(DbDrink::class.java)
                .equalTo("timestamp", timestamp)
                .findAll()

        val result = dbDrinks.deleteAllFromRealm()
        Realm.getDefaultInstance().commitTransaction()
        return result
    }
}
