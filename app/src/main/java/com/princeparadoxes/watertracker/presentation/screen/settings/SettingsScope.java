package com.princeparadoxes.watertracker.presentation.screen.settings;

import android.arch.lifecycle.ViewModelProviders;

import com.princeparadoxes.watertracker.ProjectComponent;
import com.princeparadoxes.watertracker.domain.interactor.DrinkOutputPort;
import com.princeparadoxes.watertracker.domain.interactor.settings.DayNormUseCase;
import com.princeparadoxes.watertracker.domain.interactor.settings.UserUseCase;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

import dagger.Provides;

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface SettingsScope {

    @SettingsScope
    @dagger.Component(dependencies = ProjectComponent.class, modules = Module.class)
    interface Component {
        void inject(SettingsFragment fragment);
    }

    @dagger.Module
    public class Module {

        private final SettingsFragment fragment;

        public Module(SettingsFragment fragment) {
            this.fragment = fragment;
        }

        @SettingsScope
        @Provides
        SettingsViewModel provideSettingsViewModel(DayNormUseCase dayNormUseCase,
                                                   UserUseCase userUseCase) {
            SettingsViewModel.Factory factory = new SettingsViewModel.Factory(
                    dayNormUseCase,
                    userUseCase);
            return ViewModelProviders.of(fragment, factory).get(factory.clazz());
        }
    }
}
