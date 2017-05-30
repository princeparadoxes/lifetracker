package com.princeparadoxes.watertracker.data.repository;

import com.princeparadoxes.watertracker.ApplicationScope;
import com.princeparadoxes.watertracker.data.db.model.DBDrink;
import com.princeparadoxes.watertracker.data.db.repository.DBDrinkRepository;
import com.princeparadoxes.watertracker.data.model.Drink;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by as3co on 06.05.2017.
 */
@ApplicationScope
public class DrinkRepository {

    private final DBDrinkRepository mDBDrinkRepository;

    @Inject
    public DrinkRepository(DBDrinkRepository dbDrinkRepository) {
        mDBDrinkRepository = dbDrinkRepository;

    }

    public Observable <Drink> add(Drink drink) {
        return Observable.just(DBDrink.mapFromDrink(drink))
                .map(mDBDrinkRepository::add)
                .map(Drink::mapFromDBDrink);
    }
}
