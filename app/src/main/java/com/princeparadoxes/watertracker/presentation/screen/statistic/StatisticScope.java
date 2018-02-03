package com.princeparadoxes.watertracker.presentation.screen.statistic;

import com.princeparadoxes.watertracker.ProjectComponent;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface StatisticScope {

    @StatisticScope
    @dagger.Component(dependencies = ProjectComponent.class)
    interface Component {
        void inject(StatisticFragment fragment);
    }

}
