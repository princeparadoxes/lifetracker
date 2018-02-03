package com.princeparadoxes.watertracker;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.princeparadoxes.watertracker.data.source.sp.DebugSharedPreferences;
import com.princeparadoxes.watertracker.data.source.sp.ProjectPreferences;
import com.princeparadoxes.watertracker.presentation.screen.main.MainActivity;

import java.lang.ref.WeakReference;

import io.palaima.debugdrawer.DebugDrawer;
import io.palaima.debugdrawer.actions.ActionsModule;
import io.palaima.debugdrawer.actions.ButtonAction;
import io.palaima.debugdrawer.commons.BuildModule;
import io.palaima.debugdrawer.commons.DeviceModule;
import io.palaima.debugdrawer.commons.SettingsModule;

public class DebugDrawerInitializer {

    private Application mApplication;
    private ProjectPreferences mPreferences;
    private DebugSharedPreferences mDebugSharedPreferences;
    private WeakReference<Activity> mActivityWeakReference;

    public DebugDrawerInitializer(Application application,
                                  ProjectPreferences preferences,
                                  DebugSharedPreferences debugSharedPreferences) {
        this.mApplication = application;
        this.mPreferences = preferences;
        this.mDebugSharedPreferences = debugSharedPreferences;
    }

    public DebugDrawer initDebugDrawer(Activity activity) {
        mActivityWeakReference = new WeakReference<>(activity);
        DebugDrawer.Builder builder = new DebugDrawer.Builder(activity);
        ButtonAction restartAppAction = new ButtonAction("Restart app",
                () -> {
                    restartApp();
                });

        builder.modules(
                new ActionsModule(restartAppAction),
                new DeviceModule(activity),
                new BuildModule(activity),
                new SettingsModule(activity));
        return builder.build();
    }

    private void restartApp() {
        Intent intent = new Intent(mApplication, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        mApplication.startActivity(intent);
        Activity activity = mActivityWeakReference.get();
        if (activity != null) {
            activity.finish();
        }
    }
}
