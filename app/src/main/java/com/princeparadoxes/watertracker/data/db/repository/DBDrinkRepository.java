package com.princeparadoxes.watertracker.data.db.repository;

import com.princeparadoxes.watertracker.ApplicationScope;
import com.princeparadoxes.watertracker.data.db.DBService;
import com.princeparadoxes.watertracker.data.db.model.DbDrink;

import java.util.Calendar;
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
        return getByPeriod(0);
    }

    public Float getByPeriod(int typeQuery) {
        Calendar currentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        currentTime.setTimeZone(TimeZone.getDefault());

        currentTime.set(Calendar.HOUR, 0);
        currentTime.set(Calendar.MINUTE, 0);
        currentTime.set(Calendar.SECOND, 0);
        currentTime.set(Calendar.MILLISECOND, 0);

        switch (typeQuery) {
            case 0:
                break;
            case 1:
                currentTime.add(Calendar.DAY_OF_YEAR, -1);
                break;
            case 2:
                currentTime.add(Calendar.WEEK_OF_YEAR, -1);
                break;
            case 3:
                currentTime.add(Calendar.YEAR, -1);
                break;
        }


        long timeInMillis = currentTime.getTimeInMillis();

        float result = Realm.getDefaultInstance().
                where(DbDrink.class).
                greaterThan("mTimestamp", timeInMillis).
                findAll()
                .sum("mSize").floatValue();

        Number number = Realm.getDefaultInstance().
                where(DbDrink.class).
                greaterThan("mTimestamp", timeInMillis).
                findAll().min("mSize");
        long minTimestamp = 0;
        if(number != null) minTimestamp = number.longValue();

        float deltaDay = (System.currentTimeMillis() - minTimestamp) / DAY_IN_MILLISECONDS;

        return result / deltaDay;
    }

    public Boolean deleteAllWithTimestamp(final long timestamp) {
        Realm.getDefaultInstance().beginTransaction();
        RealmResults<DbDrink> dbDrinks = Realm.getDefaultInstance().where(DbDrink.class)
                .equalTo("mTimestamp", timestamp)
                .findAll();

        boolean result = dbDrinks.deleteAllFromRealm();
        Realm.getDefaultInstance().commitTransaction();
        return result;
    }
}
