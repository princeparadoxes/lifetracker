package com.princeparadoxes.watertracker

import android.support.v4.content.ContextCompat
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import timber.log.Timber
import java.util.*


fun Calendar.getForTimestamp(timestamp: Long, field: Int): Int {
    return Calendar.getInstance().apply { timeInMillis = timestamp }.get(field)
}

fun Long.toCalendar(): Calendar {
    return Calendar.getInstance().apply { timeInMillis = this@toCalendar }
}

fun Observable<Any>.toTransformer(): ObservableTransformer<Any, Any> {
    return ObservableTransformer { this }
}

fun <T> Observable<T>.safeSubscribe(action: (T) -> Unit): Disposable {
    return this.subscribe({ action.invoke(it) }, { Timber.e(it) })
}

fun <T> Single<T>.safeSubscribe(action: (T) -> Unit): Disposable {
    return this.subscribe({ action.invoke(it) }, { Timber.e(it) })
}

fun Int.toColorInt(): Int {
    return ContextCompat.getColor(ProjectApplication.instance, this)
}

fun <T, R> Observable<T>.zipToPair(another: Observable<R>): Observable<Pair<T, R>> {
    return this.zipWith(another, BiFunction<T, R, Pair<T, R>> { t1, t2 -> t1 to t2 })
}