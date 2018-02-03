package com.princeparadoxes.watertracker.data.source.db.model

import io.realm.RealmObject

open class DrinkSchema() : RealmObject() {

    var size: Int = 0
    var timestamp: Long = 0

    constructor(size: Int, timestamp: Long) : this() {
        this.size = size
        this.timestamp = timestamp
    }

}
