package com.princeparadoxes.watertracker.ui.screen.main;

import com.princeparadoxes.watertracker.ProjectComponent;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface MainScope {

    @MainScope
    @dagger.Component(dependencies = ProjectComponent.class)
    interface Component {
        void inject(MainActivity activity);
    }

}
