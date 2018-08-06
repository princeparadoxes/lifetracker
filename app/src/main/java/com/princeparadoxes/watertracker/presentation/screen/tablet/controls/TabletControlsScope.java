package com.princeparadoxes.watertracker.presentation.screen.tablet.controls;

import android.arch.lifecycle.ViewModelProviders;

import com.princeparadoxes.watertracker.ProjectComponent;
import com.princeparadoxes.watertracker.domain.interactor.DrinkOutputPort;
import com.princeparadoxes.watertracker.domain.interactor.settings.DayNormUseCase;
import com.princeparadoxes.watertracker.domain.interactor.settings.UserUseCase;
import com.princeparadoxes.watertracker.presentation.screen.settings.SettingsScope;
import com.princeparadoxes.watertracker.presentation.screen.settings.SettingsViewModel;
import com.princeparadoxes.watertracker.presentation.screen.statistic.StatisticViewModelFactory;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

import dagger.Provides;

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface TabletControlsScope {

    @TabletControlsScope
    @dagger.Component(dependencies = ProjectComponent.class, modules = Module.class)
    interface Component {
        void inject(TabletControlsFragment fragment);
    }

    @dagger.Module
    public class Module {

        private final TabletControlsFragment tabletControlsFragment;

        public Module(TabletControlsFragment tabletControlsFragment) {
            this.tabletControlsFragment = tabletControlsFragment;
        }

        @TabletControlsScope
        @Provides
        TabletControlsViewModel provideTabletControlsViewModel(DrinkOutputPort drinkOutputPort) {
            TabletControlsViewModel.Factory factory = new TabletControlsViewModel.Factory(
                    drinkOutputPort);
            return ViewModelProviders.of(tabletControlsFragment, factory).get(factory.clazz());
        }
    }
}
