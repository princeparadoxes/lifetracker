package com.princeparadoxes.watertracker.base;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.princeparadoxes.watertracker.ProjectApplication;
import com.princeparadoxes.watertracker.ProjectComponent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseActivity extends DebugBaseActivity {

    List<WeakReference<Fragment>> mFragList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        if (params != null) {
            onExtractParams(params);
        }

        onCreateComponent(ProjectApplication.get(this).component());
        if (layoutId() != null) {
            setContentView(layoutId());
        }

        onViewSetup();

        ButterKnife.bind(this);
    }

    protected void onExtractParams(Bundle params) {

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        mFragList.add(new WeakReference<>(fragment));
    }

    public List<Fragment> getActiveFragments() {
        ArrayList<Fragment> active = new ArrayList<>();
        for (WeakReference<Fragment> reference : mFragList) {
            Fragment fragment = reference.get();
            if (fragment != null) {
                if (fragment.isVisible()) {
                    active.add(fragment);
                }
            }
        }
        return active;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getActiveFragments()) {
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    //    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        Icepick.restoreInstanceState(this, savedInstanceState);
//        super.onRestoreInstanceState(savedInstanceState);
//    }

    protected abstract Integer layoutId();

    protected abstract void onCreateComponent(ProjectComponent projectComponent);

}
