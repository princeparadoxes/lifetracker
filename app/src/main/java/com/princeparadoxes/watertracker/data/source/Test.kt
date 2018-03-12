package com.princeparadoxes.watertracker.data.source

import java.util.ArrayList

import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class Test {

    private fun sd() {
        Observable.just(ArrayList<Int>())
                .zipWith<ArrayList<Int>, Boolean>(Observable.just(ArrayList()), BiFunction { obj, c -> obj.addAll(c) })
    }
}
