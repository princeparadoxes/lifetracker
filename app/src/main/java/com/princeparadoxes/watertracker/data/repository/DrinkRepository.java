package com.princeparadoxes.watertracker.data.repository;

import com.princeparadoxes.watertracker.ApplicationScope;
import com.princeparadoxes.watertracker.data.db.model.DbDrink;
import com.princeparadoxes.watertracker.data.db.repository.DBDrinkRepository;
import com.princeparadoxes.watertracker.data.model.Drink;
import com.princeparadoxes.watertracker.data.model.StatisticType;
import com.princeparadoxes.watertracker.ui.screen.main.statistic.StatisticModel;

import javax.inject.Inject;

import io.reactivex.Observable;

@ApplicationScope
public class DrinkRepository {

    private final DBDrinkRepository mDBDrinkRepository;

    @Inject
    public DrinkRepository(DBDrinkRepository dbDrinkRepository) {
        mDBDrinkRepository = dbDrinkRepository;

    }

    public Observable<Drink> add(Drink drink) {
        return Observable.just(DbDrink.mapFromDrink(drink))
                .map(mDBDrinkRepository::add)
                .map(Drink::mapFromDBDrink);
    }

    public Observable<Float> getDayStatistic() {
        return Observable.just(mDBDrinkRepository.getDayStatistic());
    }

    public Observable<StatisticModel> getByPeriod(int typeQuery) {
        return Observable.just(mDBDrinkRepository.getByPeriod(typeQuery))
                .map(result -> new StatisticModel(StatisticType.values()[typeQuery], result));
    }

    public Observable<Boolean> del(long Timestamp) {
        return Observable.just(mDBDrinkRepository.deleteAllWithTimestamp(Timestamp));
    }
}
