package com.princeparadoxes.watertracker.data.db.model;

import com.princeparadoxes.watertracker.data.model.Drink;

import io.realm.RealmObject;

/**
 * Created by as3co on 06.05.2017.
 */

public class DbDrink extends RealmObject {

    private long timestamp;
    private int size;

    public DbDrink() {

    }

    public DbDrink(int size, long timestamp) {
        this.size = size;
        this.timestamp = timestamp;
    }


    public static DbDrink mapFromDrink(Drink drink) {

        return new DbDrink(drink.getSize(), drink.getTimestamp());

    }

    public int getSize() {
        return size;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setSize(int mSize) {
        this.size = mSize;
    }

    public void setTimestamp(long mTimestamp) {
        this.timestamp = mTimestamp;
    }
}
