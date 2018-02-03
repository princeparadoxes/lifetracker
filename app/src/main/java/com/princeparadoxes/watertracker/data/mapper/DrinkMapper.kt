package com.princeparadoxes.watertracker.data.mapper

import com.princeparadoxes.watertracker.data.source.db.model.DbDrink
import com.princeparadoxes.watertracker.domain.entity.Drink

object DrinkMapper {

    fun mapFromDrink(drink: Drink): DbDrink {
        return DbDrink(drink.size, drink.timestamp)
    }

    fun mapFromDBDrink(dbDrink: DbDrink): Drink {
        return Drink(dbDrink.size, dbDrink.timestamp)
    }
}