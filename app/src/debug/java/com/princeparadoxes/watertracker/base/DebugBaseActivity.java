package com.princeparadoxes.watertracker.base;

import android.support.v4.app.FragmentActivity;

import com.princeparadoxes.watertracker.BuildConfig;
import com.princeparadoxes.watertracker.DebugDrawerInitializer;

import javax.inject.Inject;

public class DebugBaseActivity extends FragmentActivity {

    @Inject
    DebugDrawerInitializer mDebugDrawerInitializer;

    protected void onViewSetup() {
        if (BuildConfig.DEBUG) {
            if (mDebugDrawerInitializer == null) {
                throw new RuntimeException("You need create component for this activity");
            }
            mDebugDrawerInitializer.initDebugDrawer(this);
        }

    }
}
