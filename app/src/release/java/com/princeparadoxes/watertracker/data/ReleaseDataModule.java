package com.princeparadoxes.watertracker.data;

import android.app.Application;

import com.princeparadoxes.watertracker.ApplicationScope;
import com.princeparadoxes.watertracker.data.api.ReleaseRestModule;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module(includes = {DataModule.class, ReleaseRestModule.class})
public class ReleaseDataModule {

    @Provides
    @ApplicationScope
    OkHttpClient provideOkHttpClient(Application app) {
        return DataModule.createOkHttpClient(app).build();
    }

}
