package com.princeparadoxes.watertracker;

import android.app.Application;

import com.princeparadoxes.watertracker.base.lifecycle.Foreground;
import com.princeparadoxes.watertracker.domain.DomainModule;

import dagger.Module;
import dagger.Provides;

@Module (includes = DomainModule.class)
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
