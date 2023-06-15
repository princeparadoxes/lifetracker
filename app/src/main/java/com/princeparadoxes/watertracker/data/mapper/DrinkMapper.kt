package com.princeparadoxes.watertracker.data.mapper

import com.princeparadoxes.watertracker.data.source.db.model.DrinkSchema
import com.princeparadoxes.watertracker.domain.entity.Drink

object DrinkMapper {

    fun mapToDrinkSchema(drink: Drink): DrinkSchema {
        return DrinkSchema(drink.size, drink.timestamp)
    }

    fun mapFromDrinkSchema(drinkSchema: DrinkSchema): Drink {
        return Drink(drinkSchema.size, drinkSchema.timestamp)
    }
}