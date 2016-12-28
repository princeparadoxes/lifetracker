package com.princeparadoxes.watertracker.data.rx;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SchedulerTransformer<T>
        implements ObservableTransformer<T, T>, FlowableTransformer<T, T> {

    private static SchedulerTransformer<Object> instance = new SchedulerTransformer<>();

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Publisher<T> apply(Flowable<T> upstream) {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressWarnings("unchecked")
    public static <T> SchedulerTransformer<T> getInstance() {
        return (SchedulerTransformer<T>) instance;
    }


}
