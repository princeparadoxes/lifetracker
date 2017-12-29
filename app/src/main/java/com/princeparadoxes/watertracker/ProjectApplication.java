package com.princeparadoxes.watertracker;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.princeparadoxes.watertracker.base.lifecycle.Foreground;
import com.princeparadoxes.watertracker.service.network.NetworkReceiver;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

public class ProjectApplication extends Application implements Foreground.Listener {


    private NetworkReceiver mNetworkReceiver;
    private ProjectComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        System.loadLibrary("liquidfun");
        System.loadLibrary("liquidfun_jni");

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not bind your app in this process.
            return;
        }
        LeakCanary.install(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        Foreground.init(this);
        Foreground.get().addListener(this);

        buildComponentAndInject();

        mNetworkReceiver = new NetworkReceiver(isConnected -> {
        });
        mNetworkReceiver.register(this);
    }

    public void buildComponentAndInject() {
        mComponent = ProjectComponent.Initializer.init(this);
        mComponent.inject(this);
    }

    public static ProjectApplication get(Context context) {
        return (ProjectApplication) context.getApplicationContext();
    }

    public ProjectComponent component() {
        return mComponent;
    }

    @Override
    public void onForeground() {
        // Invoked when app goes out to background;
        Timber.d("App in foreground");
    }

    @Override
    public void onBackground() {
        // Invoked when app comes back from background;
        Timber.d("App in background");
    }

}
