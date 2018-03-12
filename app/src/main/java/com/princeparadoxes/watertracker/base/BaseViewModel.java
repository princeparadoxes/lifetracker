package com.princeparadoxes.watertracker.base;

import android.arch.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaseViewModel extends ViewModel {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    protected void unsubscribeOnClear(Disposable disposable){
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
