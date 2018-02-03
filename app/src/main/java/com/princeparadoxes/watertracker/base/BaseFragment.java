package com.princeparadoxes.watertracker.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.princeparadoxes.watertracker.utils.KeyboardUtils;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseFragment extends Fragment {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(layoutId(), container, false);
        ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStop() {
        KeyboardUtils.hideSoftKeyboard(getActivity());
        super.onStop();
        compositeDisposable.clear();
    }

    public void unsubscribeOnStop(Disposable disposable){
        compositeDisposable.add(disposable);
    }

    public void unsubscribeOnStop(Disposable... disposable){
        compositeDisposable.addAll(disposable);
    }

    protected abstract int layoutId();
}
