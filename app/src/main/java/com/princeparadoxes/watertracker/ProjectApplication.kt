package com.princeparadoxes.watertracker

import android.app.Application
import android.content.Context
import android.support.v7.app.AppCompatDelegate

import com.princeparadoxes.watertracker.base.lifecycle.Foreground
import com.squareup.leakcanary.LeakCanary

import io.realm.Realm
import timber.log.Timber

open class ProjectApplication : Application(), Foreground.Listener {

    override fun onCreate() {
        super.onCreate()
        instance = this
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        if (BuildConfig.DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not bind your app in this process.
                return
            }
            LeakCanary.install(this)
            Timber.plant(Timber.DebugTree())
        }

        Foreground.init(this)
        Foreground.get().addListener(this)

        buildComponentAndInject()
        Realm.init(this)
    }

    open fun buildComponentAndInject() {
        mComponent = ProjectComponent.Initializer.init(this)
        mComponent.inject(this)
    }

    override fun onForeground() {
        // Invoked when app goes out to background;
        Timber.d("App in foreground")
    }

    override fun onBackground() {
        // Invoked when app comes back from background;
        Timber.d("App in background")
    }

    companion object {


        private lateinit var mComponent: ProjectComponent
        lateinit var instance: Context

        init {
            System.loadLibrary("liquidfun")
            System.loadLibrary("liquidfun_jni")
        }

        fun component(): ProjectComponent? {
            return mComponent
        }

    }

}
