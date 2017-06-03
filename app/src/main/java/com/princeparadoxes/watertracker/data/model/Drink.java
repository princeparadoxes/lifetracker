package com.princeparadoxes.watertracker.data.model;

import com.princeparadoxes.watertracker.data.db.model.DbDrink;


public class Drink {

    private final int mSize;
    private final long mTimestamp;

    public Drink(int mSize, long mTimestamp) {
        this.mSize = mSize;
        this.mTimestamp = mTimestamp;
    }

    public static Drink mapFromDBDrink(DbDrink dbDrink) {

        return new Drink(dbDrink.getSize(), dbDrink.getTimestamp());
    }

    public int getSize() {
        return mSize;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

}
