package com.princeparadoxes.watertracker.presentation.screen.start;

import com.princeparadoxes.watertracker.ProjectComponent;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface StartScope {

    @StartScope
    @dagger.Component(dependencies = ProjectComponent.class)
    interface Component {
        void inject(StartFragment fragment);
    }

}
