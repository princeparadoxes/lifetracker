package com.princeparadoxes.watertracker;

import android.app.Application;

import com.princeparadoxes.watertracker.base.lifecycle.Foreground;

import dagger.Module;
import dagger.Provides;

@Module
public class ProjectModule {

    private ProjectApplication mApplication;

    public ProjectModule(ProjectApplication application) {
        this.mApplication = application;
    }

    @Provides
    @ApplicationScope
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationScope
    Foreground provideForeground() {
        return Foreground.get();
    }

}
