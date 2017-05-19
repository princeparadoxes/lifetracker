package com.princeparadoxes.watertracker.data.db.repository;

import com.princeparadoxes.watertracker.ApplicationScope;
import com.princeparadoxes.watertracker.data.db.DBService;
import com.princeparadoxes.watertracker.data.db.model.DBDrink;
import com.princeparadoxes.watertracker.data.model.Drink;

import java.util.Calendar;
import java.util.TimeZone;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by as3co on 06.05.2017.
 */

@ApplicationScope
public class DBDrinkRepository {

    @Inject
    public DBDrinkRepository(DBService dbService) {

    }

    public DBDrink add(DBDrink dbDrink) {

        Realm.getDefaultInstance().executeTransaction(realm -> realm.copyToRealm(dbDrink));

        return dbDrink;
    }

    public static float getDayStatistic() {
        return getByPeriod(0);
    }

    public static float getByPeriod(int typeQuery) {


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
                        where(DBDrink.class).
                        greaterThan("mTimestamp", timeInMillis).
                        findAll()
                        .sum("mSize").floatValue();

        long minTimestamp = Realm.getDefaultInstance().
                where(DBDrink.class).
                greaterThan("mTimestamp", timeInMillis).
                findAll().min("").longValue();

        long dayInMilliseconds = 86400000;

        float DeltaDey = (System.currentTimeMillis()-minTimestamp)/dayInMilliseconds;

        return result/DeltaDey;
    }

    public static void del(final long Timestamp) {

        Realm.getDefaultInstance().executeTransaction(realm -> {
            RealmResults<DBDrink> result = realm.where(DBDrink.class)
                    .equalTo("mTimestamp", Timestamp)
                    .findAll();

            result.deleteAllFromRealm();
        });
    }
}
