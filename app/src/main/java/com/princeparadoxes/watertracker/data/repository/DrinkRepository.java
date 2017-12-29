package com.princeparadoxes.watertracker.data.repository;

import com.princeparadoxes.watertracker.ApplicationScope;
import com.princeparadoxes.watertracker.data.db.model.DbDrink;
import com.princeparadoxes.watertracker.data.db.repository.DBDrinkRepository;
import com.princeparadoxes.watertracker.data.model.Drink;
import com.princeparadoxes.watertracker.data.model.StatisticType;
import com.princeparadoxes.watertracker.data.sp.ProjectPreferences;
import com.princeparadoxes.watertracker.ui.screen.main.statistic.StatisticModel;

import javax.inject.Inject;

import io.reactivex.Observable;

@ApplicationScope
public class DrinkRepository {

    private final DBDrinkRepository mDBDrinkRepository;
    private final ProjectPreferences mProjectPreferences;

    @Inject
    public DrinkRepository(DBDrinkRepository dbDrinkRepository, ProjectPreferences projectPreferences) {
        mDBDrinkRepository = dbDrinkRepository;
        mProjectPreferences = projectPreferences;

    }

    public Observable<Drink> add(Drink drink) {
        return Observable.just(DbDrink.mapFromDrink(drink))
                .map(mDBDrinkRepository::add)
                .map(Drink::mapFromDBDrink);
    }

    public Observable<Float> getDaySum() {
        return Observable.just(mDBDrinkRepository.getDayStatistic());
    }

    public Observable<Float> getLastByDay() {
        return Observable.fromCallable(mDBDrinkRepository::getLastByDay);
    }

    public Observable<StatisticModel> getSumByPeriod(StatisticType statisticType) {
        return Observable.just(mDBDrinkRepository.getAllByPeriod(statisticType))
                .map(result -> {
                    int normValue = statisticType.getCountDays() * mProjectPreferences.getCurrentDayNorm();
                    return new StatisticModel(statisticType, result, normValue);
                });
    }

    public Observable<Boolean> del(long timestamp) {
        return Observable.just(mDBDrinkRepository.deleteAllWithTimestamp(timestamp));
    }
}
