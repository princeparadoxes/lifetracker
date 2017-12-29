package com.princeparadoxes.watertracker;

import com.facebook.stetho.Stetho;

public class DebugProjectApplication extends ProjectApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }

    @Override
    public void buildComponentAndInject() {
        super.buildComponentAndInject();
        component().inject(this);
    }
}
