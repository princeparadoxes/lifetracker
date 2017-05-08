package com.princeparadoxes.watertracker.data.db.model;

import com.princeparadoxes.watertracker.data.model.Drink;

import io.realm.RealmObject;

/**
 * Created by as3co on 06.05.2017.
 */

public class DBDrink extends RealmObject {

    private long mTimestamp;
    private int mSize;

    public DBDrink() {

    }

    public DBDrink(int mSize, long mTimestamp) {

        this.mSize = mSize;
        this.mTimestamp = mTimestamp;
    }


    public static DBDrink mapFromDrink(Drink drink) {

        return new DBDrink(drink.getSize(),drink.getTimestamp());

    }

    public int getSize() {
        return mSize;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public void setSize(int mSize) {
        this.mSize = mSize;
    }

    public void setTimestamp(long mTimestamp) {
        this.mTimestamp = mTimestamp;
    }
}
