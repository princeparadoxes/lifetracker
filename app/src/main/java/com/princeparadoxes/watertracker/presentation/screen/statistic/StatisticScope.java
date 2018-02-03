package com.princeparadoxes.watertracker.presentation.screen.statistic;

import android.arch.lifecycle.ViewModelProviders;

import com.princeparadoxes.watertracker.ProjectComponent;
import com.princeparadoxes.watertracker.domain.interactor.DrinkOutputGateway;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

import dagger.Module;
import dagger.Provides;

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface StatisticScope {

    @StatisticScope
    @dagger.Component(dependencies = ProjectComponent.class, modules = Module.class)
    interface Component {
        void inject(StatisticFragment fragment);
    }

    @dagger.Module
    public class Module {

        private final StatisticFragment statisticFragment;

        public Module(StatisticFragment statisticFragment) {
            this.statisticFragment = statisticFragment;
        }

        @StatisticScope
        @Provides
        StatisticViewModel provideStatisticViewModel(DrinkOutputGateway drinkOutputGateway) {
            ViewModelFactory factory = new ViewModelFactory(drinkOutputGateway);
            return ViewModelProviders.of(statisticFragment, factory).get(StatisticViewModel.class);
        }
    }
}
