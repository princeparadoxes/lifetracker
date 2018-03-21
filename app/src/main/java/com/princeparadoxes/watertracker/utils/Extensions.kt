package com.princeparadoxes.watertracker.utils

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.Disposable
import timber.log.Timber
import java.util.*


fun Calendar.getForTimestamp(timestamp: Long, field: Int): Int {
    return Calendar.getInstance().apply { timeInMillis = timestamp }.get(field)
}

fun Long.toCalendar() : Calendar {
    return Calendar.getInstance().apply { timeInMillis = this@toCalendar }
}

fun Observable<Any>.toTransformer() : ObservableTransformer<Any, Any> {
    return ObservableTransformer { this }
}

fun <T> Observable<T>.safeSubscribe(action: (T) -> Unit): Disposable {
    return this.subscribe({ action.invoke(it) }, { Timber.e(it) })
}