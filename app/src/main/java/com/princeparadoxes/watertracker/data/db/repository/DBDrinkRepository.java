package com.princeparadoxes.watertracker.data.db.repository;

import com.princeparadoxes.watertracker.ApplicationScope;
import com.princeparadoxes.watertracker.data.db.DBService;
import com.princeparadoxes.watertracker.data.db.model.DbDrink;
import com.princeparadoxes.watertracker.data.model.StatisticType;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

@ApplicationScope
public class DBDrinkRepository {

    public static final long DAY_IN_MILLISECONDS = 24 * 60 * 60 * 1000;

    @Inject
    public DBDrinkRepository(DBService dbService) {

    }

    public DbDrink add(DbDrink dbDrink) {
        Realm.getDefaultInstance().executeTransaction(realm -> realm.copyToRealm(dbDrink));
        return dbDrink;
    }

    public Float getDayStatistic() {
        return getAllByPeriod(StatisticType.DAY);
    }

    public Float getAllByPeriod(StatisticType statisticType) {
        Calendar currentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        currentTime.setTimeZone(TimeZone.getDefault());
        currentTime.set(Calendar.HOUR, 0);
        currentTime.set(Calendar.MINUTE, 0);
        currentTime.set(Calendar.SECOND, 0);
        currentTime.set(Calendar.MILLISECOND, 0);

        switch (statisticType) {
            case DAY:
                break;
            case WEEK:
                currentTime.add(Calendar.WEEK_OF_YEAR, -1);
                break;
            case MONTH:
                currentTime.add(Calendar.MONTH, -1);
                break;
            case YEAR:
                currentTime.add(Calendar.YEAR, -1);
                break;
            default:
                throw new IllegalArgumentException("Wrong statistic type");
        }

        long timeInMillis = currentTime.getTimeInMillis();

        float result = Realm.getDefaultInstance()
                .where(DbDrink.class)
                .greaterThan("timestamp", timeInMillis)
                .findAll()
                .sum("size")
                .floatValue();

        return result;
    }

    public Float getLastByDay() {
        Calendar currentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        currentTime.setTimeZone(TimeZone.getDefault());
        currentTime.set(Calendar.HOUR, 0);
        currentTime.set(Calendar.MINUTE, 0);
        currentTime.set(Calendar.SECOND, 0);
        currentTime.set(Calendar.MILLISECOND, 0);

        List<DbDrink> dayDrinks = Realm.getDefaultInstance()
                .where(DbDrink.class)
                .greaterThan("timestamp", currentTime.getTimeInMillis())
                .findAllSorted("timestamp");

        return (float) dayDrinks.get(dayDrinks.size() - 1).getSize();
    }

    public Boolean deleteAllWithTimestamp(final long timestamp) {
        Realm.getDefaultInstance().beginTransaction();
        RealmResults<DbDrink> dbDrinks = Realm.getDefaultInstance().where(DbDrink.class)
                .equalTo("timestamp", timestamp)
                .findAll();

        boolean result = dbDrinks.deleteAllFromRealm();
        Realm.getDefaultInstance().commitTransaction();
        return result;
    }
}
