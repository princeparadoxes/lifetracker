package com.princeparadoxes.watertracker.data.db.repository;

import com.princeparadoxes.watertracker.ApplicationScope;
import com.princeparadoxes.watertracker.data.db.DBService;
import com.princeparadoxes.watertracker.data.db.model.DBDrink;
import com.princeparadoxes.watertracker.data.model.Drink;

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

    public static Number getDayStatistic() {
        Number result = Realm.getDefaultInstance().where(DBDrink.class).findAll().sum("mSize");

         return result;
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
