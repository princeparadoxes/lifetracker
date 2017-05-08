package com.princeparadoxes.watertracker.data.model;

import com.princeparadoxes.watertracker.data.db.model.DBDrink;


/**
 * Created by as3co on 06.05.2017.
 */

public class Drink {

    private final int mSize;
    private final long mTimestamp;

    public Drink(int mSize, long mTimestamp) {
        this.mSize = mSize;
        this.mTimestamp = mTimestamp;
    }

    public static Drink mapFromDBDrink(DBDrink dbDrink) {

        return new Drink(dbDrink.getSize(), dbDrink.getTimestamp());
    }

    public int getSize() {
        return mSize;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

}
