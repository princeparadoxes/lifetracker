package com.princeparadoxes.watertracker.data.mapper

import com.princeparadoxes.watertracker.data.source.db.model.DrinkSchema
import com.princeparadoxes.watertracker.domain.entity.Drink

object DrinkMapper {

    fun mapFromDrink(drink: Drink): DrinkSchema {
        return DrinkSchema(drink.size, drink.timestamp)
    }

    fun mapFromDBDrink(drinkSchema: DrinkSchema): Drink {
        return Drink(drinkSchema.size, drinkSchema.timestamp)
    }
}